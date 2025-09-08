from pydantic import BaseModel

class UserImageCreate(BaseModel):
    user_id: str
