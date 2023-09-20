package com.example.demo.src.board;


import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.firebase.FireBaseService;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.board.model.*;
import com.example.demo.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.demo.common.response.BaseResponseStatus.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/posts")
@Tag(name = "Board", description = "Board API")
public class PostController {


    private final PostService postService;

    private final JwtService jwtService;

    private final FireBaseService fireBaseService;


    /* 게시물 등록 API */
    @ResponseBody // BODY
    @PostMapping("")
    @Tag(name="Board")
    @Operation(summary = "게시물 등록", description = "로그인한 회원 정보로 게시물을 등록한다.")
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
    @Tag(name="Board")
    @Operation(summary = "게시물 삭제", description = "게시물 ID(postIdx)로 게시물을 삭제한다.")
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
    @Tag(name="Board")
    @Operation(summary = "게시물 수정", description = "게시물 ID(postIdx)로 게시물을 수정한다.")
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
    @Tag(name="Board")
    @Operation(summary = "내 게시물(피드) 읽기", description = "로그인한 정보(userIdx)로 내가 올린 게시물을 확인한다.")
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
    @Tag(name="Board")
    @Operation(summary = "상대 게시물(피드) 읽기", description = "로그인한 정보(userIdx)로 남이 올린 게시물을 확인한다.")
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
    @Tag(name="Board")
    @Operation(summary = "댓글 등록", description = "로그인한 정보(userIdx)로 댓글을 작성한다.")
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
    @Tag(name="Board")
    @Operation(summary = "댓글 삭제", description = "로그인한 정보(userIdx)로 댓글을 삭제한다.")
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
    @Tag(name="Board")
    @Operation(summary = "해당 포스트 댓글 조회", description = "해당 포스트(postIdx)에 작성된 댓글을 확인한다.")
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


    /* 신고 등록 API */
    @ResponseBody // BODY
    @PostMapping("/report")
    @Tag(name="Board")
    @Operation(summary = "신고 등록", description = "포스트(postIdx) 혹은 댓글(commentIdx)를 신고한다.")
    public BaseResponse<String> createReport(
            @Valid @RequestBody PostReportReq postReportReq) {

        if(postReportReq.getPostIdx()==null && postReportReq.getCommentIdx()==null){
            return new BaseResponse<>(BOARD_REPORT_UNKNOWN);
        }

        try{
            postService.createReport(postReportReq);
            return new BaseResponse<>(SUCCESS);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /* ADMIN : 게시물 조회 API (전체) */
    @ResponseBody
    @GetMapping("")
    @Tag(name="Board")
    @Operation(summary = "전체 게시물 조회", description = "모든 게시물을 조회한다(ADMIN)")
    public BaseResponse<GetUserFeedPostRes> readAllPosts(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "3", required = false) int pageSize
            ){
        try {
            GetUserFeedPostRes getUserFeedPostRes = postService.readAllPosts(pageNo, pageSize);
            return new BaseResponse<>(getUserFeedPostRes);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /* ADMIN : 신고 조회 API (전체) */
    @ResponseBody
    @GetMapping("/reports")
    @Tag(name="Board")
    @Operation(summary = "전체 신고 내역 조회", description = "모든 신고 내역을 조회한다(ADMIN)")
    public BaseResponse<GetAllReportRes> readAllreports(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "3", required = false) int pageSize
    ){
        try {
            GetAllReportRes getAllReportRes = postService.readAllReports(pageNo, pageSize);
            return new BaseResponse<>(getAllReportRes);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /* ADMIN : 신고 삭제 API */
    @DeleteMapping("/reports/{reportIdx}")
    @Tag(name="Board")
    @Operation(summary = "신고 내역 삭제", description = "특정 신고 내역(reportIdx)을 삭제한다(ADMIN)")
    public BaseResponse<String> deleteReport(@PathVariable Long reportIdx){
        try{

            postService.deleteReport(reportIdx);

            return new BaseResponse<>(SUCCESS);

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }



}
