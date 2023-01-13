package com.hangoutwithus.hangoutwithus.controller;

import com.hangoutwithus.hangoutwithus.dto.PostDto;
import com.hangoutwithus.hangoutwithus.service.PostService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/{memberId}")
    public PostDto post(@PathVariable Long memberId, @RequestBody PostDto postDto) {
        return postService.post(memberId, postDto);
    }

    @PutMapping("/{postId}")
    public PostDto update(@PathVariable Long postId, @RequestBody PostDto postDto) {
        return postService.update(postId, postDto);
    }

    @DeleteMapping("/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }
}