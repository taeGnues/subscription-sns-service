package com.example.demo.src.pay.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.board.entity.Comment;
import com.example.demo.src.board.entity.PostImgPath;
import com.example.demo.src.board.entity.Report;
import com.example.demo.src.board.model.GetPostRes;
import com.example.demo.src.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.vm.ci.meta.Local;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter // Entity에서는 Setter를 만들면 안됨!!!! => 별도의 메서드를 만들기!!
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "SUBSCRIBE") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class Subscribe extends BaseEntity {

    @Id // PK를 의미하는 어노테이션
    @Column(name = "SUBSCRIBE_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subsIdx;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private User user;

    @Column(nullable = true)
    private LocalDate subsStartAt;

    @Column(nullable = true)
    private LocalDate subsEndAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SubsState subsState;

    @Builder
    public Subscribe(User user, LocalDate subsStartAt, LocalDate subsEndAt, SubsState subsState) {
        this.user = user;
        this.subsStartAt = subsStartAt;
        this.subsEndAt = subsEndAt;
        this.subsState = subsState;
    }

    /* 메소드 */

    public void deleteSubscribe() {
        this.state = State.INACTIVE;
    }
    public void activateSubscribe() {
        this.subsState = SubsState.SUBS;
        this.subsStartAt = LocalDate.now();
        this.subsEndAt = LocalDate.now().plus(30, ChronoUnit.DAYS);

    }
    public void unactivateSubscribe() {
        this.subsState = SubsState.UNSUBS;
        this.subsStartAt = null;
        this.subsEndAt = null;
    }



    public enum SubsState{
        UNSUBS, SUBS
    }

}
