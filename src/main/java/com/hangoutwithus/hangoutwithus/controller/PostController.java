package com.hangoutwithus.hangoutwithus.controller;

import com.hangoutwithus.hangoutwithus.dto.PostRequest;
import com.hangoutwithus.hangoutwithus.dto.PostResponse;
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
    public PostResponse post(@PathVariable Long memberId, @RequestBody PostRequest postRequest) {
        return postService.post(memberId, postRequest);
    }

    @GetMapping("/{postId}")
    public PostResponse findById(@PathVariable Long postId) {
        return postService.findById(postId);
    }

    @PutMapping("/{postId}")
    public PostResponse update(@PathVariable Long postId, @RequestBody PostRequest postRequest) {
        return postService.update(postId, postRequest);
    }

    @DeleteMapping("/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }
}