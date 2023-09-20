package com.example.demo.src.pay.model;

import com.example.demo.src.pay.entity.Subscribe;
import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostPaymentReq {

    @NotNull(message = "사용자 정보를 입력해주세요.")
    private Long userIdx;

    @NotNull(message = "결제고유번호(imp_uid)를 입력해주세요.")
    private String imp_uid; // 결제 고유 번호

    @NotNull(message = "주문 번호(rsp.merchant_uid)를 입력해주세요.")
    private String merchant_uid; // 주문 번호

    @NotNull(message = "결제 금액을 입력해주세요.")
    private int amount; // 결제 금액

}
