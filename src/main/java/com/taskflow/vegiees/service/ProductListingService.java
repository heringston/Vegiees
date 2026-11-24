package com.taskflow.vegiees.service;

import com.taskflow.vegiees.dto.CloudinaryUploadResult;
import com.taskflow.vegiees.dto.CreateProductRequest;
import com.taskflow.vegiees.entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.taskflow.vegiees.dto.ProductResponse;
import com.taskflow.vegiees.dto.ProductImageResponse;
import com.taskflow.vegiees.entity.ProductImage;

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

    public ProductResponse createListing(CreateProductRequest request,
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

            Product product = productListingPersistenceService.persistListing(request,
                    uploadedProductImages, uploadedGrowingSourceImage);

            return mapToResponse(product);

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

    private ProductResponse mapToResponse(Product product){

        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setTitle(product.getTitle());
        response.setCategory(product.getCategory());
        response.setDescription(product.getDescription());

        response.setQuantity(product.getQuantity());
        response.setQuantityUnit(product.getQuantityUnit());

        response.setPrice(product.getPrice());
        response.setPriceUnit(product.getPriceUnit());

        response.setHarvestDate(product.getHarvestDate());

        response.setPickupAddressLine(product.getPickupAddressLine());
        response.setPickupCity(product.getPickupCity());
        response.setPickupDistrict(product.getPickupDistrict());
        response.setPickupState(product.getPickupState());
        response.setPickupPincode(product.getPickupPincode());

        response.setStatus(product.getStatus());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        List<ProductImageResponse> imageResponses = new ArrayList<>();

        for(ProductImage image : product.getImages()){

            ProductImageResponse imageResponse = new ProductImageResponse();

            imageResponse.setImageUrl(image.getImageUrl());
            imageResponse.setImageType(image.getImageType());
            imageResponse.setDisplayOrder(image.getDisplayOrder());

            imageResponses.add(imageResponse);
        }

        response.setImages(imageResponses);

        return response;

    }
}
