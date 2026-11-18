package com.taskflow.vegiees.service;

import com.taskflow.vegiees.dto.CloudinaryUploadResult;
import com.taskflow.vegiees.entity.ImageType;
import com.taskflow.vegiees.entity.Product;
import com.taskflow.vegiees.entity.ProductImage;
import org.springframework.stereotype.Service;


import java.io.IOException;

@Service
public class ProductImageService {

    public ProductImage createProductImage(
            Product product,
            CloudinaryUploadResult uploadResult,
            ImageType imageType,
            Integer displayOrder
    ){

        ProductImage productImage = new ProductImage();

        productImage.setImageUrl(uploadResult.imageUrl());
        productImage.setPublicId(uploadResult.publicId());
        productImage.setProduct(product);
        productImage.setImageType(imageType);
        productImage.setDisplayOrder(displayOrder);

        return productImage;

    }
}
