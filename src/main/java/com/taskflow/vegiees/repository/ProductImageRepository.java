package com.taskflow.vegiees.repository;

import com.taskflow.vegiees.entity.ImageType;
import com.taskflow.vegiees.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProductIdOrderByDisplayOrderAsc(Long productId);

    List<ProductImage> findByProductId(Long productId);

    long countByProductIdAndImageType(Long productId, ImageType imageType);
}
