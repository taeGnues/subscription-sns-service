package com.example.demo.src.user.model;


import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserRes {
    private Long userIdx;
    private String userId;
    private String name;

    public GetUserRes(User user) {
        this.userIdx = user.getUserIdx();
        this.userId = user.getUserID();
        this.name = user.getName();
    }
}
