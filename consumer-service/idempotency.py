import redis
from config import REDIS_HOST, REDIS_PORT, IDEMPOTENCY_TTL_SECONDS

redis_client = redis.Redis(host=REDIS_HOST, port=REDIS_PORT, decode_responses=True)

def is_duplicate(event_id: str) -> bool:
    """
    Returns True if event was already processed.
    """

    if redis_client.exists(event_id):
        return True

    redis_client.set(event_id, "1", ex=IDEMPOTENCY_TTL_SECONDS)
    return False
