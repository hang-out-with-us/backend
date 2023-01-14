package com.hangoutwithus.hangoutwithus.controller;

import com.hangoutwithus.hangoutwithus.dto.PostRequest;
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
    public PostRequest post(@PathVariable Long memberId, @RequestBody PostRequest postRequest) {
        return postService.post(memberId, postRequest);
    }

    @PutMapping("/{postId}")
    public PostRequest update(@PathVariable Long postId, @RequestBody PostRequest postRequest) {
        return postService.update(postId, postRequest);
    }

    @DeleteMapping("/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }
}