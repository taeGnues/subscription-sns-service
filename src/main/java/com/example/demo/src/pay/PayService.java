package com.example.demo.src.pay;



import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.board.entity.Comment;
import com.example.demo.src.board.entity.Post;
import com.example.demo.src.board.entity.Report;
import com.example.demo.src.board.model.*;
import com.example.demo.src.board.repositories.CommentRepository;
import com.example.demo.src.board.repositories.PostRepository;
import com.example.demo.src.board.repositories.ReportRepository;
import com.example.demo.src.pay.entity.Order;
import com.example.demo.src.pay.entity.Subscribe;
import com.example.demo.src.pay.model.GetPaymentSubsRes;
import com.example.demo.src.pay.model.PostPaymentReq;
import com.example.demo.src.pay.repository.OrderRepository;
import com.example.demo.src.pay.repository.SubscribeRepository;
import com.example.demo.src.user.UserRepository;
import com.example.demo.src.user.entity.User;
import com.example.demo.utils.JwtService;
import com.siot.IamportRestClient.response.IamportResponse;
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
public class PayService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final SubscribeRepository subscribeRepository;
    private final OrderRepository orderRepository;

    /* 결제 성공 후 구독 진행 */
    @Transactional
    public void createPayment(PostPaymentReq postPaymentReq) {

        try {
            Long userIdx = postPaymentReq.getUserIdx();
            // 유저 존재 확인
            User user = userRepository.findByUserIdxAndState(userIdx, ACTIVE)
                    .orElseThrow(() -> new BaseException(NOT_FIND_USER));

            // 해당 유저가 구독 중인지 확인
            Subscribe subs = subscribeRepository.findSubsByUserIdx(userIdx)
                    .orElseThrow(() -> new BaseException(NOT_FIND_USER));

            if(subs.getSubsState() == Subscribe.SubsState.SUBS){
                // 만약 구독중이었다면, 결제 취소
            }

            // 실제 결제 금액이 알맞는지 확인

            // 구독 진행
            subs.activateSubscribe();
            subscribeRepository.save(subs); // 구독
            orderRepository.save(Order
                    .builder()
                    .user(user)
                    .amount(postPaymentReq.getAmount())
                    .build()
            ); // 결제 정보 저장


        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BaseException(PAY_SUBS_SAVE_FAILED);

        }
    }

    /* 구독 취소 */
    @Transactional
    public void unactivatedSubs(Long userIdx) {
        Subscribe subs = subscribeRepository.findSubsByUserIdx(userIdx)
                .orElseThrow(() -> new BaseException(NOT_FIND_USER));
        subs.unactivateSubscribe();
    }

    /* 구독 조회 (전체) */
    @Transactional(readOnly = true)
    public List<GetPaymentSubsRes> readAllSubscriptionHistory(int pageNo, int pageSize) {
        List<GetPaymentSubsRes> getPaymentSubsResList = new ArrayList<>();

        try {
            List<GetPaymentSubsRes> res = subscribeRepository.findAllSubs(PageRequest.of(pageNo, pageSize));

            return res;
        }catch (BaseException e){
            e.printStackTrace();
            throw new BaseException(PAY_EMPTY_SUBS);
        }
    }

}
