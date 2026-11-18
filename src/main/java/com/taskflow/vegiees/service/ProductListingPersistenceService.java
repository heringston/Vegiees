package com.taskflow.vegiees.service;

import com.taskflow.vegiees.dto.CloudinaryUploadResult;
import com.taskflow.vegiees.dto.CreateProductRequest;
import com.taskflow.vegiees.entity.ImageType;
import com.taskflow.vegiees.entity.Product;
import com.taskflow.vegiees.entity.ProductImage;
import com.taskflow.vegiees.repository.ProductImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductListingPersistenceService {

    private final ProductService productService;
    private final ProductImageService productImageService;
    private final ProductImageRepository productImageRepository;

    public ProductListingPersistenceService(ProductService productService,
                                            ProductImageService productImageService,
                                            ProductImageRepository productImageRepository) {
        this.productService = productService;
        this.productImageService = productImageService;
        this.productImageRepository = productImageRepository;
    }

    @Transactional
    public Product persistListing(
            CreateProductRequest request,
            List<CloudinaryUploadResult> productImageUploads,
            CloudinaryUploadResult growingSourceUpload
    ){

        Product product = productService.createProduct(request);

        List<ProductImage> images = new ArrayList<>();

        for(int i = 0;i < productImageUploads.size(); i++){
            ProductImage productImage = productImageService.createProductImage(product,
                    productImageUploads.get(i), ImageType.PRODUCT, i);

            product.addImage(productImage);
            images.add(productImage);
        }

        if(growingSourceUpload != null){

            ProductImage sourceImage = productImageService.createProductImage(product,
                    growingSourceUpload, ImageType.GROWING_SOURCE, 0);

            product.addImage(sourceImage);
            images.add(sourceImage);
        }

        productImageRepository.saveAll(images);

        return product;


    }
}
