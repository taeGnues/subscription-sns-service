package com.example.demo.src.user.model;

import com.example.demo.src.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostUserReq {

    private String phoneNum;

    private String name;

    private String userID;

    private String password;

    private LocalDate birthDate; // 생년월일


    public User toEntity() {
        return User.builder()
                .phoneNum(this.phoneNum)
                .name(this.name)
                .userID(this.userID)
                .password(this.password)
                .birthDate(this.birthDate)
                .userStatus(User.UserStatus.ACTIVE)
                .lastestLoginAt(new Timestamp(System.currentTimeMillis()))
                .build();
    }
}
