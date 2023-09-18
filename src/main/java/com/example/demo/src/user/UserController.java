package com.example.demo.src.user;


import com.example.demo.common.Constant.SocialLoginType;
import com.example.demo.common.oauth.OAuthService;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.user.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


import static com.example.demo.common.response.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/users")
public class UserController {


    private final UserService userService;

    private final OAuthService oAuthService;

    private final JwtService jwtService;


    /* 일반 회원 가입 API */
    @ResponseBody // BODY
    @PostMapping("")
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
    public BaseResponse<String> changePassword(@RequestBody PatchPwdReq patchPwdReq){
        if(patchPwdReq.getUserIdx() == null){
            return new BaseResponse<>(USERS_INFO_UNKNOWN);
        }

        String res = userService.changePassword(patchPwdReq);

        return new BaseResponse<>(res);
    }


    //Query String
//    @ResponseBody
//    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
//    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String Email) {
//        if(Email == null){
//            List<GetUserRes> getUsersRes = userService.getUsers();
//            return new BaseResponse<>(getUsersRes);
//        }
//        // Get Users
//        List<GetUserRes> getUsersRes = userService.getUsers(Email);
//        return new BaseResponse<>(getUsersRes);
//    }

    /* 회원 1명 조회 API */
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/app/users/:userId
    public BaseResponse<GetUserRes> getUser(@PathVariable("userIdx") Long userIdx) {
        GetUserRes getUserRes = userService.getUser(userIdx);
        return new BaseResponse<>(getUserRes);
    }



    /**
     * 유저정보변경 API
     * [PATCH] /app/users/:userId
     * @return BaseResponse<String>
     */
//    @ResponseBody
//    @PatchMapping("/{userIdx}")
//    public BaseResponse<String> modifyUserName(@PathVariable("userId") Long userId, @RequestBody PatchUserReq patchUserReq){
//
//        Long jwtUserId = jwtService.getUserIdx();
//
//        userService.modifyUserName(userId, patchUserReq);
//
//        String result = "수정 완료!!";
//        return new BaseResponse<>(result);
//
//    }

    /**
     * 유저정보삭제 API
     * [DELETE] /app/users/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @DeleteMapping("/{userId}")
    public BaseResponse<String> deleteUser(@PathVariable("userId") Long userId){
        Long jwtUserId = jwtService.getUserIdx();

        userService.deleteUser(userId);

        String result = "삭제 완료!!";
        return new BaseResponse<>(result);
    }

    /* 일반 유저 로그인 */
    @ResponseBody
    @PostMapping("/logIn")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){

        // 요청값 유효성 확인

        if(postLoginReq.getUserID() == null){
            return new BaseResponse<>(USERS_EMPTY_USERID);
        }
        if(postLoginReq.getPassword() == null){
            return new BaseResponse<>(USERS_EMPTY_PASSWORD);
        }

        // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
        PostLoginRes postLoginRes = userService.logIn(postLoginReq);
        return new BaseResponse<>(postLoginRes);
    }


    /* 유저 소셜 가입, 로그인 인증으로 리다이렉트 해주는 url => Step01. '인가코드 받는 URI' 생성 */
    @GetMapping("/auth/{socialLoginType}/login")
    public String socialLoginRedirect(@PathVariable(name="socialLoginType") String SocialLoginPath) throws IOException {
        SocialLoginType socialLoginType= SocialLoginType.valueOf(SocialLoginPath.toUpperCase());
        String redirectUrl = oAuthService.accessRequest(socialLoginType);
        return redirectUrl;
    }


    /* Social Login API Server 요청에 의한 callback 을 처리, SNS Login 요청 결과로 받은 Json 형태의 java 객체 (access_token, jwt_token, user_num 등) */
    @ResponseBody
    @GetMapping(value = "/auth/{socialLoginType}/login/callback")
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
