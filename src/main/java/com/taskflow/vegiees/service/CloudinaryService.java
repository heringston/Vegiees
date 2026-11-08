package com.taskflow.vegiees.service;

import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary){
        this.cloudinary = cloudinary;
    }
}
