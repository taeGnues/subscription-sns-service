package com.example.demo.src.user;



import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.pay.entity.Subscribe;
import com.example.demo.src.pay.repository.SubscribeRepository;
import com.example.demo.src.user.entity.User;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.common.entity.BaseEntity.State.ACTIVE;
import static com.example.demo.common.response.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final SubscribeRepository subscribeRepository;
    private final JwtService jwtService;


    /* 일반 유저 회원 가입 */
    @Transactional
    public PostUserRes createUser(PostUserReq postUserReq) {

        //중복 체크 (사용자 이름으로 확인)
        Optional<User> checkUser = userRepository.findByUserIDAndState(postUserReq.getUserID(), ACTIVE);
        if(checkUser.isPresent()){
            throw new BaseException(POST_USERS_EXISTS_USERID);
        }

        // 비밀번호 암호화
        String encryptPwd;
        try {
            encryptPwd = new SHA256().encrypt(postUserReq.getPassword());
            postUserReq.setPassword(encryptPwd);
        } catch (Exception exception) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        User saveUser = userRepository.save(postUserReq.toEntity());
        subscribeRepository.save(
                Subscribe.builder()
                        .user(saveUser)
                        .subsState(Subscribe.SubsState.UNSUBS)
                        .build()
        );
        return new PostUserRes(saveUser.getUserIdx());

    }

    /* OAuth2 유저 생성 */
    @Transactional
    public PostUserRes createOAuthUser(User user) {
        User saveUser = userRepository.save(user);

        subscribeRepository.save(
                Subscribe.builder()
                        .user(saveUser)
                        .subsState(Subscribe.SubsState.UNSUBS)
                        .build()
        );

        // JWT 발급
        String jwtToken = jwtService.createJwt(saveUser.getUserIdx());
        return new PostUserRes(saveUser.getUserIdx(), jwtToken);

    }

    /* 일반 유저 로그인 */
    @Transactional
    public PostLoginRes logIn(PostLoginReq postLoginReq) {
        User user = userRepository.findByUserIDAndState(postLoginReq.getUserID(), ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));

        String encryptPwd;
        try {
            encryptPwd = new SHA256().encrypt(postLoginReq.getPassword());
        } catch (Exception exception) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        // JWT 발급.
        if(user.getPassword().equals(encryptPwd)){
            Long userIdx = user.getUserIdx();
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(userIdx, jwt);
        } else{
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }
    @Transactional
    public void deleteUser(Long userIdx) {
        User user = userRepository.findByUserIdxAndState(userIdx, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        user.deleteUser();
    }

    /* 전체 회원 조회 */
    @Transactional(readOnly = true)
    public List<GetUserRes> getUsers() {
        List<GetUserRes> getUserResList = userRepository.findAllByState(ACTIVE).stream()
                .map(GetUserRes::new)
                .collect(Collectors.toList());
        return getUserResList;
    }

    /* userIdx로 유저 조회 */
    @Transactional(readOnly = true)
    public GetUserRes getUser(Long userIdx) {
        User user = userRepository.findByUserIdxAndState(userIdx, ACTIVE)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        return new GetUserRes(user);
    }

    /* userID(사용자 이름)으로 유저 존재여부 조회 */
    @Transactional(readOnly = true)
    public boolean checkUserByUserID(String userID) {

        Optional<User> checkUser = userRepository.findByUserID(userID);
        return checkUser.isPresent();
    }

    /* userID(사용자 이름)으로 유저 조회*/
    @Transactional(readOnly = true)
    public GetUserRes getUserByUserID(String userID) {
        User user = userRepository.findByUserIDAndState(userID, ACTIVE).orElseThrow(() -> new BaseException(NOT_FIND_USER));
        return new GetUserRes(user);
    }
    /* 전화번호로 유저 조회 */
    @Transactional(readOnly = true)
    public Long findUserByPhoneNum(String phoneNum) {
        User user = userRepository.findByPhoneNum(phoneNum).orElseThrow(()->new BaseException(NOT_FIND_USER));
        return user.getUserIdx();
    }

    /* 유저 idx로 해당하는 유저 전화번호 업데이트 */
    public String changePassword(PatchPwdReq patchPwdReq) {
        User user = userRepository.findById(patchPwdReq.getUserIdx()).orElseThrow(()->new BaseException(NOT_FIND_USER));

        // 암호화

        String encryptPwd;
        try {
            encryptPwd = new SHA256().encrypt(patchPwdReq.getPassword());
        } catch (Exception exception) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }

        user.updatePassword(encryptPwd);
        userRepository.save(user); // user 비밀번호 업데이트!!

        return "SUCCESS";
    }
}
