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
public class GetPaymentSubsRes {

    private Long userIdx;
    private String userID;
    private String name;
    private LocalDate subsStartAt;
    private LocalDate subsEndAt;
    private Subscribe.SubsState subsState;

}
