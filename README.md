# AI 챗봇 서비스

# 기술 스택
- **Backend : Spring Boot, Spring Data JPA, Spring Security**
- **Database : MySQL, Redis**
- **Test : JUnit**

# 주요 기능
## 1. 사용자 인증
### [회원가입]
- 사용자는 이메일, 비밀번호, 이름을 이용해 회원가입할 수 있습니다.
  - **[예외 처리]** 이메일, 비밀번호, 이름을 입력하지 않을 경우 예외를 발생시킨다.
- 사용자는 이메일 인증을 해야 회원가입할 수 있습니다.
  - **[예외 처리]** 해당 이메일로 가입한 이력이 존재할 경우 예외를 발생시킨다.
  - **[예외 처리]** 이메일 인증코드가 일치하지 않을 경우 예외를 발생시킨다.

### [로그인]
- 사용자는 가입 시 사용한 이메일, 비밀번호를 이용해 로그인할 수 있습니다.
  - **[예외 처리]** 이메일, 비밀번호를 입력하지 않을 경우 예외를 발생시킨다.
  - **[예외 처리]** 이메일 또는 비밀번호가 다를 경우 예외를 발생시킨다.
---
## 2. 채팅 기능
### [채팅]
- 사용자는 텍스트를 입력하여 전송하고, AI의 응답을 받을 수 있습니다.
### [채팅방 생성]
- 사용자가 새로운 채팅방에서 대화를 하는 경우, 새로운 채팅방 엔티티가 생성됩니다.
### [채팅 저장]
- 사용자가 대화한 기록은 저장됩니다.
### [채팅방 조회]
- 사용자는 기존 채팅방 조회 시, 이전 대화 기록을 조회할 수 있습니다.
  - **[예외 처리]** 인증되지 않은 사용자가 채팅방을 조회할 경우 예외를 발생시킨다.

---
# API 명세서
## 사용자
|기능|URL|HTTP Method|인증 여부|
|---|---|---|---|
|**회원가입**|**/members**|**POST**|**X**|
|**로그인**|**/login**|**POST**|**X**|
|**로그아웃**|**/logout**|**POST**|**O**|

## 채팅
|기능|URL|HTTP Method|인증 여부|
|---|---|---|---|
|**새로운 채팅방에서 채팅**|**/chats**|**POST**|**O/X**|
|**기존 채팅방에서 채팅**|**/chats/{conversationId}**|**POST**|**O**|
|**로그인한 사용자의 채팅방 목록 조회**|**/chats/conversations**|**GET**|**O**|
|**로그인한 사용자의 특정 채팅방 대화 기록 조회**|**/chats/conversations/{conversationId}**|**GET**|**O**|

## 이메일
|기능|URL|HTTP Method|인증 여부|
|---|---|---|---|
|**이메일 인증코드 전송**|**/emails/send-code**|**POST**|**X**|
|**이메일 인증코드 검증**|**/emails/verify-code**|**POST**|**X**|

## JWT
|기능|URL|HTTP Method|인증 여부|
|---|---|---|---|
|**토큰 재발급**|**/jwts/reissue**|**POST**|**O**|

---
# ERD
<img width="1120" height="320" alt="Image" src="https://github.com/user-attachments/assets/96efffff-6f15-473f-aaed-12cfd5c18965" />

