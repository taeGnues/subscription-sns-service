package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


//구글에 일회성 코드를 다시 보내 받아올 액세스 토큰을 포함한 JSON 문자열을 담을 클래스
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoOAuthToken {
    private String token_type; // 토큰 타입, bearer로 고정
    private String access_token; // 사용자 토큰 값
    private String refresh_token; // 사용자 리프레시 토큰 값
    private int refresh_token_expires_in; // 리프레시 토큰 만료 시간(초)
    private int expires_in; // 액세스 토큰과 ID 토큰의 만료 시간
    // private String id_token;
    // private String scope;
}
