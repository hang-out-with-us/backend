package com.hangoutwithus.hangoutwithus.controller;

import com.hangoutwithus.hangoutwithus.dto.MemberRequest;
import com.hangoutwithus.hangoutwithus.dto.MemberResponse;
import com.hangoutwithus.hangoutwithus.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@Api
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/")
    @ApiOperation(value = "회원가입")
    public MemberResponse signup(@RequestBody MemberRequest memberRequest) {
        return memberService.signup(memberRequest);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "회원 정보 조회")
    public MemberResponse findOne(@PathVariable Long id) {
        return memberService.findOne(id);
    }

    @PutMapping("/{id}")
    public MemberResponse update(@PathVariable Long id, @RequestBody MemberRequest memberRequest) {
        return memberService.update(id, memberRequest);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("회원 탈퇴")
    public void delete(@PathVariable Long id) {
        memberService.delete(id);
    }
}
