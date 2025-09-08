from deepface import DeepFace
from crud import get_all_user_images
from database import SessionLocal

async def recognize_face(uploaded_file):
    async with SessionLocal() as db:
        users = await get_all_user_images(db)

    best_match = None
    lowest_distance = float('inf')

    for user in users:
        try:
            result = DeepFace.verify(uploaded_file, user.image_path, enforce_detection=False)
            if result["verified"] and result["distance"] < 0.4:
                if result["distance"] < lowest_distance:
                    best_match = user.user_id
                    lowest_distance = result["distance"]
        except Exception:
            continue

    if best_match is not None:
        confidence = 1 - lowest_distance
    else:
        confidence = 0

    return {"user_id": best_match, "confidence": confidence}
