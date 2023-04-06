package com.hangoutwithus.hangoutwithus.repository;


import com.hangoutwithus.hangoutwithus.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, String> {
}
