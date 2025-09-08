import shutil
from sqlalchemy.future import select
from sqlalchemy.ext.asyncio import AsyncSession
from models import UserImage
from pathlib import Path

IMAGES_DIR = Path("images")
IMAGES_DIR.mkdir(exist_ok=True)

async def store_user_image(db: AsyncSession, user_id: str, file):
    image_path = IMAGES_DIR / f"{user_id}.jpg"
    
    with open(image_path, "wb") as buffer:
        shutil.copyfileobj(file.file, buffer)
    
    user_image = UserImage(user_id=user_id, image_path=str(image_path))
    db.add(user_image)
    await db.commit()
    return user_image

async def get_all_user_images(db: AsyncSession):
    result = await db.execute(select(UserImage))
    return result.scalars().all()
