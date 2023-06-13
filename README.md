# HangOutWithUs

## 기획의도

번화가에서 다른 사람들과 같이 만나서 놀고 싶을 때 실시간으로 매칭 시켜주는 앱 입니다. Sprint boot, Spring Data JPA, JWT, OAuth2, CI/CD 를 학습하고 적용해보기 위한
프로젝트입니다. 스토어에 런칭하여 실사용자가 있는 서비스를 만드는 것이 목표입니다.

## 개발환경 및 Stack

- DB
    - MariaDB (dev, prod)
    - h2 (local)
- BackEnd
    - JAVA
    - Spring Boot
    - Spring Data JPA
    - Spring Security
        - Oauth2
- 툴
    - Intellij
    - Swagger
- CI/CD
    - Github Actions
    - AWS
        - EC2
        - S3
        - IAM
        - CodeDeploy

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
            - https://deeplify.dev/back-end/spring/oauth2-social-login
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

## 서비스 세부 설명

## **Spring Security**

[블로그 정리 글](https://bottlemoon.me/spring-spring-security%eb%a1%9c-jwt%ed%86%a0%ed%81%b0-%ec%9d%b8%ec%a6%9d-%ea%b5%ac%ed%98%84/)

### JWT

[[Spring] JWT, Refresh Token - BottleMoon](https://bottlemoon.me/spring-jwt-refresh-token/)

해당 프로젝트는 모바일 앱의 백엔드라서 Session, Cookie를 사용하지 않고 JWT Token 방식을 사용 했습니다.

### Refresh Token

**Refresh Token이란?**

token 인증 방식의 단점을 보안하기 위해 token의 유효기간을 짧게 가져가는 방법이 있는데, 이 방식을 사용하면 token이 만료될 때 마다 다시 인증과정을 거쳐야하는 단점이 생긴다. 이 단점을 보안하기 위해
사용하는 방식이 Refresh Token이다.

로그인 시 access token과 Refresh Token을 같이 발급한다. 이때 서버는 Refresh Token의 유효기간을 길게 가져가는데, 만약 access Token이 만료되면 RefreshToken을
사용해 AccessToken을 재발급 요청한다. 이때 사용자는 재발급 과정을 알 수 없으며, 로그인을 다시 할 필요가 없어진다.

서버는 Refresh Token을 DB에 저장한다(Stateful). 만약 토큰이 탈취당했다고 의심이 들거나, 보안상 문제가 생기면 DB의 Refresh Token을 삭제해서 해당 탈취당한 Access Token과
Refresh Token을 통해 인증을 하지 못하게 한다. → Stateless 인증방식의 단점을 보완

이 때 refresh token을 사용하면 stateless의 장점이 결국 없어지는 것 아닌지, session과 다를 바 없지 않냐고 의구심이 들 수 있는데 Refresh Token에 접근하는 경우는 로그인,
로그아웃, Access Token의 재발급 시점 정도로 서비스를 이용하기 위해 수시로 session에 접근해야하는 것 보다 빈도수가 현저히 낮다. 또한 Redis와 같은 인메모리 데이터베이스를 사용해서 DB 접근으로
인한 성능상 문제를 개선할 수 있다 (현재 프로젝트에선 적용하지 않음)

~~현재 프로젝트에서 API요청시 마다 JWT와 Refresh Token을 함께 전송해 매번 Refresh Token을 검증했었는데, 이 방식은 Session을 사용하는 것과 다를 바 없어서 Access
Token만을 이용해 요청을 보내는 방식으로 수정했습니다.~~

### 플로우

- 로그인
    - Access Token, Refresh Token 발급 이 때 Refresh Token은 DB에 저장
- API 요청
    - Header에 Access Token을 같이 담아서 요청
    - Access Token이 유효할 때
        - 그대로 진행
    - Access Token이 만료되었을 때
        - Refresh Token을 담아서 재발급 요청
            - Refresh Token이 유효할 때
                - Access Token 재발급
            - Refresh Token이 유효하지 않을 때
                - 로그아웃 처리
            - Refresh Token이 유효하지만, 유효기간이 얼마 남지 않았을 때
                - Access Token, Refresh Token 모두 재발급
- 로그아웃
    - DB에 있는 Refresh Token 삭제
- 보안상 문제가 생겼다고 판단 시
    - DB에 있는 Refresh Token 삭제

## OAuth2 소셜 로그인

### 용어

- Resource Owner
    - 자신의 정보에 접근할 수 있는 주체
- Client
    - Resource Owner 의 정보를 사용하고자 접근 요청을 하는 애플리케이션
    - 우리가 평상시에 쓰는 용어인 프론트, 백엔드와 헷갈려서 프론트엔드만을 생각할 수 있는데 여기서는 프론트엔드, 백엔드 상관 없이 Resource Owner의 정보에 접근하려고 하는 애플리케이션을 말한다.
    - 이 프로젝트에서는 현재 개발중인 Spring boot, flutter 서비스.
- Resource Server
    - Resource Owner 의 정보를 가지고있는 서버 ex) Google, Naver, Kakao
- Authorization Server
    - 인증/인가를 수행하는 서버로 클라이언트의 자격을 확인하고 Access Token 을 발급해주는 역할을 한다.
    - 개념상으로 Resource Server 와 분리되어 있지만 보통 한 기업에 함께 있다고 생각하면 됨.
- Authorization Code
    - Authorization Server에게 Access Token을 받아올 때 필요한 Code 소셜 로그인에 성공하면 받을 수 있다.
- Access Token
    - Resource Owner의 정보에 접근하기 위해 필요한 토큰. Authorization Code를 주고 얻을 수 있다.

### 플로우

1. Resource Owner 가 Flutter에서 Spring에게 소셜로그인 요청
2. 로그인 페이지 Redircet
3. 로그인이 완료되면 지정된 Redirect Uri 로 Authorizaiton Code와 함께 리다이렉트
4. Spring에서 Authorization Code를 주고 Authorization 서버에게 Access Token을 받아옴
5. Access Token를 사용해 Resource Server 에 Resource Owner의 정보를 가져옴
6. 해당 정보를 바탕으로 로그인, 회원가입 진행

### Authorization Code를 쓰는 이유

- Authorization Code 방식을 사용하지 않으면 Redirect URI 에 바로 Access Token을 담아야 하는데 이 방식은 탈취당할 위험이 크다. 그래서 중간 과정에 Authorization
  Code 를 제공하고 이 코드와 교환할 수 있게 하는 방법이 Authorization Code 방식이다.
- Authorization Code 는 한번의 교환에만 사용되고 재사용되지 않는다.
- Redirect URI를 통해 Authorization Code를 전달하기 때문에 가로채서 Access Token을 얻을 수 있지 않을까 생각할 수도 있지만, Authorization Server에
  Authorization Code와 Access Token을 교환하기 위해선 Client ID, Client Secret이 필요하기 때문에 Authorization Code를 탈취 당하더라도 Access
  Token이 유출당할 위험은 없음.

### 현재 프로젝트에서 개선해야 할 것

소셜 로그인이 다 끝나고 Spring Boot API의 JWT 와 Refresh Token 을 flutter 에게 전달해줘야 하는데 현재는 Redirect Url의 파라미터에 담아서 전달하고 있다. 보안상 위험할 거
같아서 다른 방법을 찾아보려고 한다.

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

## 회고

- 프로젝트 설계
    - 이번 프로젝트는 앱 출시까지 제대로 해보기 위해 도메인 설계 단계부터 시작한게 처음이었고, 개인적으로는 도메인 설계를 철저히 했다고 생각했습니다. 하지만 중간 중간 초기 설계를 수정해야할 부분이 너무
      많았습니다.
      프로젝트 설계 단계의 중요성을 명확하게 인지하게 되었고, 앞서 열심히 설계 해본 경험 덕분에 설계를 하지 않고 시작한 것보단 프로젝트 진행이 훨씬 수월했습니다. 프로젝트 설계를 생각보다 자세히, 완전하게
      해야한다는 것을 알게 되었습니다.
- CI/CD 경험
    - 로컬에서 git push를 하면 github actions 에서 자동으로 빌드, 테스트를 진행하고 문제가 없으면 AWS에 빌드된 프로젝트를 전달해 내가 만들어 놓은 스크립트 대로 프로젝트를 배포하는
      방식으로 CI/CD를 구축했습니다. 이 과정에서 반복적인 단순노동 과정을 자동화 시켜 개발의 질이 굉장히 올라갔습니다. 매번 직접 배포하게 되면 실수가 있을 수도 있는데 배포 자동화를 통해 그런 위험도
      없어지는 것 같습니다. 이번 프로젝트를 통해 자동배포하의 편리함을 느낄 수 있었습니다.
- 풀스택 개발
    - 원래 저는 이 프로젝트의 백엔드 파트를 담당했었는데, 사정이 생겨 프론트까지 담당하게 되면서 본의 아니게 풀스택을 경험하게 되었습니다. 처음엔 프론트 경험이 없어서 에러 하나에 몇주씩 붙잡아가면서 시간을
      많이 소비했는데, 이 과정을 통해 프론트와 백엔드개발의 전체적인 흐름을 이해하게 되었습니다. 지금 돌이켜보면 소중한 경험을 한 것 같습니다.
- Srping Security, JWT, Oauth2
    - 이 프로젝트에서 가장 어려움이 많았던 파트였습니다. 프로젝트를 진행하기 전까지는 보안 개념이 전무하다 싶었습니다. 하지만 이 프로젝트를 진행하면서 보안에 대한 이해도가 높아졌고, JWT, Refresh
      Token, Oauth2를 구현한 경험으로 Spring Security를 완벽히 이해하진 않지만, 기본적인 흐름을 파악하는 데에 성공했습니다.

## **TODO**

- [x]  local, dev, prod 개발 환경 분리
- [x]  배포 자동화
- [x]  Spring Security 로 JWT 토큰 인증 구현
- [x]  JWT Refresh Token 구현
- [x]  채팅기능 구현
- [x]  파일 업로드 처리
- [x]  OAuth2 구현
- [ ]  바뀐 도메인 설계 수정
- [ ]  Response 체계화
- [ ]  ExceptionHandler, ControllerAdvice 적용해서 예외처리