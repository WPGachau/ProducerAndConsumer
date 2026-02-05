import httpx
import logging
from tenacity import retry, stop_after_attempt, wait_exponential
from config import ANALYTICS_URL

logger = logging.getLogger("analytics_client")

@retry(
    stop=stop_after_attempt(5),
    wait=wait_exponential(multiplier=2),
    reraise=True
)
async def send_to_analytics(payload: dict):

    async with httpx.AsyncClient(timeout=10.0) as client:
        response = await client.post(ANALYTICS_URL, json=payload)

        if response.status_code >= 400:
            logger.error("Analytics API error: %s", response.text)
            response.raise_for_status()

        logger.info("Sent event %s to Analytics", payload["eventId"])
