package com.hangoutwithus.hangoutwithus.service;

import com.hangoutwithus.hangoutwithus.dto.PostRequest;
import com.hangoutwithus.hangoutwithus.dto.PostResponse;
import com.hangoutwithus.hangoutwithus.entity.Image;
import com.hangoutwithus.hangoutwithus.entity.Member;
import com.hangoutwithus.hangoutwithus.entity.Post;
import com.hangoutwithus.hangoutwithus.repository.ImageRepository;
import com.hangoutwithus.hangoutwithus.repository.MemberRepository;
import com.hangoutwithus.hangoutwithus.repository.PostRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;

    @Value("${file.path}")
    String path;

    public PostService(PostRepository postRepository, MemberRepository memberRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.imageRepository = imageRepository;
    }

    @Transactional
    public PostResponse post(Principal principal, PostRequest postRequest) {
        if (postRepository.findAllByMemberId(memberRepository.findMemberByEmail(principal.getName()).orElseThrow().getId()).size() > 0) {
            throw new IllegalStateException("이미 작성한 글이 있습니다.");
        }
        Member member = memberRepository.findMemberByEmail(principal.getName()).orElseThrow();
        Post post = Post.builder()
                .content(postRequest.getContent())
                .member(member)
                .build();
        List<Image> images = new ArrayList<>();
        member.addPost(post);

        PostResponse postResponse = new PostResponse(post);
        for (MultipartFile file : postRequest.getImages()) {
            Image image = uploadImage(file, principal, post);
            postResponse.addFilename(image.getName());
        }

        return postResponse;
    }


    public void delete(Principal principal) {
        Long postId = memberRepository.findMemberByEmail(principal.getName()).orElseThrow().getPost().getId();
        postRepository.deleteById(postId);
    }

    public PostResponse findById(Long postId) {
        return new PostResponse(postRepository.findById(postId).orElseThrow());
    }

    public Image uploadImage(MultipartFile file, Principal principal, Post post) {
        int pos = file.getOriginalFilename().lastIndexOf(".");
        String ext = file.getOriginalFilename().substring(pos);
        String fileName = principal.getName() + UUID.randomUUID() + ext;
        String filePath = path + fileName;

        Image image = Image.builder()
                .name(fileName)
                .member(memberRepository.findMemberByEmail(principal.getName()).orElseThrow())
                .post(post)
                .build();
        try {
            Files.write(Path.of(filePath), file.getBytes());
        } catch (Exception e) {
        }
        return imageRepository.save(image);
    }
}