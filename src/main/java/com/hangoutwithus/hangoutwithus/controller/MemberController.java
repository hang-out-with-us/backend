package com.hangoutwithus.hangoutwithus.controller;

import com.hangoutwithus.hangoutwithus.dto.MemberRequest;
import com.hangoutwithus.hangoutwithus.dto.MemberResponse;
import com.hangoutwithus.hangoutwithus.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@Api
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "회원 정보 조회")
    public MemberResponse findOne(@PathVariable Long id) {
        return memberService.findOne(id);
    }

    @PutMapping("/")
    public MemberResponse update(Principal principal, @RequestBody MemberRequest memberRequest) {
        return memberService.update(principal, memberRequest);
    }

    @DeleteMapping("/")
    @ApiOperation("회원 탈퇴")
    public void delete(Principal principal) {
        memberService.delete(principal);
    }

    @PostMapping("/like/{id}")
    @ApiOperation("좋아요")
    public void like(Principal principal, @PathVariable Long id) {
        memberService.like(principal, id);
    }

    @GetMapping("/like/list-who-like-me")
    @ApiOperation("나를 좋아요 하는 사람들")
    public List<MemberResponse> listWhoLikeMe(Principal principal) {
        return memberService.listWhoLikeMe(principal);
    }
}
