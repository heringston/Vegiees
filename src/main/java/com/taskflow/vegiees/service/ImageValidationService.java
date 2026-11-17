package com.taskflow.vegiees.service;

import com.taskflow.vegiees.entity.ImageType;
import com.taskflow.vegiees.entity.ProductImage;
import com.taskflow.vegiees.exception.InvalidImageException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Service
public class ImageValidationService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static final int MAX_PRODUCT_IMAGES = 5;
    private static final int MAX_GROWING_SOURCE_IMAGES = 1;

    private static final Set<String> ALLOWED_FILE_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    public void validate(MultipartFile file){

        if(file == null || file.isEmpty()){
            throw new InvalidImageException("Image file must not be empty");
        }

        if(file.getSize() > MAX_FILE_SIZE){
            throw new InvalidImageException("Image size must not exceed 5 MB");
        }

        String contentType = file.getContentType();

        if(contentType == null || !ALLOWED_FILE_TYPES.contains(contentType)){
            throw new InvalidImageException("Only JPEG , PNG and WebP images are allowed");
        }
    }

    public void validateListingImages(List<MultipartFile> productImages ,
                                     MultipartFile growingSourceImage){

        if(productImages == null || productImages.isEmpty()){
            throw new InvalidImageException("Minimum 1 Product Image is required");
        }

        if(productImages.size() > 5){
            throw new InvalidImageException("Maximum 5 Product Images are allowed");
        }

        for(MultipartFile image : productImages){
            validate(image);
        }

        if(growingSourceImage != null){
            validate(growingSourceImage);
        }
    }
}