---
# 프로젝트 구조
```
src
    ├─main
    │  ├─java
    │  │  └─bogus
    │  │      └─ai_chatbot
    │  │          │  AiChatbotApplication.java
    │  │          │  
    │  │          ├─config
    │  │          │      OpenAiConfig.java
    │  │          │      OpenApiConfig.java
    │  │          │      RedisConfig.java
    │  │          │      SecurityConfig.java
    │  │          │      WebConfig.java
    │  │          │      
    │  │          ├─domain
    │  │          │  ├─chat
    │  │          │  │  ├─controller
    │  │          │  │  │      ChatController.java
    │  │          │  │  │      
    │  │          │  │  ├─dto
    │  │          │  │  │      ChatRequest.java
    │  │          │  │  │      ChatResponse.java
    │  │          │  │  │      ConversationDto.java
    │  │          │  │  │      ConversationsDto.java
    │  │          │  │  │      MessageDto.java
    │  │          │  │  │      MessagesDto.java
    │  │          │  │  │      
    │  │          │  │  ├─entity
    │  │          │  │  │  │  Conversation.java
    │  │          │  │  │  │  Message.java
    │  │          │  │  │  │  
    │  │          │  │  │  └─field
    │  │          │  │  │          Role.java
    │  │          │  │  │          
    │  │          │  │  ├─repository
    │  │          │  │  │      ConversationRepository.java
    │  │          │  │  │      MessageRepository.java
    │  │          │  │  │      
    │  │          │  │  └─service
    │  │          │  │          ChatService.java
    │  │          │  │          ConversationService.java
    │  │          │  │          MessageService.java
    │  │          │  │          
    │  │          │  ├─common
    │  │          │  │  ├─api
    │  │          │  │  │  └─dto
    │  │          │  │  │          CustomApiResponse.java
    │  │          │  │  │          
    │  │          │  │  └─exception
    │  │          │  │      ├─error
    │  │          │  │      │  │  ErrorCode.java
    │  │          │  │      │  │  
    │  │          │  │      │  └─dto
    │  │          │  │      │          ErrorResponse.java
    │  │          │  │      │          
    │  │          │  │      ├─exception
    │  │          │  │      │      AuthException.java
    │  │          │  │      │      BusinessException.java
    │  │          │  │      │      ChatException.java
    │  │          │  │      │      CustomException.java
    │  │          │  │      │      
    │  │          │  │      └─handler
    │  │          │  │              GlobalExceptionHandler.java
    │  │          │  │              
    │  │          │  ├─email
    │  │          │  │  ├─controller
    │  │          │  │  │      EmailController.java
    │  │          │  │  │      
    │  │          │  │  ├─dto
    │  │          │  │  │      EmailDto.java
    │  │          │  │  │      
    │  │          │  │  └─service
    │  │          │  │          EmailService.java
    │  │          │  │          
    │  │          │  ├─jwt
    │  │          │  │  ├─controller
    │  │          │  │  │      ReissueController.java
    │  │          │  │  │      
    │  │          │  │  ├─dto
    │  │          │  │  │      JwtInfoDto.java
    │  │          │  │  │      
    │  │          │  │  ├─service
    │  │          │  │  │      ReissueService.java
    │  │          │  │  │      
    │  │          │  │  └─util
    │  │          │  │          JwtUtil.java
    │  │          │  │          
    │  │          │  ├─member
    │  │          │  │  ├─controller
    │  │          │  │  │      MemberController.java
    │  │          │  │  │      
    │  │          │  │  ├─dto
    │  │          │  │  │      MemberJoinDto.java
    │  │          │  │  │      MemberLoginDto.java
    │  │          │  │  │      MemberSessionDto.java
    │  │          │  │  │      
    │  │          │  │  ├─entity
    │  │          │  │  │      Member.java
    │  │          │  │  │      
    │  │          │  │  ├─repository
    │  │          │  │  │      MemberRepository.java
    │  │          │  │  │      
    │  │          │  │  └─service
    │  │          │  │          MemberService.java
    │  │          │  │          
    │  │          │  ├─openai
    │  │          │  │  ├─dto
    │  │          │  │  │      OpenAiMessage.java
    │  │          │  │  │      OpenAiRequest.java
    │  │          │  │  │      OpenAiResponse.java
    │  │          │  │  │      
    │  │          │  │  └─service
    │  │          │  │          OpenAiClient.java
    │  │          │  │          
    │  │          │  ├─redis
    │  │          │  │  └─service
    │  │          │  │          RedisService.java
    │  │          │  │          
    │  │          │  └─security
    │  │          │      ├─dto
    │  │          │      │      CustomUserDetails.java
    │  │          │      │      
    │  │          │      ├─filter
    │  │          │      │      CustomLogoutFilter.java
    │  │          │      │      ExceptionHandlerFilter.java
    │  │          │      │      JwtAuthenticationFilter.java
    │  │          │      │      LoginFilter.java
    │  │          │      │      
    │  │          │      ├─properties
    │  │          │      │      PermitPaths.java
    │  │          │      │      
    │  │          │      └─service
    │  │          │              CustomUserDetailsService.java
    │  │          │              
    │  │          └─testdata
    │  │                  TestData.java
    │  │                  
    │  └─resources
    │      │  application.yml
    │      │  
    │      ├─static
    │      └─templates
    └─test
        └─java
            └─bogus
                └─ai_chatbot
                    │  AiChatbotApplicationTests.java
                    │  
                    └─domain
                        ├─chat
                        │  └─service
                        │          ChatServiceTest.java
                        │          ConversationServiceTest.java
                        │          MessageServiceTest.java
                        │          
                        ├─email
                        │  └─service
                        │          EmailServiceTest.java
                        │          
                        ├─member
                        │  └─service
                        │          MemberServiceTest.java
                        │          
                        ├─openai
                        │  └─service
                        │          OpenAiClientTest.java
                        │          
                        └─security
                            ├─filter
                            │      CustomLogoutFilterTest.java
                            │      JwtAuthenticationFilterTest.java
                            │      LoginFilterTest.java
                            │      
                            └─service
                                    CustomUserDetailsServiceTest.java
```
---
# 실제 화면
## 홈화면(로그인 X)
<img width="1916" height="953" alt="Image" src="https://github.com/user-attachments/assets/a837960e-40dc-4628-b845-b5f0ae1f07ac" />

## 홈화면(로그인 O)
<img width="1918" height="947" alt="Image" src="https://github.com/user-attachments/assets/33314ce3-080c-4344-9752-398cce0253e1" />

## 로그인 화면
<img width="1917" height="951" alt="Image" src="https://github.com/user-attachments/assets/d7ddacc2-b1d9-4322-9427-3902d88e23c0" />

## 회원가입 화면
<img width="1917" height="943" alt="Image" src="https://github.com/user-attachments/assets/d218b7ba-7791-4a2f-baf8-33ca123bced8" />

## 회원가입 화면(이메일 인증코드 전송 후)
<img width="1918" height="952" alt="Image" src="https://github.com/user-attachments/assets/4bc139b3-f0c5-4865-9135-e2bfee04496a" />

## 채팅 화면(로그인 X)
<img width="1916" height="950" alt="Image" src="https://github.com/user-attachments/assets/6a69192b-c59b-4971-a6ff-c6e926fda43e" />

## 채팅 화면(로그인 O)
<img width="1918" height="949" alt="Image" src="https://github.com/user-attachments/assets/4ba2de3a-01f1-495a-bf95-b80691963940" />

## 채팅 화면(로그인 O + 새로운 채팅)
<img width="1919" height="949" alt="Image" src="https://github.com/user-attachments/assets/ed62fdae-22df-4566-9f32-91b95e83fdec" />
