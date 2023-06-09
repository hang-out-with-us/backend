package com.hangoutwithus.hangoutwithus.controller;

import com.hangoutwithus.hangoutwithus.dto.*;
import com.hangoutwithus.hangoutwithus.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Api
public class AuthController {

    private final MemberService memberService;

    public AuthController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/authenticate")
    @ApiOperation(value = "로그인")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto) {
        return memberService.authorize(loginDto);
    }

    @PostMapping("/signup")
    @ApiOperation(value = "회원가입")
    public MemberResponse signup(@RequestBody MemberRequest memberRequest) {
        return memberService.signup(memberRequest);
    }

    @GetMapping("/refresh")
    @ApiOperation(value = "토큰 재발급")
    public ResponseEntity<TokenDto> refresh(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return memberService.refresh(refreshToken);
    }
}
