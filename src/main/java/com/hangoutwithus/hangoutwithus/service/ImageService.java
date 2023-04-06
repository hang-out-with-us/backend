package com.hangoutwithus.hangoutwithus.service;

import com.hangoutwithus.hangoutwithus.repository.ImageRepository;
import com.hangoutwithus.hangoutwithus.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class ImageService {
    final private ImageRepository imageRepository;
    final private MemberRepository memberRepository;
    @Value("${file.path}")
    String path;

    public ImageService(ImageRepository imageRepository, MemberRepository memberRepository) {
        this.imageRepository = imageRepository;
        this.memberRepository = memberRepository;
    }

    public Resource getImage(String filename) throws IOException {
        return new InputStreamResource(Files.newInputStream(Path.of(path + filename)));
    }
}