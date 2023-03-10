package com.hangoutwithus.hangoutwithus.service;

import com.hangoutwithus.hangoutwithus.dto.PostRequest;
import com.hangoutwithus.hangoutwithus.dto.PostResponse;
import com.hangoutwithus.hangoutwithus.entity.Member;
import com.hangoutwithus.hangoutwithus.entity.Post;
import com.hangoutwithus.hangoutwithus.repository.MemberRepository;
import com.hangoutwithus.hangoutwithus.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public PostService(PostRepository postRepository, MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    public PostResponse post(Principal principal, PostRequest postRequest) {
        Post post = Post.builder()
                .image(postRequest.getImage())
                .content(postRequest.getContent())
                .locationX(postRequest.getLocationX())
                .locationY(postRequest.getLocationY())
                .areaName(postRequest.getAreaName())
                .build();
        Member member = memberRepository.findMemberByEmail(principal.getName()).orElseThrow();
        member.addPost(post);
        post = postRepository.save(post);
        return new PostResponse(post);
    }

    public PostResponse update(Principal principal, PostRequest postRequest) {

        Long postId = memberRepository.findMemberByEmail(principal.getName()).orElseThrow().getPost().getId();
        Post post = postRepository.findById(postId).orElseThrow();

        String image = postRequest.getImage() == null ? post.getImage() : postRequest.getImage();
        String content = postRequest.getContent() == null ? post.getContent() : postRequest.getContent();
        Integer locationX = postRequest.getLocationX() == null ? post.getLocationX() : postRequest.getLocationX();
        Integer locationY = postRequest.getLocationY() == null ? post.getLocationY() : postRequest.getLocationY();
        String areaName = postRequest.getAreaName() == null ? post.getAreaName() : postRequest.getAreaName();

        post.updatePost(image, content, locationX, locationY, areaName);

        return new PostResponse(post);
    }

    public void delete(Principal principal) {
        Long postId = memberRepository.findMemberByEmail(principal.getName()).orElseThrow().getPost().getId();
        postRepository.deleteById(postId);
    }

    public PostResponse findById(Long postId) {
        return new PostResponse(postRepository.findById(postId).orElseThrow());
    }
}