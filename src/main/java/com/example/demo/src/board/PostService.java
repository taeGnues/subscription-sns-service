package com.example.demo.src.board;



import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.board.entity.Comment;
import com.example.demo.src.board.entity.Post;
import com.example.demo.src.board.entity.Report;
import com.example.demo.src.board.model.*;
import com.example.demo.src.board.repositories.CommentRepository;
import com.example.demo.src.board.repositories.PostRepository;
import com.example.demo.src.board.repositories.ReportRepository;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Transactional
@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;
    private final JwtService jwtService;


    /* 게시물 등록 */
    @Transactional
    public PostPostRes createPost(Long userIdx, PostPostReq postPostReq) {

        Post savePost;

        try {
            User user = userRepository.findByUserIdxAndState(userIdx, ACTIVE)
                    .orElseThrow(() -> new BaseException(NOT_FIND_USER));

            System.out.println(user.getName());
            System.out.println(postPostReq.toString());

            savePost = postRepository.save(postPostReq.toEntity(user));

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(BOARD_POST_SAVE_FAILED);

        }

        return new PostPostRes(savePost.getPostIdx());
    }

    /* 게시물 삭제 */
    @Transactional
    public void deletePost(Long postIdx) {
        Post post = postRepository.findByPostIdxAndState(postIdx, ACTIVE)
                .orElseThrow(() -> new BaseException(BOARD_NOT_FIND_POST));
        post.deletePost();
    }

    /* 게시물 수정 */
    @Transactional
    public void modifyPost(Long postIdx, PatchPostReq patchPostReq) {

        try {
            User user = userRepository.findByUserIdxAndState(patchPostReq.getUserIdx(), ACTIVE)
                    .orElseThrow(() -> new BaseException(NOT_FIND_USER));
            Post post = postRepository.findByPostIdxAndState(postIdx, ACTIVE)
                    .orElseThrow(() -> new BaseException(BOARD_NOT_FIND_POST));

            post.updatePost(patchPostReq.getContent());

            postRepository.save(post);

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(BOARD_POST_SAVE_FAILED);

        }

    }

    /* 게시물 조회 (자신) */
    @Transactional(readOnly = true)
    public GetUserFeedPostRes readFeedMyPosts(int pageNo, int pageSize, Long userIdx) {

        List<GetPostRes> getPostResList = new ArrayList<>();

        try {
            List<Post> res = postRepository.findMyPosts(userIdx, PageRequest.of(pageNo, pageSize));

            for(Post post : res){
                GetPostRes getPostRes = post.toGetPostRes(); // 변환
                getPostResList.add(getPostRes);
            }

            return GetUserFeedPostRes.builder()
                    .getPostResList(getPostResList)
                    .pageNo(pageNo)
                    .pageSize(pageSize)
                    .build();

        }catch (BaseException e){
            e.printStackTrace();
            throw new BaseException(BOARD_EMPTY_POST);
        }
    }

    /* 게시물 조회 (다른 유저) */
    @Transactional(readOnly = true)
    public GetUserFeedPostRes readFeedOtherPosts(int pageNo, int pageSize, Long userIdx) {

        List<GetPostRes> getPostResList = new ArrayList<>();

        try {
            List<Post> res = postRepository.findOtherPosts(userIdx, PageRequest.of(pageNo, pageSize));

            for(Post post : res){
                GetPostRes getPostRes = post.toGetPostRes(); // 변환
                getPostResList.add(getPostRes);
            }

            return GetUserFeedPostRes.builder()
                    .getPostResList(getPostResList)
                    .pageNo(pageNo)
                    .pageSize(pageSize)
                    .build();

        }catch (BaseException e){
            e.printStackTrace();
            throw new BaseException(BOARD_EMPTY_POST);
        }
    }

    /* 게시물 조회 (전체) */
    @Transactional(readOnly = true)
    public GetUserFeedPostRes readAllPosts(int pageNo, int pageSize) {

        List<GetPostRes> getPostResList = new ArrayList<>();

        try {
            List<Post> res = postRepository.findPosts(PageRequest.of(pageNo, pageSize));

            for(Post post : res){
                GetPostRes getPostRes = post.toGetPostRes(); // 변환
                getPostResList.add(getPostRes);
            }

            return GetUserFeedPostRes.builder()
                    .getPostResList(getPostResList)
                    .pageNo(pageNo)
                    .pageSize(pageSize)
                    .build();

        }catch (BaseException e){
            e.printStackTrace();
            throw new BaseException(BOARD_EMPTY_POST);
        }
    }

    /* 댓글 작성 */
    @Transactional
    public PostCommentRes createComment(Long userIdx, Long postIdx, PostCommentReq postCommentReq) {
        Comment saveComment;

        try {
            User user = userRepository.findByUserIdxAndState(userIdx, ACTIVE)
                    .orElseThrow(() -> new BaseException(NOT_FIND_USER));
            Post post = postRepository.findByPostIdxAndState(postIdx, ACTIVE)
                    .orElseThrow(() -> new BaseException(BOARD_NOT_FIND_POST));

            saveComment = commentRepository.save(postCommentReq.toEntity(user, post));

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(BOARD_COMMENT_SAVE_FAILED);
        }

        return new PostCommentRes(saveComment.getCommentIdx());
    }

    /* 댓글 삭제 */
    @Transactional
    public void deleteComment(Long commentIdx) {
        Comment comment = commentRepository.findByCommentIdxAndState(commentIdx, ACTIVE)
                .orElseThrow(() -> new BaseException(BOARD_NOT_FIND_POST));
        comment.deleteComment();
    }

    /* 해당 포스트 댓글 조회 */
    @Transactional(readOnly = true)
    public GetUserFeedCommentRes readFeedComments(int pageNo, int pageSize, Long postIdx) {

        List<GetCommentRes> getCommentResList = new ArrayList<>();

        try {
            List<Comment> res = commentRepository.findCommentsByPostIdx(postIdx, PageRequest.of(pageNo, pageSize));

            for(Comment comment : res){
                GetCommentRes getCommentRes = comment.toGetCommentRes(); // 변환
                getCommentResList.add(getCommentRes);
            }

            return GetUserFeedCommentRes.builder()
                    .getCommentResList(getCommentResList)
                    .pageNo(pageNo)
                    .pageSize(pageSize)
                    .build();

        }catch (BaseException e){
            e.printStackTrace();
            throw new BaseException(BOARD_EMPTY_COMMENT);
        }
    }


    /* 신고 등록 */
    @Transactional
    public void createReport(PostReportReq postReportReq) {
        Report saveReport;
        Comment comment = null;
        Post post = null;

        try {
            if(postReportReq.getCommentIdx()!=null){
                 comment = commentRepository.findByCommentIdxAndState(postReportReq.getCommentIdx(), ACTIVE)
                        .orElseThrow(() -> new BaseException(BOARD_NOT_FIND_COMMENT));
            }
            if(postReportReq.getPostIdx()!=null){
                post = postRepository.findByPostIdxAndState(postReportReq.getPostIdx(), ACTIVE)
                        .orElseThrow(() -> new BaseException(BOARD_NOT_FIND_POST));
            }

            reportRepository.save(postReportReq.toEntity(comment, post));

        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(BOARD_REPORT_SAVE_FAILED);
        }
    }

    /* 신고 조회 (전체) */
    @Transactional(readOnly = true)
    public GetAllReportRes readAllReports(int pageNo, int pageSize) {

        List<GetReportRes> getReportResList = new ArrayList<>();

        try {
            List<Report> res = reportRepository.findReports(PageRequest.of(pageNo, pageSize));
            GetReportRes getReportRes= new GetReportRes();
            for(Report report : res){
                Post reportPost = report.getPost();
                Comment reportComment = report.getComment();

                if(reportPost != null && reportPost.getPostIdx() != null){
                    getReportRes = report.toGetReportRes(report.getReportIdx(), "게시글");
                }else if(reportComment != null && reportComment.getCommentIdx() != null){
                    getReportRes = report.toGetReportRes(report.getReportIdx(), "댓글");
                }


                getReportResList.add(getReportRes);
            }

            return GetAllReportRes.builder()
                    .getReportResList(getReportResList)
                    .pageNo(pageNo)
                    .pageSize(pageSize)
                    .build();

        }catch (BaseException e){
            e.printStackTrace();
            throw new BaseException(BOARD_EMPTY_REPORT);
        }
    }

    /* 신고 삭제 */
    @Transactional
    public void deleteReport(Long reportIdx) {
        Report report = reportRepository.findByReportIdxAndState(reportIdx, ACTIVE)
                .orElseThrow(() -> new BaseException(BOARD_NOT_FIND_POST));
        report.deleteReport();
    }

}
