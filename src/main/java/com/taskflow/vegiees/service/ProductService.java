package com.taskflow.vegiees.service;

import com.taskflow.vegiees.dto.CreateProductRequest;
import com.taskflow.vegiees.dto.ProductResponse;
import com.taskflow.vegiees.entity.Product;
import com.taskflow.vegiees.entity.User;
import com.taskflow.vegiees.repository.ProductRepository;
import com.taskflow.vegiees.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public ProductService(ProductRepository productRepository, UserRepository userRepository, CurrentUserService currentUserService) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }


    public ProductResponse createProduct(CreateProductRequest request){

        User authenticatedUser = currentUserService.getAuthenticatedUser();

        User seller  = userRepository.getReferenceById(
                authenticatedUser.getId()
        );

        Product product = new Product();

        product.setSeller(seller);
        product.setTitle(request.getTitle());
        product.setCategory(request.getCategory());
        product.setDescription(request.getDescription());

        product.setQuantity(request.getQuantity());
        product.setQuantityUnit(request.getQuantityUnit());

        product.setPrice(request.getPrice());
        product.setPriceUnit(request.getPriceUnit());

        product.setHarvestDate(request.getHarvestDate());

        product.setPickupAddressLine(request.getPickupAddressLine());
        product.setPickupCity(request.getPickupCity());
        product.setPickupDistrict(request.getPickupDistrict());
        product.setPickupState(request.getPickupState());
        product.setPickupPincode(request.getPickupPincode());

        if (request.isSaveAddressToProfile()) {
            seller.setAddressLine(request.getPickupAddressLine());
            seller.setCity(request.getPickupCity());
            seller.setDistrict(request.getPickupDistrict());
            seller.setState(request.getPickupState());
            seller.setPincode(request.getPickupPincode());
        }

        Product savedProduct = productRepository.save(product);

        return mapToResponse(savedProduct);
    }

    private ProductResponse mapToResponse(Product product){

        ProductResponse response  = new ProductResponse();

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

        return response;
    }
}
