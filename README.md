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

### Refresh Token

- 처리 방식
  - access token 과 refresh token이 모두 유효할 때
    - 그대로 진행
  - access token이 유효하지 않고 refresh token이 유효할 때
    - access token 을 재발급하고 클라이언트에게 전달
  - access token이 유효하고 refresh token이 유효하지 않을 때
    - access token의 유출된 것일 수 있음.
    - 에러를 발생 시키고 다시 인증 과정을 거쳐야 토큰 발급.
  - access token과 refresh token이 모두 유효하지 않을 때
    - 에러 발생시킴.
    - 다시 인증 과정을 거쳐야 토큰 발급
- Refresh Token의 발급 시기
  - 인증 과정을 거쳤을 때 (로그인)
  - refresh token 의 유효기간이 얼마 남지 않았을 때.
- 재발급 방식
  - GetMapping 으로 재발급 api 만듬
  - refresh token이 유효하지만 access token이 만료시 서버단에서 토큰 만료 에러코드로 응답
  - 클라이언트 단에서 Interceptor를 사용해 토큰 만료 에러코드를 응답받으면 가로채서 재발급 api로 서버에 토큰 재발급 요청
  - 서버단에서 token 재발급 후 클라이언트에 전달

## 채팅

### 환경

- STOMP
- SockJs

### STOMP란?

메시지 전송을 효율적으로 하기 위한 프로토콜로 websocket 위에서 동작한다.

기본적으로 publish - subscribe 구조로 되어있으며, 소비하는 주체와 공급하는 주체를 분리해 제공한다. 따라서 메시지를 전송하는 부분과 받고 처리하는 부분이 확실하게 구조로 정해져있다.

### 플로우

- 서로 Like 하면 채팅방 자동으로 개설 (1 : 1 채팅)
- ChatRoom의 id로 채팅방을 구분, 해당 채팅방 id로 구독하는 유저에게 메시지 보냄.
  - 해당 채팅방에 있을 경우
    - 바로 읽음 처리
  - 해당 채팅방에 연결되어있지 않았을 때
    - unread상태로 디비에 저장
    - 구독 하기 전에 클라이언트가 unread messages 를 get으로 요청
    - DB에서 (unread && sender == 상대방) 인 메시지들을 리스트로 전달, 읽음처리

## **TODO**

- [x]  local, dev, prod 개발 환경 분리
- [x]  배포 자동화
- [x]  Spring Security 로 JWT 토큰 인증 구현
- [x]  JWT Refresh Token 구현
- [x]  채팅기능 구현
- [x]  파일 업로드 처리
- [ ]  oauth2 구현
- [ ]  Response 체계화
- [ ]  ExceptionHandler, ControllerAdvice 적용해서 예외처리