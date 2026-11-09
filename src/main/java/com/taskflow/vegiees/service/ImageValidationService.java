package com.taskflow.vegiees.service;

import com.taskflow.vegiees.exception.InvalidImageException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Service
public class ImageValidationService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    private static final Set<String> ALLOWED_FILE_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    public static void validate(MultipartFile file){

        if(file == null || file.isEmpty()){
            throw new InvalidImageException("Image file must not be empty");
        }

        if(file.getSize() > MAX_FILE_SIZE){
            throw new InvalidImageException("Image size must not exceed 5 MB");
        }

        String contentType = file.getContentType();

        if(contentType == null || !ALLOWED_FILE_TYPES.contains(contentType)){
            throw new InvalidImageException("Only JPEG , PNG and WebP images are allowed");
        }
    }
}
