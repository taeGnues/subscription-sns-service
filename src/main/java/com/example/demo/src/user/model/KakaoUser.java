package com.example.demo.src.user.model;

import com.example.demo.src.user.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

//카카오로 액세스 토큰을 보내 받아올 카카오에 등록된 사용자 정보
@Getter
@Setter
public class KakaoUser {
    private String id; // 회원 번호

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @Setter
    public static class KakaoAccount {
        private String email;
        private Profile profile;
        private String birthday;
    }

    @Getter
    @Setter
    public static class Profile {
        private String nickname;
        @JsonProperty("profile_image_url")
        private String profileImageUrl;
    }



    public User toEntity() {
        return User.builder()
                .userID(kakaoAccount.email)
                .phoneNum("NONE")
                .birthDate(LocalDate.now())
                .name(kakaoAccount.profile.nickname)
                .lastestLoginAt(new Timestamp(System.currentTimeMillis()))
                .userStatus(User.UserStatus.ACTIVE)
                .password("NONE")
                .build();
    }
}
