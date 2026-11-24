package com.taskflow.vegiees.controller;

import com.taskflow.vegiees.dto.CreateProductRequest;
import com.taskflow.vegiees.dto.ProductResponse;
import com.taskflow.vegiees.entity.Product;
import com.taskflow.vegiees.service.ProductListingService;
import com.taskflow.vegiees.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class ProductController {

    private final ProductListingService productListingService;

    public ProductController(ProductListingService productListingService) {
        this.productListingService = productListingService;
    }

    @PostMapping(
            value = "/api/products",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestPart("product")
            CreateProductRequest request,

            @RequestPart("productImages")
            List<MultipartFile> productImages,

            @RequestPart(value = "growingSourceImage" , required = false)
            MultipartFile growingSourceImage
    ) throws IOException {
//        System.out.println("CREATE PRODUCT CONTROLLER REACHED");
        ProductResponse response = productListingService.createListing(request, productImages,
                growingSourceImage);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
