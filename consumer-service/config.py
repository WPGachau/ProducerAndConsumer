import os

KAFKA_BOOTSTRAP = os.getenv("KAFKA_BOOTSTRAP", "localhost:9092")
CUSTOMER_TOPIC = "customer_data"
INVENTORY_TOPIC = "inventory_data"

CONSUMER_GROUP = "analytics-consumer-group"

REDIS_HOST = os.getenv("REDIS_HOST", "localhost")
REDIS_PORT = 6379

ANALYTICS_URL = os.getenv("ANALYTICS_URL", "http://localhost:8085/analytics/data")

IDEMPOTENCY_TTL_SECONDS = 60 * 60 * 24 * 7  # 7 days
