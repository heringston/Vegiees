package com.taskflow.vegiees.dto;

import com.taskflow.vegiees.entity.ImageType;

public class ProductImageResponse {

    private String imageUrl;
    private ImageType imageType;
    private int displayOrder;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ImageType getImageType() {
        return imageType;
    }

    public void setImageType(ImageType imageType) {
        this.imageType = imageType;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
