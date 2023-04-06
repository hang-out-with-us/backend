package com.hangoutwithus.hangoutwithus.controller;

import com.hangoutwithus.hangoutwithus.service.ImageService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@RestController
@Api
@RequestMapping("/image")
public class ImageController {
    final private ImageService imageService;
    @Value("${file.path}")
    String path;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadImage(@PathVariable("fileName") String filename) throws IOException {
        Resource resource = imageService.getImage(filename);
        String contentType = Files.probeContentType(Path.of(path + filename));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}