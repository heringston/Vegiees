package com.taskflow.vegiees.service;

import com.taskflow.vegiees.dto.CloudinaryUploadResult;
import com.taskflow.vegiees.entity.ImageType;
import com.taskflow.vegiees.entity.Product;
import com.taskflow.vegiees.entity.ProductImage;
import com.taskflow.vegiees.entity.User;
import com.taskflow.vegiees.exception.ForbiddenException;
import com.taskflow.vegiees.exception.ResourceNotFoundException;
import com.taskflow.vegiees.repository.ProductImageRepository;
import com.taskflow.vegiees.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ProductImageService {

    private final ImageValidationService imageValidationService;
    private final CloudinaryService cloudinaryService;
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final CurrentUserService currentUserService;

    public ProductImageService(ImageValidationService imageValidationService,
                               CloudinaryService cloudinaryService,
                               ProductImageRepository productImageRepository,
                               ProductRepository productRepository,
                               CurrentUserService currentUserService) {
        this.imageValidationService = imageValidationService;
        this.cloudinaryService = cloudinaryService;
        this.productImageRepository = productImageRepository;
        this.productRepository = productRepository;
        this.currentUserService = currentUserService;
    }

    public Product getProduct(Long productId){

        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product Not Found"));
    }

    @Transactional
    public ProductImage uploadProductImage(
            Long productId,
            MultipartFile file,
            ImageType imageType,
            Integer displayOrder
    ) throws IOException {

        imageValidationService.validate(file);

        Product product = getProduct(productId);
        User authenticatedUser = currentUserService.getAuthenticatedUser();

        if(!product.getSeller().getId().equals(authenticatedUser.getId())){
            throw new ForbiddenException("You are not allowed to upload images for this product");
        }

        imageValidationService.validateImageCount(productId , imageType);

        CloudinaryUploadResult uploadResult = cloudinaryService.uploadImage(file);

        try {

            ProductImage productImage = new ProductImage();

            productImage.setImageUrl(uploadResult.imageUrl());
            productImage.setPublicId(uploadResult.publicId());
            productImage.setProduct(product);
            productImage.setImageType(imageType);
            productImage.setDisplayOrder(displayOrder);

            return productImageRepository.save(productImage);
        }catch(RuntimeException ex){

            try{
                cloudinaryService.deleteImage(uploadResult.publicId());
            }catch (IOException cleanupException){
                ex.addSuppressed(cleanupException);
            }

            throw ex;
        }
    }
}
