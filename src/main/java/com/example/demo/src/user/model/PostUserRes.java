package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUserRes {
    private Long userIdx;
    private String jwt;

    public PostUserRes(Long userIdx) {
        this.userIdx = userIdx;
    }
}
