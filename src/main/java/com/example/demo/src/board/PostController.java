package com.example.demo.src.board;


import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.firebase.FireBaseService;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.board.model.*;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.demo.common.response.BaseResponseStatus.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/posts")
public class PostController {


    private final PostService postService;

    private final JwtService jwtService;

    private final FireBaseService fireBaseService;


    /* 게시물 등록 API */
    @ResponseBody // BODY
    @PostMapping("")
    public BaseResponse<PostPostRes> createPost(@Valid @RequestBody PostPostReq postPostReq) {
        // 형식적 데이터 검사.
        try{

            PostPostRes postPostRes = postService.createPost(postPostReq.getUserIdx(), postPostReq);
            return new BaseResponse<>(postPostRes);

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /* 게시물 삭제 API */
    @DeleteMapping("/{postIdx}")
    public BaseResponse<String> deletePost(@PathVariable Long postIdx){
        try{

            postService.deletePost(postIdx);

            return new BaseResponse<>(SUCCESS);

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /* 게시물 수정 API */
    @ResponseBody
    @PatchMapping("/{postIdx}")
    public BaseResponse<String> modifyPost(@PathVariable("postIdx") Long postIdx, @Valid @RequestBody PatchPostReq patchPostReq){

//        Long jwtUserId = jwtService.getUserIdx();
        try {
            postService.modifyPost(postIdx, patchPostReq);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }


    }

    /* 게시물 조회 API (자신) */

    @ResponseBody
    @GetMapping("/mypage/{userIdx}")
    public BaseResponse<GetUserFeedPostRes> readFeedMyPost(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "3", required = false) int pageSize,
            @PathVariable Long userIdx){
        try {
            GetUserFeedPostRes getUserFeedPostRes = postService.readFeedMyPosts(pageNo, pageSize, userIdx);
            return new BaseResponse<>(getUserFeedPostRes);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /* 게시물 조회 API (상대) */

    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<GetUserFeedPostRes> readFeedOtherPost(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "3", required = false) int pageSize, // 페이징 처리
            @PathVariable Long userIdx){
        try {
            GetUserFeedPostRes getUserFeedPostRes = postService.readFeedOtherPosts(pageNo, pageSize, userIdx);
            return new BaseResponse<>(getUserFeedPostRes);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /* 댓글 작성 API */

    /* 댓글 조회 API */

    /* 신고 API */




    /* ADMIN : 게시물 조회 API (전체) */
    /* ADMIN : 게시물 조회 API (전체) */
    /* ADMIN : 댓글 삭제 API (전체) */

//    /* 비밀번호 찾기 로직 */
//
//    /* 전화번호로 회원 존재 확인 -> userIdx 리턴 */
//    /* GET */
//    @ResponseBody
//    @GetMapping("/changepwd")
//    public BaseResponse<Long> findUserByPhoneNum(@RequestBody PostPhoneNumReq postPhoneNumReq){
//        if(postPhoneNumReq.getPhoneNum() == null){
//            return new BaseResponse<>(USERS_EMPTY_PHONENUM);
//        }
//
//        Long userIdx = userService.findUserByPhoneNum(postPhoneNumReq.getPhoneNum());
//
//        return new BaseResponse<>(userIdx);
//    }
//
//    /* userIdx로 찾은 유저 비밀번호 변경해주기 */
//    @ResponseBody
//    @PatchMapping("/changepwd")
//    public BaseResponse<String> changePassword(@RequestBody PatchPwdReq patchPwdReq){
//        if(patchPwdReq.getUserIdx() == null){
//            return new BaseResponse<>(USERS_INFO_UNKNOWN);
//        }
//
//        String res = userService.changePassword(patchPwdReq);
//
//        return new BaseResponse<>(res);
//    }
//
//    /** 전체 회원 조회
//     * 회원 조회 API
//     * [GET] /users
//     * 회원 번호 및 전화번호 검색 조회 API
//     * [GET] /app/users? Email=
//     * @return BaseResponse<List<GetUserRes>>
//     */
//    //Query String
////    @ResponseBody
////    @GetMapping("") // (GET) 127.0.0.1:9000/app/users
////    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String Email) {
////        if(Email == null){
////            List<GetUserRes> getUsersRes = userService.getUsers();
////            return new BaseResponse<>(getUsersRes);
////        }
////        // Get Users
////        List<GetUserRes> getUsersRes = userService.getUsers(Email);
////        return new BaseResponse<>(getUsersRes);
////    }
//
//    /* 회원 1명 조회 API */
//    @ResponseBody
//    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/app/users/:userId
//    public BaseResponse<GetUserRes> getUser(@PathVariable("userIdx") Long userIdx) {
//        GetUserRes getUserRes = userService.getUser(userIdx);
//        return new BaseResponse<>(getUserRes);
//    }
//
//
//
//    /**
//     * 유저정보변경 API
//     * [PATCH] /app/users/:userId
//     * @return BaseResponse<String>
//     */
////    @ResponseBody
////    @PatchMapping("/{userIdx}")
////    public BaseResponse<String> modifyUserName(@PathVariable("userId") Long userId, @RequestBody PatchUserReq patchUserReq){
////
////        Long jwtUserId = jwtService.getUserIdx();
////
////        userService.modifyUserName(userId, patchUserReq);
////
////        String result = "수정 완료!!";
////        return new BaseResponse<>(result);
////
////    }
//
//    /**
//     * 유저정보삭제 API
//     * [DELETE] /app/users/:userId
//     * @return BaseResponse<String>
//     */
//    @ResponseBody
//    @DeleteMapping("/{userId}")
//    public BaseResponse<String> deleteUser(@PathVariable("userId") Long userId){
//        Long jwtUserId = jwtService.getUserIdx();
//
//        userService.deleteUser(userId);
//
//        String result = "삭제 완료!!";
//        return new BaseResponse<>(result);
//    }
//
//    /* 일반 유저 로그인 */
//    @ResponseBody
//    @PostMapping("/logIn")
//    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
//
//        // 요청값 유효성 확인
//
//        if(postLoginReq.getUserID() == null){
//            return new BaseResponse<>(USERS_EMPTY_USERID);
//        }
//        if(postLoginReq.getPassword() == null){
//            return new BaseResponse<>(USERS_EMPTY_PASSWORD);
//        }
//
//        // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
//        PostLoginRes postLoginRes = userService.logIn(postLoginReq);
//        return new BaseResponse<>(postLoginRes);
//    }
//
//    @PostMapping("/files")
//    public String uploadFile(@RequestParam("file") MultipartFile file, String nameFile)
//            throws IOException, FirebaseAuthException {
//        if (file.isEmpty()) {
//            return "is empty";
//        }
//        return fireBaseService.uploadFiles(file, nameFile);
//    }
}
