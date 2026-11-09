package com.taskflow.vegiees.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.taskflow.vegiees.dto.CloudinaryUploadResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary){
        this.cloudinary = cloudinary;
    }

    public CloudinaryUploadResult uploadImage(MultipartFile file) throws IOException{

        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes() ,
                ObjectUtils.emptyMap()
        );

        String imageUrl = (String) uploadResult.get("secure_url");
        String publicId = (String) uploadResult.get("public_id");

        return new CloudinaryUploadResult(imageUrl , publicId);

    }

    public void deleteImage(String publicId) throws IOException {

        cloudinary.uploader().destroy(
                publicId ,
                ObjectUtils.emptyMap()
        );
    }
}
