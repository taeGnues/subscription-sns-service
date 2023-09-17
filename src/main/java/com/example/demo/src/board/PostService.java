package com.example.demo.src.board;



import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.board.entity.Post;
import com.example.demo.src.board.model.*;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import com.example.demo.utils.JwtService;
import jdk.internal.org.jline.utils.Log;
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

//    }
//
//    /* OAuth2 유저 생성 */
//    @Transactional
//    public PostUserRes createOAuthUser(User user) {
//        User saveUser = userRepository.save(user);
//
//        // JWT 발급
//        String jwtToken = jwtService.createJwt(saveUser.getUserIdx());
//        return new PostUserRes(saveUser.getUserIdx(), jwtToken);
//
//    }
//
//    /* 일반 유저 로그인 */
//    @Transactional
//    public PostLoginRes logIn(PostLoginReq postLoginReq) {
//        User user = userRepository.findByUserIDAndState(postLoginReq.getUserID(), ACTIVE)
//                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
//
//        String encryptPwd;
//        try {
//            encryptPwd = new SHA256().encrypt(postLoginReq.getPassword());
//        } catch (Exception exception) {
//            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
//        }
//
//        // JWT 발급.
//        if(user.getPassword().equals(encryptPwd)){
//            Long userIdx = user.getUserIdx();
//            String jwt = jwtService.createJwt(userIdx);
//            return new PostLoginRes(userIdx, jwt);
//        } else{
//            throw new BaseException(FAILED_TO_LOGIN);
//        }
//
//    }
//    @Transactional
//    public void deleteUser(Long userIdx) {
//        User user = userRepository.findByUserIdxAndState(userIdx, ACTIVE)
//                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
//        user.deleteUser();
//    }
//
//    /* 전체 회원 조회 */
//    @Transactional(readOnly = true)
//    public List<GetUserRes> getUsers() {
//        List<GetUserRes> getUserResList = userRepository.findAllByState(ACTIVE).stream()
//                .map(GetUserRes::new)
//                .collect(Collectors.toList());
//        return getUserResList;
//    }
//
//    /* userIdx로 유저 조회 */
//    @Transactional(readOnly = true)
//    public GetUserRes getUser(Long userIdx) {
//        User user = userRepository.findByUserIdxAndState(userIdx, ACTIVE)
//                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
//        return new GetUserRes(user);
//    }
//
//    /* userID(사용자 이름)으로 유저 존재여부 조회 */
//    @Transactional(readOnly = true)
//    public boolean checkUserByUserID(String userID) {
//
//        Optional<User> checkUser = userRepository.findByUserID(userID);
//        return checkUser.isPresent();
//    }
//
//    /* userID(사용자 이름)으로 유저 조회*/
//    @Transactional(readOnly = true)
//    public GetUserRes getUserByUserID(String userID) {
//        User user = userRepository.findByUserIDAndState(userID, ACTIVE).orElseThrow(() -> new BaseException(NOT_FIND_USER));
//        return new GetUserRes(user);
//    }
//    /* 전화번호로 유저 조회 */
//    @Transactional(readOnly = true)
//    public Long findUserByPhoneNum(String phoneNum) {
//        User user = userRepository.findByPhoneNum(phoneNum).orElseThrow(()->new BaseException(NOT_FIND_USER));
//        return user.getUserIdx();
//    }
//
//    /* 유저 idx로 해당하는 유저 전화번호 업데이트 */
//    public String changePassword(PatchPwdReq patchPwdReq) {
//        User user = userRepository.findById(patchPwdReq.getUserIdx()).orElseThrow(()->new BaseException(NOT_FIND_USER));
//
//        // 암호화
//
//        String encryptPwd;
//        try {
//            encryptPwd = new SHA256().encrypt(patchPwdReq.getPassword());
//        } catch (Exception exception) {
//            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
//        }
//
//        user.updatePassword(encryptPwd);
//        userRepository.save(user); // user 비밀번호 업데이트!!
//
//        return "SUCCESS";
//    }
}
