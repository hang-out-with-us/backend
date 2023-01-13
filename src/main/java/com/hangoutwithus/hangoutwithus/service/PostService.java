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

    public void post(Long memberId, PostDto postDto) {
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
    }

}