# Tnovel Backend Service
Tnovel Backend Structure 설명 README 파일입니다.

## ✨Common
### REST API
Tnovel REST API는 Spring Boot로 제작했다.

### Folder Structure
- `src`: 메인 로직
  `src`에는 도메인 별로 패키지를 구성하도록 했다. 회원(User), 게시글(Board), 결제 및 구독(Pay)로 도메인이 나뉜다.
- `common`: 메인 로직은 아니지만 `src` 에서 필요한 부차적인 파일들을 모아놓은 폴더
  - `config`: 설정 파일 관련 폴더
  - `entity`: 공통 Entity 관리 폴더
  - `exceptions`: 예외처리 관리 폴더
  - `oauth`: Oauth 인증에 필요한 파일 관리 폴더
  - `response`: baseResponse를 관리하는 폴더
  - `secret`: 보안과 관련된 파일 관리 폴더(차후 환경 변수로 분리 추천)
  - `Constant`: 상수와 관련된 내용

- 도메인 폴더 구조
> entity - model - controller - service - repository

- `Entity` : 해당 도메인에서 사용될 엔티티 모음
- `model` : 해당 컨트롤러, 서비스, 레포지토리 레이어에서 사용할 DTO를 모음.
- `Controller`: Request를 처리하고 Response 해주는 곳. (Service에 넘겨주고 다시 받아온 결과값을 형식화), 형식적 Validation
- `Service`: 비즈니스 로직 처리, 논리적 Validation
- `Repository`: Spring Data JPA Interface를 상속받아 DB 처리를 가능하게 해준다.


## ✨Structure
앞에 (*)이 붙어있는 파일(or 폴더)은 추가적인 과정 이후에 생성된다.
```text
api-server-spring-boot
  > * build
  > gradle
  > src.main.java.com.example.demo
    > common
      > config
        | RestTemplateConfig.java // HTTP get,post 요청을 날릴때 일정한 형식에 맞춰주는 template
        | SwaggerConfig.java // Swagger 관련 설정
        | WebConfig.java // Web 관련 설정(CORS 설정 포함)
      > entity
        | BaseEntity.java // create, update, state 등 Entity에 공통적으로 정의되는 변수를 정의한 BaseEntity
      > exceptions
        | BaseException.java // Controller, Service에서 Response 용으로 공통적으로 사용 될 익셉션 클래스
        | ExceptionAdvice.java // ExceptionHandler를 활용하여 정의해놓은 예외처리를 통합 관리하는 클래스
      > oauth
        | KakaoOauth.java // Kakao OAuth 처리 클래스
        | OAuthService.java // OAuth 공통 처리 서비스 클래스
        | SocialOauth.java // OAuth 공통 메소드 정의 인터페이스
      > response
        | BaseResponse.java // Controller 에서 Response 용으로 공통적으로 사용되는 구조를 위한 모델 클래스
        | BaseResponseStatus.java // Controller, Service에서 사용할 Response Status 관리 클래스 
      > secret
        | Secret.java // jwt 암호키 보관 클래스
      | Constant // 상수 보관 클래스
    > src
      > board // 게시물 관련(피드, 댓글, 신고)
        > entity
          | Comment // 댓글
          | Post // 게시물
          | PostImgPath // 게시물에 첨부되는 이미지 
          | Report // 신고
         > repositories
         PostController.java
         PostService.java
      > pay // 결제 관련(결제, 구독)
        > entity
          | Order.java // 결제(주문) 
          | Subscribe.java // 구독
        > model
        > repository
        PayController.java
        PayService
      > user // 회원 정보 관련(회원가입, 회원정보)
        > entity
          | User.java // User Entity
        > model
          | .. 생략 
        | UserController.java
        | UserService.java
        | UserRepository.java
    > utils
      | JwtService.java // JWT 관련 클래스
      | SHA256.java // 암호화 알고리즘 클래스
      | ValidateRegex.java // 정규표현식 관련 클래스
    | DemoApplication // SpringBootApplication 서버 시작 지점
  > resources
    | application.yml // Database 연동을 위한 설정 값 세팅 및 Port 정의 파일
    | logback-spring.xml // logback 설정 xml 파일
build.gradle // gradle 빌드시에 필요한 dependency 설정하는 곳
.gitignore // git 에 포함되지 않아야 하는 폴더, 파일들을 작성 해놓는 곳

```
## ✨Description

### ERD
![](/Users/gnues/Downloads/Gridgestagram_20230920_234707.jpg)

자세한 링크는 https://aquerytool.com/aquerymain/index/?rurl=0993eee7-e3ee-4fde-bd1e-b6dac07d5f89 확인
(비밀번호 : 3ehs35)

### 서비스 구성도

![](/Users/gnues/Desktop/스크린샷 2023-09-20 오후 11.54.18.png)

### API 명세서 (SWAGGER)

pdf 참조.