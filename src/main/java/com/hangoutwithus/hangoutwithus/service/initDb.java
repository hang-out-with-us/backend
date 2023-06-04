package com.hangoutwithus.hangoutwithus.service;

import com.hangoutwithus.hangoutwithus.entity.*;
import com.hangoutwithus.hangoutwithus.repository.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Random;

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
        private final ChatRoomRepository chatRoomRepository;
        private final ChatRoomInfoRepository chatRoomInfoRepository;

        private final ImageRepository imageRepository;
        private final GeolocationRepository geolocationRepository;

        InitService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, PostRepository postRepository, ChatRoomRepository chatRoomRepository, ChatRoomInfoRepository chatRoomInfoRepository, ImageRepository imageRepository, GeolocationRepository geolocationRepository) {
            this.memberRepository = memberRepository;
            this.passwordEncoder = passwordEncoder;
            this.postRepository = postRepository;
            this.chatRoomRepository = chatRoomRepository;
            this.chatRoomInfoRepository = chatRoomInfoRepository;
            this.imageRepository = imageRepository;
            this.geolocationRepository = geolocationRepository;
        }

        public void dbInit1() {
            for (int i = 0; i < 50; i++) {
                Member member = Member.builder()
                        .name("Name" + i)
                        .password(passwordEncoder.encode("123123"))
                        .role(Role.valueOf("ROLE_USER"))
                        .age((int) (Math.random() * 10) + 20)
                        .email("email" + i + "@example.com")
                        .isCompletedSignup(true)
                        .build();
                memberRepository.save(member);
                Post post = Post.builder()
                        .member(member)
                        .content("내용" + i)
                        .build();
                postRepository.save(post);
                Geolocation geolocation = Geolocation.builder()
                        .latitude(randomDouble())
                        .longitude(randomDouble())
                        .member(member)
                        .build();
                geolocationRepository.save(geolocation);
                for (int j = 0; j < 3; j++) {
                    Image image = Image.builder()
                            .name(i+"-"+j + ".jpg")
                            .post(post)
                            .member(member)
                            .build();
                    imageRepository.save(image);
                }
            }
            ChatRoom chatRoom = new ChatRoom();
            chatRoomRepository.save(chatRoom);
            ChatRoomInfo chatRoomInfo1 = ChatRoomInfo.builder()
                    .chatRoom(chatRoom)
                    .member(memberRepository.findById(1L).orElseThrow())
                    .build();
            ChatRoomInfo chatRoomInfo2 = ChatRoomInfo.builder()
                            .chatRoom(chatRoom)
                    .member(memberRepository.findById(2L).orElseThrow())
                    .build();

            chatRoomInfoRepository.save(chatRoomInfo1);
            chatRoomInfoRepository.save(chatRoomInfo2);
        }
    }
    public static Double randomDouble() {
        Random random = new Random();
        double min = -180.0;
        double max = 180.0;
        int precision = 6;

        double randomValue = min + (max - min) * random.nextDouble();
        return Math.round(randomValue * Math.pow(10, precision)) / Math.pow(10, precision);
    }
}
