from fastapi import FastAPI, UploadFile, Form
import sqlite3
import shutil
import os

app = FastAPI(title="Face Recognition Service")

# Initialize database
conn = sqlite3.connect("users.db", check_same_thread=False)
cursor = conn.cursor()
cursor.execute("""
CREATE TABLE IF NOT EXISTS user_images (
    id TEXT PRIMARY KEY,
    image_path TEXT
)
""")
conn.commit()

# Folder to store images
os.makedirs("images", exist_ok=True)

@app.post("/api/store-user-image")
async def store_user_image(user_id: str = Form(...), file: UploadFile = None):
    if not file:
        return {"error": "No image uploaded"}
    
    file_path = f"images/{user_id}.jpg"
    with open(file_path, "wb") as buffer:
        shutil.copyfileobj(file.file, buffer)

    cursor.execute("INSERT OR REPLACE INTO user_images (id, image_path) VALUES (?, ?)", 
                   (user_id, file_path))
    conn.commit()

    return {"message": "User image stored successfully", "user_id": user_id, "path": file_path}
