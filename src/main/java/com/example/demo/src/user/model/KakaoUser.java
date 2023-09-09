package com.example.demo.src.user.model;

import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//카카오로 액세스 토큰을 보내 받아올 카카오에 등록된 사용자 정보
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoUser {
    public String id; // 회원 번호
    public KakaoAccount kakaoAccount;

    public User toEntity() {
        return User.builder()
                .password("NONE")
                .isOAuth(true)
                .build();
    }
}
