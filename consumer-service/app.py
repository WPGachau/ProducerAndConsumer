from fastapi import FastAPI
import asyncio
from kafka_consumer import AnalyticsConsumer
from logging_config import setup_logging

setup_logging()

app = FastAPI()
consumer = AnalyticsConsumer()

@app.on_event("startup")
async def startup_event():
    asyncio.create_task(consumer.start())

@app.get("/health")
def health():
    return {"status": "ok"}
