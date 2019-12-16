package com.example.dell.expelliarmus;

public class MineMessage {
    private String photo;
    private String photoName;
    private String photoMessage;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public MineMessage(String photoName, String photoMessage) {
        this.photoName = photoName;
        this.photoMessage = photoMessage;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getPhotoMessage() {
        return photoMessage;
    }

    public void setPhotoMessage(String photoMessage) {
        this.photoMessage = photoMessage;
    }
}
