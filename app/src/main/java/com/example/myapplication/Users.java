package com.example.myapplication;

public class Users {
    String name, email, imageUri, UID;

    public Users() {}

    public Users(String name, String email, String imageUri, String UID) {
        this.name = name;
        this.email = email;
        this.imageUri = imageUri;
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
