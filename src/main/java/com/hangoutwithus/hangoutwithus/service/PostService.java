package com.hangoutwithus.hangoutwithus.service;

import com.hangoutwithus.hangoutwithus.dto.PostDto;
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

    public PostDto post(Long memberId, PostDto postDto) {
        Post post = Post.builder()
                .image(postDto.getImage())
                .content(postDto.getContent())
                .locationX(postDto.getLocationX())
                .locationY(postDto.getLocationY())
                .areaName(postDto.getAreaName())
                .build();
        Member member = memberRepository.findById(memberId).orElseThrow();
        member.addPost(post);
        postRepository.save(post);
        return new PostDto(post);
    }

    public PostDto update(Long postId, PostDto postDto) {
        Post post = postRepository.findById(postId).orElseThrow();

        String image = postDto.getImage() == null ? post.getImage() : postDto.getImage();
        String content = postDto.getContent() == null ? post.getContent() : postDto.getContent();
        Integer locationX = postDto.getLocationX() == null ? post.getLocationX() : postDto.getLocationX();
        Integer locationY = postDto.getLocationY() == null ? post.getLocationY() : postDto.getLocationY();
        String areaName = postDto.getAreaName() == null ? post.getAreaName() : postDto.getAreaName();

        post.updatePost(image, content, locationX, locationY, areaName);

        return new PostDto(post);
    }

    public void delete(Long postId) {
        memberRepository.deleteById(postId);
    }
}