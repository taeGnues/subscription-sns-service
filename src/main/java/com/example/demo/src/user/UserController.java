package com.example.demo.src.user;


import com.example.demo.common.Constant.SocialLoginType;
import com.example.demo.common.oauth.OAuthService;
import com.example.demo.utils.JwtService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.annotations.OpenAPI30;
import lombok.RequiredArgsConstructor;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.user.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


import static com.example.demo.common.response.BaseResponseStatus.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/users")
@Tag(name="User", description = "User API")
public class UserController {


    private final UserService userService;

    private final OAuthService oAuthService;

    private final JwtService jwtService;


    /* 일반 회원 가입 API */
    @ResponseBody // BODY
    @PostMapping("")
    @Operation(summary = "일반 회원 가입", description = "postUserReq에 정보에 맞는 회원을 생성한다.")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // 형식적 데이터 검사.

        if(postUserReq.getPhoneNum() == null){
            return new BaseResponse<>(USERS_EMPTY_PHONENUM);
        }
        if(postUserReq.getName() == null){
            return new BaseResponse<>(USERS_EMPTY_NAME);
        }
        if(postUserReq.getUserID() == null){
            return new BaseResponse<>(USERS_EMPTY_USERID);
        }
        if(postUserReq.getPassword() == null){
            return new BaseResponse<>(USERS_EMPTY_PASSWORD);
        }
        if(postUserReq.getBirthDate() == null){
            return new BaseResponse<>(USERS_EMPTY_BIRTHDATE);
        }

        PostUserRes postUserRes = userService.createUser(postUserReq);
        return new BaseResponse<>(postUserRes);
    }


    /* 비밀번호 찾기 로직 */

    /* 전화번호로 회원 존재 확인 -> userIdx 리턴 */
    /* GET */
    @ResponseBody
    @GetMapping("/changepwd")
    @Operation(summary = "전화번호로 회원 찾기", description = "전화번호에 맞는 회원을 찾아 userIdx를 리턴한다.")
    public BaseResponse<Long> findUserByPhoneNum(@RequestBody PostPhoneNumReq postPhoneNumReq){
        if(postPhoneNumReq.getPhoneNum() == null){
            return new BaseResponse<>(USERS_EMPTY_PHONENUM);
        }

        Long userIdx = userService.findUserByPhoneNum(postPhoneNumReq.getPhoneNum());

        return new BaseResponse<>(userIdx);
    }

    /* userIdx로 찾은 유저 비밀번호 변경해주기 */
    @ResponseBody
    @PatchMapping("/changepwd")
    @Operation(summary = "userIdx로 회원 비밀번호 수정", description = "userIdx로 회원 정보를 찾은 후 비밀번호를 수정한다.")
    public BaseResponse<String> changePassword(@RequestBody PatchPwdReq patchPwdReq){
        if(patchPwdReq.getUserIdx() == null){
            return new BaseResponse<>(USERS_INFO_UNKNOWN);
        }

        String res = userService.changePassword(patchPwdReq);

        return new BaseResponse<>(res);
    }

    /* 회원 1명 조회 API */
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/app/users/:userId
    @Operation(summary = "회원 1명 조회", description = "userIdx에 맞는 회원을 조회한다.")
    public BaseResponse<GetUserRes> getUser(@PathVariable("userIdx") Long userIdx) {
        GetUserRes getUserRes = userService.getUser(userIdx);
        return new BaseResponse<>(getUserRes);
    }

    @ResponseBody
    @DeleteMapping("/{userId}")
    @Operation(summary = "회원 1명 삭제", description = "userIdx에 맞는 회원을 삭제한다.")
    public BaseResponse<String> deleteUser(@PathVariable("userId") Long userId){
        Long jwtUserId = jwtService.getUserIdx();

        userService.deleteUser(userId);

        String result = "삭제 완료!!";
        return new BaseResponse<>(result);
    }

    /* 일반 유저 로그인 */
    @ResponseBody
    @PostMapping("/logIn")
    @Operation(summary = "일반 회원 로그인", description = "postLoginReq에 정보에 맞는 회원을 로그인 시킨 후 JWT을 발급한다.")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){

        // 요청값 유효성 확인

        if(postLoginReq.getUserID() == null){
            return new BaseResponse<>(USERS_EMPTY_USERID);
        }
        if(postLoginReq.getPassword() == null){
            return new BaseResponse<>(USERS_EMPTY_PASSWORD);
        }

        PostLoginRes postLoginRes = userService.logIn(postLoginReq);
        return new BaseResponse<>(postLoginRes);
    }


    /* 유저 소셜 가입, 로그인 인증으로 리다이렉트 해주는 url => Step01. '인가코드 받는 URI' 생성 */

    @GetMapping("/auth/{socialLoginType}/login")
    @Operation(summary = "유저 소셜 로그인 redirectUrl", description = "소셜 가입(카카오)로 할 수 있는 redirectUrl를 리턴해준다.")
    public String socialLoginRedirect(@PathVariable(name="socialLoginType") String SocialLoginPath) throws IOException {
        SocialLoginType socialLoginType= SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
        String redirectUrl = oAuthService.accessRequest(socialLoginType);
        return redirectUrl;
    }


    /* Social Login API Server 요청에 의한 callback 을 처리, SNS Login 요청 결과로 받은 Json 형태의 java 객체 (access_token, jwt_token, user_num 등) */
    @ResponseBody
    @GetMapping(value = "/auth/{socialLoginType}/login/callback")
    @Operation(summary = "유저 소셜 정보로 가입", description = "소셜 정보로 회원가입 혹은 로그인을 진행한다.")
    public BaseResponse<GetSocialOAuthRes> socialLoginCallback(
            @PathVariable(name = "socialLoginType") String socialLoginPath,
            @RequestParam(name = "code") String code) throws IOException, BaseException{
        log.info(">> 소셜 로그인 API 서버로부터 받은 인가 code : {}", code);
        SocialLoginType socialLoginType = SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        GetSocialOAuthRes getSocialOAuthRes = oAuthService.oAuthLoginOrJoin(socialLoginType,code);
        return new BaseResponse<>(getSocialOAuthRes);
    }

    /* ADMIN : 유저 조회 API (전체) */
    /* ADMIN : 유저 상세 조회 API */
    /* ADMIN : 유저 상태 변경 API */

}
