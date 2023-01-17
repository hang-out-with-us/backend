package com.hangoutwithus.hangoutwithus.controller;

import com.hangoutwithus.hangoutwithus.dto.PostRequest;
import com.hangoutwithus.hangoutwithus.dto.PostResponse;
import com.hangoutwithus.hangoutwithus.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@Api
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/{memberId}")
    @ApiOperation(value = "글 작성")
    public PostResponse post(@PathVariable Long memberId, @RequestBody PostRequest postRequest) {
        return postService.post(memberId, postRequest);
    }

    @GetMapping("/{postId}")
    @ApiOperation(value = "글 가져오기")
    public PostResponse findById(@PathVariable Long postId) {
        return postService.findById(postId);
    }

    @PutMapping("/{postId}")
    @ApiOperation(value = "글 수정")
    public PostResponse update(@PathVariable Long postId, @RequestBody PostRequest postRequest) {
        return postService.update(postId, postRequest);
    }

    @DeleteMapping("/{postId}")
    @ApiOperation(value = "글 삭제")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }
}