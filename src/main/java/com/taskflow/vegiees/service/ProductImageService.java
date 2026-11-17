package com.taskflow.vegiees.service;

import com.taskflow.vegiees.dto.CloudinaryUploadResult;
import com.taskflow.vegiees.entity.ImageType;
import com.taskflow.vegiees.entity.Product;
import com.taskflow.vegiees.entity.ProductImage;
import com.taskflow.vegiees.entity.User;
import com.taskflow.vegiees.exception.ForbiddenException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ProductImageService {

    private final CloudinaryService cloudinaryService;
    private final CurrentUserService currentUserService;

    public ProductImageService(CloudinaryService cloudinaryService,
                               CurrentUserService currentUserService) {
        this.cloudinaryService = cloudinaryService;
        this.currentUserService = currentUserService;
    }

    public ProductImage uploadProductImage(
            Product product,
            MultipartFile file,
            ImageType imageType,
            Integer displayOrder
    ) throws IOException {

        User authenticatedUser = currentUserService.getAuthenticatedUser();

        if(!product.getSeller().getId().equals(authenticatedUser.getId())){
            throw new ForbiddenException("You are not allowed to upload images for this product");
        }

        CloudinaryUploadResult uploadResult = cloudinaryService.uploadImage(file);


        ProductImage productImage = new ProductImage();

        productImage.setImageUrl(uploadResult.imageUrl());
        productImage.setPublicId(uploadResult.publicId());
        productImage.setProduct(product);
        productImage.setImageType(imageType);
        productImage.setDisplayOrder(displayOrder);

        return productImage;

    }
}
