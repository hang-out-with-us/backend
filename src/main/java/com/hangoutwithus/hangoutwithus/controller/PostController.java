package com.hangoutwithus.hangoutwithus.controller;

import com.hangoutwithus.hangoutwithus.dto.PostRequest;
import com.hangoutwithus.hangoutwithus.dto.PostResponse;
import com.hangoutwithus.hangoutwithus.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@Api
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(value = "/")
    @ApiOperation(value = "글 작성")
    public PostResponse post(Principal principal, @RequestPart("files") List<MultipartFile> files, @RequestPart("data") PostRequest postRequest) {
        postRequest.getImages().addAll(files);
        System.out.println(postRequest.getImages().toString());
        return postService.post(principal, postRequest);
    }

    @GetMapping("/{postId}")
    @ApiOperation(value = "글 가져오기")
    public PostResponse findById(@PathVariable Long postId) {
        return postService.findById(postId);
    }

    @DeleteMapping("/")
    @ApiOperation(value = "글 삭제")
    public void delete(Principal principal) {
        postService.delete(principal);
    }
}