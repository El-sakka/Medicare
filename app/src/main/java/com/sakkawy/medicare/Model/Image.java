package com.sakkawy.medicare.Model;

public class Image {
    private String userId;
    private String parentId;
    private String imageUrl;

    public Image() {
    }

    public Image(String userId, String parentId, String imageUrl) {
        this.userId = userId;
        this.parentId = parentId;
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
