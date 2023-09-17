package com.example.demo.src.user.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.board.entity.Post;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter // Entity에서는 Setter를 만들면 안됨!!!! => 별도의 메서드를 만들기!!
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "USER") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class User extends BaseEntity {

    @Id // PK를 의미하는 어노테이션
    @Column(name = "USER_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(nullable = false, length = 11)
    private String phoneNum;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(nullable = false, length = 100)
    private String userID;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate birthDate; // 생년월일

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus userStatus; // 유저 계정 상태

    @Column(nullable = false)
    private Timestamp lastestLoginAt; // 마지막 로그인 일시

    @OneToMany(mappedBy = "user") // 단방향 매핑 (연관관계 주인은 USER table, 한명의 user가 여러개의 posts를 가짐.)
    private List<Post> posts = new ArrayList<Post>(); // 지금까지 작성한 글 목록.
    // 가짜 매핑 - 주인의 반대편. 등록 수정은 불가..! 값을 조회만 가능

    @Builder
    public User(Long userIdx, String phoneNum, String name, String userID, String password, LocalDate birthDate, UserStatus userStatus, Timestamp lastestLoginAt) {
        this.userIdx = userIdx;
        this.phoneNum = phoneNum;
        this.name = name;
        this.userID = userID;
        this.password = password;
        this.birthDate = birthDate;
        this.userStatus = userStatus;
        this.lastestLoginAt = lastestLoginAt;
    }

    public void deleteUser() {
        this.state = State.INACTIVE;
    }
    public void updatePassword(String password) {this.password = password; }

    public enum UserStatus {
        ACTIVE, INACTIVE, BLOCKED
    }
}
