# Twitter Clone Backend Demo Web Application


Java Version: 17 LTS
---------------------------------------

Spring Boot Version: 2.5.7
---------------------------------------

Dependencies
---------------------------------------
1. Developer Tools
    - Lombok
2. WEB
    - Spring Web
3. SQL
    - MySql Driver
    - H2 Database
    - Spring Data JPA
4. Security
    - Spring Security
    - JWT
5. I/O
    - Java Mail Sender
6. Document API
    - Springfox Swagger 3.0.0
7. Messaging
    - WebSocket
8. Testing
    - JUnit 5
    - AssertJ
    - Mockito
9. Additional
    - TimeAgo - library for displaying dates as relative time ago language

Architecture
---------------------------------------
<img src = "readme_images/twitter-clone-architecture.jpg" >

Entity Relationship Diagram
---------------------------------------




Implemented Features
---------------------------------------
- authentication
  - sign up with email confirmation
  - login authentication
- user profile:
  - update user profile
  - check selected user profile
  - follow/unfollow user
- posts:
  - add posts
  - delete posts
  - update posts / not implemented
- comments:
  - add comments to posts