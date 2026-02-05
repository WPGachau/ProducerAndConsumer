import json
import logging
from aiokafka import AIOKafkaConsumer
from config import *
from idempotency import is_duplicate
from merger import merge_event
from analytics_client import send_to_analytics

logger = logging.getLogger("kafka_consumer")

class AnalyticsConsumer:

    def __init__(self):
        self.consumer = AIOKafkaConsumer(
            CUSTOMER_TOPIC,
            INVENTORY_TOPIC,
            bootstrap_servers=KAFKA_BOOTSTRAP,
            group_id=CONSUMER_GROUP,
            enable_auto_commit=False,
            value_deserializer=lambda v: json.loads(v.decode("utf-8"))
        )

    async def start(self):
        await self.consumer.start()
        logger.info("Kafka consumer started")

        try:
            async for msg in self.consumer:
                await self.process_message(msg)

        finally:
            await self.consumer.stop()

    async def process_message(self, msg):

        event = msg.value
        event_id = event.get("eventId")

        logger.info("Consumed event %s from topic %s", event_id, msg.topic)

        # Idempotency check
        if is_duplicate(event_id):
            logger.warning("Duplicate event detected: %s", event_id)
            await self.consumer.commit()
            return

        try:
            merged = merge_event(event)

            await send_to_analytics(merged)

            await self.consumer.commit()

            logger.info("Successfully processed event %s", event_id)

        except Exception as e:
            logger.error("Processing failed for event %s: %s", event_id, str(e))
            # Do not commit offset
            # Message will be retried
