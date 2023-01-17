package com.hangoutwithus.hangoutwithus.service;

import com.hangoutwithus.hangoutwithus.dto.PostRequest;
import com.hangoutwithus.hangoutwithus.dto.PostResponse;
import com.hangoutwithus.hangoutwithus.entity.Member;
import com.hangoutwithus.hangoutwithus.entity.Post;
import com.hangoutwithus.hangoutwithus.repository.MemberRepository;
import com.hangoutwithus.hangoutwithus.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public PostService(PostRepository postRepository, MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    public PostResponse post(Long memberId, PostRequest postRequest) {
        Post post = Post.builder()
                .image(postRequest.getImage())
                .content(postRequest.getContent())
                .locationX(postRequest.getLocationX())
                .locationY(postRequest.getLocationY())
                .areaName(postRequest.getAreaName())
                .build();
        Member member = memberRepository.findById(memberId).orElseThrow();
        member.addPost(post);
        post = postRepository.save(post);
        return new PostResponse(post);
    }

    public PostResponse update(Long postId, PostRequest postRequest) {
        Post post = postRepository.findById(postId).orElseThrow();

        String image = postRequest.getImage() == null ? post.getImage() : postRequest.getImage();
        String content = postRequest.getContent() == null ? post.getContent() : postRequest.getContent();
        Integer locationX = postRequest.getLocationX() == null ? post.getLocationX() : postRequest.getLocationX();
        Integer locationY = postRequest.getLocationY() == null ? post.getLocationY() : postRequest.getLocationY();
        String areaName = postRequest.getAreaName() == null ? post.getAreaName() : postRequest.getAreaName();

        post.updatePost(image, content, locationX, locationY, areaName);

        return new PostResponse(post);
    }

    public void delete(Long postId) {
        memberRepository.deleteById(postId);
    }

    public PostResponse findById(Long postId) {
        return new PostResponse(postRepository.findById(postId).orElseThrow());
    }
}