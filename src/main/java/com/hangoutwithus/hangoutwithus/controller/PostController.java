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
    public void post(@PathVariable Long memberId, @RequestBody PostDto postDto) {
        postService.post(memberId, postDto);
    }

    @PutMapping("/{postId}")
    public void update(@PathVariable Long postId, @RequestBody PostDto postDto) {
        postService.update(postId, postDto);
    }
}