package com.taskflow.vegiees.service;

import com.taskflow.vegiees.dto.CreateProductRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.taskflow.vegiees.dto.CloudinaryUploadResult;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class ProductListingServiceTest {

    @Mock
    private ImageValidationService imageValidationService;

    @Mock
    private ProductListingPersistenceService productListingPersistenceService;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private ProductListingService productListingService;

    @Test
    void shouldDeletePreviouslyUploadedImagesWhenUploadFails() throws IOException{

        MultipartFile image1 = new MockMultipartFile(
                "productImages", "image1.jpg" , "image/jpeg", new byte[]{1}
        );

        MultipartFile image2 = new MockMultipartFile(
                "productImages", "image2.jpg" , "image/jpeg", new byte[]{2}
        );

        MultipartFile image3 = new MockMultipartFile(
                "productImages", "image3.jpg" , "image/jpeg", new byte[]{3}
        );


        when(cloudinaryService.uploadImage(any(MultipartFile.class)))
                .thenReturn(
                        new CloudinaryUploadResult("url1", "public-id-1"),
                        new CloudinaryUploadResult("url2", "public-id-2")
                )
                .thenThrow(new IOException("Simulated Cloudinary Failure"));

        assertThrows(IOException.class, () ->
                productListingService.createListing(
                        new CreateProductRequest(),
                        List.of(image1, image2, image3),
                        null
                ));

        verify(cloudinaryService).deleteImage("public-id-1");
        verify(cloudinaryService).deleteImage("public-id-2");

        verify(cloudinaryService , times(3))
                .uploadImage(any(MultipartFile.class));

        verifyNoInteractions(productListingPersistenceService);
    }

    @Test
    void shouldDeleteAllUploadedImagesWhenDatabasePersistenceFails() throws IOException{

        MultipartFile image1 = new MockMultipartFile(
                "productImages", "image1.jpg", "image/jpeg", new byte[]{1}
        );

        MultipartFile image2 = new MockMultipartFile(
                "productImages", "image2.jpg", "image.jpeg", new byte[]{2}
        );

        MultipartFile growingSource = new MockMultipartFile(
                "growingSourceImage", "source.jpg", "image/jpeg", new byte[]{3}
        );

        when(cloudinaryService.uploadImage(any(MultipartFile.class)))
                .thenReturn(
                        new CloudinaryUploadResult("url1" , "public-id-1"),
                        new CloudinaryUploadResult("url2", "public-id-2"),
                        new CloudinaryUploadResult("url3", "public-id-3")
                );

        when(productListingPersistenceService.persistListing(
                any(CreateProductRequest.class),
                anyList(),
                any(CloudinaryUploadResult.class)
        )).thenThrow(new RuntimeException("Simulated Database Failure"));

        assertThrows(RuntimeException.class, () ->
                productListingService.createListing(
                        new CreateProductRequest(),
                        List.of(image1 , image2),
                        growingSource
                )
        );

        verify(cloudinaryService).deleteImage("public-id-1");
        verify(cloudinaryService).deleteImage("public-id-2");
        verify(cloudinaryService).deleteImage("public-id-3");

        verify(cloudinaryService, times(3))
                .uploadImage(any(MultipartFile.class));

    }
}
