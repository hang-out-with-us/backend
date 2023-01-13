package com.hangoutwithus.hangoutwithus.controller;

import com.hangoutwithus.hangoutwithus.dto.MemberBaseDto;
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
    public MemberBaseDto create(@RequestBody MemberBaseDto memberbaseDto) {
        System.out.println(memberbaseDto.getName());
        return memberService.create(memberbaseDto);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "회원 정보 조회")
    public MemberBaseDto findOne(@PathVariable Long id) {
        return memberService.findOne(id);
    }

    @PutMapping("/{id}")
    public MemberBaseDto update(@PathVariable Long id, @RequestBody MemberBaseDto memberBaseDto) {
        return memberService.update(id, memberBaseDto);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("회원 탈퇴")
    public void delete(@PathVariable Long id) {
        memberService.delete(id);
    }
}
