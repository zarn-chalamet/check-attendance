from sqlalchemy import Column, String
from database import Base

class UserImage(Base):
    __tablename__ = "user_images"
    
    user_id = Column(String, primary_key=True, index=True)
    image_path = Column(String, nullable=False)
