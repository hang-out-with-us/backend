## 환경

- DB
    - MariaDB
    - h2 (test)
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
    - 삭제
- 추천 기능
    - 가까운 지역에서 인원 수와 관심사가 맞는 사람을 추천
- 매칭 기능
    - 나를 좋아요 한 사람 리스트
    - 서로 좋아요 한 회원끼리 채팅방 개설

### 도메인 설계

![domain model](https://i.ibb.co/V3f2bm0/Screenshot-2022-12-28-at-7-56-03-PM.png)

### 엔티티

- Member
    - id : Long
    - name : String
    - email : String
    - password : String
    - age : Integer
    - board : Board
    - membersWhoLikeMe : List<Member>
    - membersILike : List<Member>
    - chatRoomList : List<ChatRoom>
- Board
    - number_of_person : Integer
    - image : String
    - coment : String
    - location : ?
    - createdDate : LocalDateTime
- ChatRoom
    - id : Long
    - members : List<Member>
    - messages : List<Message>
- Message
    - id : Long
    - messageFrom : Member
    - chatRoom : ChatRoom
    - content : String
    - createdDate : LocalDateTime

ERD
![ERD](https://i.ibb.co/kXSn8vk/Screenshot-2022-12-28-at-7-52-28-PM.png)