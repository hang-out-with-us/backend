# 몇명이서 오셨어요

## 컨셉

번화가에서 다른 사람들과 같이 만나서 놀고 싶을 때 실시간으로 매칭시켜주는 앱.

## 환경

- DB
    - MariaDB (dev, prod)
    - h2 (local)
- BackEnd
    - JAVA
    - Spring Boot
    - Spring Data JPA
- 툴
    - Intellij
    - Swagger

## 기능 목록

- 회원 기능
    - 회원 가입
        - 이메일, 비밀번호
    - 로그인
    - 회원 탈퇴
    - 소셜 로그인 (추후 추가.)
        - 애플
        - 네이버
        - 카카오
        - etc..
        - 참고 문서
            - [https://deeplify.dev/back-end/spring/oauth2-social-login](https://deeplify.dev/back-end/spring/oauth2-social-login)
- 게시글
    - 등록
    - 수정
        - 이미지 업로드
    - 삭제
- 추천 기능
    - 가까운 지역에서 인원 수와 관심사가 맞는 사람을 추천
- 매칭 기능
    - 나를 좋아요 한 사람 리스트
    - 서로 좋아요 한 회원끼리 채팅방 개설

## 도메인 설계

![domain model](https://i.ibb.co/18yfdLR/Screenshot-2022-12-29-at-3-52-06-PM.png)

### 엔티티

- Member
    - id : Long
    - name : String
    - email : String
    - password : String
    - age : Integer
    - post : Post
    - membersWhoLikeMe : List<Member>
    - membersILike : List<Member>
    - chatRoomList : List<ChatRoom>
    - Role : role
    - createdDate : LocalDateTime
    - updatedDate : LocalDateTime
- MemberLike
    - id : Long
    - likeFrom : Member
    - likeTo : Member
- Board
    - number_of_person : Integer
    - image : String
    - content : String
    - locationX : Integer
    - locationY : Integer
    - areaName : String
    - createdDate : LocalDateTime
    - updatedDate : LocalDateTime
- ChatRoom
    - id : Long
    - createdDate : LocalDateTime
    - updatedDate : LocalDateTime
- ChatRoomInfo
    - id : Long
    - member : Member
    - chatRoom : ChatRoom
- Message
    - id : Long
    - messageFrom : Member
    - chatRoom : ChatRoom
    - content : String
    - createdDate : LocalDateTime

### ERD

![ERD](https://i.ibb.co/LYJhNrD/Screenshot-2022-12-29-at-3-43-39-PM.png)

## CI/CD

### 환경

- Github Actions
- AWS
    - EC2
    - S3
    - IAM
    - CodeDeploy

### Flow

![CI/CD](https://i.ibb.co/hsnmgcj/Screenshot-2023-01-28-at-9-52-26-PM.png)

## Spring Security JWT Authentication

[블로그 정리 글](https://bottlemoon.me/spring-spring-security%eb%a1%9c-jwt%ed%86%a0%ed%81%b0-%ec%9d%b8%ec%a6%9d-%ea%b5%ac%ed%98%84/)

## TODO

- [x] local, dev, prod 개발 환경 분리
- [x] 배포 자동화
- [x] Spring Security 로 JWT 토큰 인증 구현
- [ ] JWT Refresh Token 구현
- [ ] Response 체계화
- [ ] 채팅기능 구현
- [ ] 파일 업로드 처리
- [ ] ExceptionHandler, ControllerAdvice 적용해서 예외처리