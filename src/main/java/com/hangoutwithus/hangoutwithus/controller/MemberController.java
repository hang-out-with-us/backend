package com.hangoutwithus.hangoutwithus.controller;

import com.hangoutwithus.hangoutwithus.dto.GeolocationDto;
import com.hangoutwithus.hangoutwithus.dto.MemberRecommendResponse;
import com.hangoutwithus.hangoutwithus.dto.MemberRequest;
import com.hangoutwithus.hangoutwithus.dto.MemberResponse;
import com.hangoutwithus.hangoutwithus.entity.Geolocation;
import com.hangoutwithus.hangoutwithus.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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

    @GetMapping("/recommend")
    @ApiOperation(value = "회원 추천", notes = "밑에 있는 parameter들 잘못 나온 거고 page, size 만 설정하면 됨." +
            "<br>page는 몇 번째 페이지인지, size는 페이지당 몇 개의 데이터 가져올지" +
            "<br>ex)http://{path}/member/recommend?page=0&size=15")

    public Slice<MemberRecommendResponse> recommend(Principal principal, Pageable pageable) {
        return memberService.recommend(principal, pageable);
    }

    @GetMapping("/logout")
    @ApiOperation(value = "로그아웃")
    public void logout(Principal principal) {
        memberService.logout(principal);
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

    //Geolocation
    @PostMapping("/geolocation")
    @ApiOperation("위치 정보 등록")
    public void geolocation(Principal principal, @RequestBody GeolocationDto geolocationDto) {
        memberService.geolocation(principal, geolocationDto);
    }
}
