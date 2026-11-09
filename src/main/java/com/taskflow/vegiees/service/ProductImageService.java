package com.taskflow.vegiees.service;

import com.taskflow.vegiees.dto.CloudinaryUploadResult;
import com.taskflow.vegiees.entity.ImageType;
import com.taskflow.vegiees.entity.Product;
import com.taskflow.vegiees.entity.ProductImage;
import com.taskflow.vegiees.exception.ResourceNotFoundException;
import com.taskflow.vegiees.repository.ProductImageRepository;
import com.taskflow.vegiees.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ProductImageService {

    private final ImageValidationService imageValidationService;
    private final CloudinaryService cloudinaryService;
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    public ProductImageService(ImageValidationService imageValidationService,
                               CloudinaryService cloudinaryService,
                               ProductImageRepository productImageRepository,
                               ProductRepository productRepository) {
        this.imageValidationService = imageValidationService;
        this.cloudinaryService = cloudinaryService;
        this.productImageRepository = productImageRepository;
        this.productRepository = productRepository;
    }

    public Product getProduct(Long productId){

        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
    }

    public ProductImage uploadProductImage(
            Long productId,
            MultipartFile file,
            ImageType imageType,
            Integer displayOrder
    ) throws IOException {

        ImageValidationService.validate(file);

        Product product = getProduct(productId);

        CloudinaryUploadResult uploadResult = cloudinaryService.uploadImage(file);

        ProductImage productImage = new ProductImage();

        productImage.setImageUrl(uploadResult.imageUrl());
        productImage.setPublicId(uploadResult.publicId());
        productImage.setProduct(product);
        productImage.setImageType(imageType);
        productImage.setDisplayOrder(displayOrder);

        return productImageRepository.save(productImage);
    }
}
