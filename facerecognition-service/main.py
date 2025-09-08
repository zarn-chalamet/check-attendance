from fastapi import FastAPI, UploadFile, File
from database import Base, engine, SessionLocal
import crud
from face_recognition_utils import recognize_face

app = FastAPI(title="Face Recognition Service")

# Create DB tables on startup
@app.on_event("startup")
async def init_db():
    async with engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)

@app.post("/api/store-user-image")
async def store_user_image(user_id: str, file: UploadFile = File(...)):
    async with SessionLocal() as db:
        user_image = await crud.store_user_image(db, user_id, file)
    return {"user_id": user_image.user_id, "image_path": user_image.image_path}

@app.post("/api/face/recognize")
async def face_recognize(file: UploadFile = File(...)):
    temp_file = "temp.jpg"
    with open(temp_file, "wb") as f:
        f.write(await file.read())
    
    result = await recognize_face(temp_file)
    return result
