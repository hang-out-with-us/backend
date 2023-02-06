package com.hangoutwithus.hangoutwithus.service;

import com.hangoutwithus.hangoutwithus.entity.Member;
import com.hangoutwithus.hangoutwithus.entity.Post;
import com.hangoutwithus.hangoutwithus.entity.Role;
import com.hangoutwithus.hangoutwithus.repository.MemberRepository;
import com.hangoutwithus.hangoutwithus.repository.PostRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Component
public class initDb {

    private final InitService initService;


    public initDb(InitService initService) {
        this.initService = initService;
    }

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }


    @Component
    @Transactional
    static class InitService {
        private final MemberRepository memberRepository;
        private final PasswordEncoder passwordEncoder;
        private final PostRepository postRepository;

        InitService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, PostRepository postRepository) {
            this.memberRepository = memberRepository;
            this.passwordEncoder = passwordEncoder;
            this.postRepository = postRepository;
        }

        public void dbInit1() {
            for (int i = 0; i < 300; i++) {
                Member member = Member.builder()
                        .name("Name" + i)
                        .password(passwordEncoder.encode("123123"))
                        .role(Role.valueOf("ROLE_USER"))
                        .age((int) (Math.random() * 10) + 20)
                        .email("email" + i + "@example.com")
                        .build();
                memberRepository.save(member);
                Post post = Post.builder()
                        .member(member)
                        .content("내용" + i)
                        .locationX((int) (Math.random() * 100))
                        .locationY((int) (Math.random() * 100))
                        .build();
                postRepository.save(post);
            }
        }
    }
}
