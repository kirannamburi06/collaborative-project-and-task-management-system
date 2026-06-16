package com.kiran.collaborativeprojectandtaskmanagementsystem.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    private final CloudinaryProperties cloudinaryProperties;

    public CloudinaryConfig(CloudinaryProperties cloudinaryProperties) {
        this.cloudinaryProperties = cloudinaryProperties;
    }

    @Bean
    public Cloudinary cloudinary(){

        Map<String, String> config = Map.of(
                "cloud_name", cloudinaryProperties.getCloudName(),
                "api_key", cloudinaryProperties.getApiKey(),
                "api_secret", cloudinaryProperties.getApiSecret()
        );

        return new Cloudinary(config);
    }
}
