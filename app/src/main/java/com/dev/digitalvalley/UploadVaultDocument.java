package com.dev.digitalvalley;

import com.google.firebase.database.Exclude;

public class UploadVaultDocument {
    private String imageName;
    private String imageUrl;
    private String imageKey;

    public UploadVaultDocument() {
        //empty constructor
    }

    public UploadVaultDocument(String imageName, String imageUrl) {
        if (imageName.trim().equals("")) {
            imageName = "Untitled";
        }
        this.imageName = imageName;
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Exclude
    public String getImageKey() {
        return imageKey;
    }

    @Exclude
    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }
}
