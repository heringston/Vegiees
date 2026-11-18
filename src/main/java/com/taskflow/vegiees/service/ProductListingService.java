package com.taskflow.vegiees.service;

import com.taskflow.vegiees.dto.CloudinaryUploadResult;
import com.taskflow.vegiees.dto.CreateProductRequest;
import com.taskflow.vegiees.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductListingService {

    private final ImageValidationService imageValidationService;
    private final CloudinaryService cloudinaryService;
    private final ProductListingPersistenceService productListingPersistenceService;

    public ProductListingService(ImageValidationService imageValidationService,
                                 CloudinaryService cloudinaryService,
                                 ProductListingPersistenceService productListingPersistenceService) {
        this.imageValidationService = imageValidationService;
        this.cloudinaryService = cloudinaryService;
        this.productListingPersistenceService = productListingPersistenceService;
    }

    public Product createListing(CreateProductRequest request,
                                 List<MultipartFile> productImages,
                                 MultipartFile growingSourceImage) throws IOException {

        imageValidationService.validateListingImages(productImages , growingSourceImage);

        List<CloudinaryUploadResult> uploadedProductImages = new ArrayList<>();
        CloudinaryUploadResult uploadedGrowingSourceImage = null;

        try{

            for(MultipartFile image : productImages){
                CloudinaryUploadResult uploadedImage = cloudinaryService.uploadImage(image);

                uploadedProductImages.add(uploadedImage);
            }

            if(growingSourceImage != null){

                CloudinaryUploadResult uploadedSource = cloudinaryService.uploadImage(growingSourceImage);

                uploadedGrowingSourceImage = uploadedSource;
            }

            return productListingPersistenceService.persistListing(request,
                    uploadedProductImages, uploadedGrowingSourceImage);

        } catch (IOException | RuntimeException ex){

            for(CloudinaryUploadResult uploadedImage : uploadedProductImages){

                try{
                    cloudinaryService.deleteImage(uploadedImage.publicId());
                } catch (Exception cleanupException){
                    ex.addSuppressed(cleanupException);
                }
            }

            if(uploadedGrowingSourceImage != null){

                try{
                    cloudinaryService.deleteImage(uploadedGrowingSourceImage.publicId());
                } catch (Exception cleanupException){
                    ex.addSuppressed(cleanupException);
                }
            }

            throw ex;

        }

    }

}
