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

    /* 댓글 등록 API */
    @ResponseBody // BODY
    @PostMapping("/{postIdx}/comment/")
    public BaseResponse<PostCommentRes> createPost(
            @PathVariable Long postIdx,
            @Valid @RequestBody PostCommentReq postCommentReq) {

        try{
            PostCommentRes postCommentRes = postService.createComment(postCommentReq.getUserIdx(), postIdx, postCommentReq);
            return new BaseResponse<>(postCommentRes);

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /* 댓글 삭제 API */
    @DeleteMapping("/comment/{commentIdx}")
    public BaseResponse<String> deleteComment(@PathVariable Long commentIdx){
        try{

            postService.deleteComment(commentIdx);

            return new BaseResponse<>(SUCCESS);

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /* 해당 포스트의 댓글 조회 API */
    @ResponseBody
    @GetMapping("/{postIdx}/comments")
    public BaseResponse<GetUserFeedCommentRes> readFeedComments(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "3", required = false) int pageSize,
            @PathVariable Long postIdx){
        try {
            GetUserFeedCommentRes getUserFeedCommentRes = postService.readFeedComments(pageNo, pageSize, postIdx);
            return new BaseResponse<>(getUserFeedCommentRes);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    /* 신고 API */




    /* ADMIN : 게시물 조회 API (전체) */
    /* ADMIN : 게시물 조회 API (전체) */
    /* ADMIN : 댓글 삭제 API (전체) */

}
