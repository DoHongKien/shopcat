package com.assignment;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String productImage = "product-images";
        Path path = Paths.get(productImage);
        String abolutePath = path.toFile().getAbsolutePath();

        String localPath = productImage + "/**";
        registry.addResourceHandler(localPath).addResourceLocations("file:/" + abolutePath + "/");
    }

}
