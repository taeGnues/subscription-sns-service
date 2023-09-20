package com.example.demo.src.pay;


import com.example.demo.common.exceptions.BaseException;
import com.example.demo.common.firebase.FireBaseService;
import com.example.demo.common.response.BaseResponse;
import com.example.demo.src.board.PostService;
import com.example.demo.src.board.model.*;
import com.example.demo.src.pay.model.GetPaymentSubsRes;
import com.example.demo.src.pay.model.PostPaymentReq;
import com.example.demo.utils.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.example.demo.common.response.BaseResponseStatus.BOARD_REPORT_UNKNOWN;
import static com.example.demo.common.response.BaseResponseStatus.SUCCESS;
@CrossOrigin
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/payment")
@Tag(name="Pay", description = "Pay API")
public class PayController {


    private final PayService payService;

    private final JwtService jwtService;

    private final FireBaseService fireBaseService;


    /* 결제 검증 및 성공 시 구독 진행 API */

    @ResponseBody // BODY
    @PostMapping("/validate")
    @Tag(name="Pay")
    @Operation(summary = "주문 내역 및 구독 등록", description = "결제 성공 후 주문 내역과 구독 상태를 등록한다.")
    public BaseResponse<String> createPayment(@Valid @RequestBody PostPaymentReq postPaymentReq) {
        // 형식적 데이터 검사.
        try{

            payService.createPayment(postPaymentReq);
            return new BaseResponse<>(SUCCESS);

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }

    /* 구독 취소 API */
    @PatchMapping("/{userIdx}")
    @Tag(name="Pay")
    @Operation(summary = "구독 취소", description = "구독 상태를 취소로 변경한다.")
    public BaseResponse<String> unactivatedSubs(@PathVariable Long userIdx){
        try{

            payService.unactivatedSubs(userIdx);

            return new BaseResponse<>(SUCCESS);

        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


    /* ADMIN : 구독 조회 API */
    @ResponseBody
    @GetMapping("/admin/subs")
    @Tag(name="Pay")
    @Operation(summary = "회원 구독 내역 조회", description = "구독 상태를 취소로 변경한다.")
    public BaseResponse<List<GetPaymentSubsRes>> readAllSubscriptionHistory(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "3", required = false) int pageSize){
        try {
            List<GetPaymentSubsRes> getPaymentSubsRes = payService.readAllSubscriptionHistory(pageNo, pageSize);
            return new BaseResponse<>(getPaymentSubsRes);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }


}
