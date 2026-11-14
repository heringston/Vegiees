package com.taskflow.vegiees.service;

import com.taskflow.vegiees.entity.ImageType;
import com.taskflow.vegiees.exception.InvalidImageException;
import com.taskflow.vegiees.repository.ProductImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Service
public class ImageValidationService {

    private final ProductImageRepository productImageRepository;

    public ImageValidationService(ProductImageRepository productImageRepository){
        this.productImageRepository = productImageRepository;
    }

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

    public void validateImageCount(Long productId , ImageType imageType){

        long existingCount = productImageRepository.countByProductIdAndImageType(
                productId , imageType
        );

        int maxAllowed = switch(imageType){
            case PRODUCT -> MAX_PRODUCT_IMAGES;
            case GROWING_SOURCE -> MAX_GROWING_SOURCE_IMAGES;
        };

        if(existingCount >= maxAllowed){
            throw new InvalidImageException("Maximum number of " + imageType + "images uploaded");
        }

    }
}
