from fastapi import FastAPI
from watchdog.observers import Observer
from watchdog.events import FileSystemEventHandler
from garmin_fit_sdk import Decoder, Stream
from contextlib import asynccontextmanager
import motor.motor_asyncio
import uvicorn
import asyncio

'''

Garmin API Collection Server:

(1) Uses Watchdog to listen for file updates in the FITActivity directory (where users should enter their daily FIT 
files from their Garmin device). Uses uvicorn to spawn a new instance of the API (in release this would be containerised with Docker and deployed on
Kubernetes cluster or AWS services).
(2) FIT Files are processed using the official Garmin SDK release which extracts functional readings (messages) and metrics which 
are not currently working (errors).

'''

# Parsing FIT files using Official Garmin SDK
async def process_FIT_file(path):
    print(f"Reading new file: {path}")
    try:
        # Obtain stream and decoder object for FIT representation.
        stream = Stream.from_file(path)
        decoder = Decoder(stream)
        # Read the FIT file
        messages, errors = decoder.read()
        entry = {"file_path": path, "messages": messages, "errors": errors}
        print("Entry written to DB: ", entry)
        result = collection.insert_one(entry)
        return entry, messages, errors, result

    except Exception as e:
        print(f"Streamed FIT file reading failed due to: {e}")



# API server setup
app = FastAPI()

# Configuration options
FIT_FILES_LOCATION = "./FITActivity"

# MongoDB setup
DB_ENDPOINT = "mongodb://localhost:27017"
client = motor.motor_asyncio.AsyncIOMotorClient(DB_ENDPOINT)
db_name = "FITActivityDB"
db = client[db_name]
collection = db['FITActivityCollection']


# Check if the database exists and create it if not
async def ensure_db_exists(client, db_name):
    db_names = await client.list_database_names()
    if db_name not in db_names:
        await client.create_database(db_name)


class FileEventHandler(FileSystemEventHandler):
    # In the event a new file is created in the specified folder.
    def on_created(self, event):
        print("Detected new file in target folder")
        # Spawn a new event loop to handle async processing (specifically DB write operation).
        loop = asyncio.new_event_loop()
        asyncio.set_event_loop(loop)

        if not event.is_directory:
            # Run the task to completion so it doesn't block the primary scheduler loop.
            result = loop.run_until_complete(self.run_task(event.src_path))
            print("Result of FIT processing: ", result)


    async def run_task(self, path):
        await process_FIT_file(path)


# Lifespan event handler
@asynccontextmanager
async def lifespan_handler(app: FastAPI):
    #await ensure_db_exists(client, db_name)
    observer = Observer()
    observer.schedule(FileEventHandler(), path=FIT_FILES_LOCATION, recursive=False)
    observer.start()
    yield
    # Shutdown logic
    observer.stop()
    observer.join()

# Apply the lifespan event handler
app = FastAPI(lifespan=lifespan_handler)

if __name__ == "__main__":
    # Hosting service on localhost:8000.
    uvicorn.run("garmin_collection_server:app", host="0.0.0.0", port=8000, log_level="info")
