from typing import Dict, Any

def merge_event(event: Dict[str, Any]) -> Dict[str, Any]:
    """
    In this simplified implementation,
    we pass the event as-is.
    
    In production:
    - Join customer + inventory on keys
    - Enrich with derived metrics
    """

    return {
        "eventId": event["eventId"],
        "eventType": event["eventType"],
        "sourceSystem": event["sourceSystem"],
        "timestamp": event["timestamp"],
        "payload": event["payload"]
    }
