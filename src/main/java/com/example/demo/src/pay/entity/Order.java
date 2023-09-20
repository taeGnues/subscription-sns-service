package com.example.demo.src.pay.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.board.entity.Comment;
import com.example.demo.src.board.entity.PostImgPath;
import com.example.demo.src.board.entity.Report;
import com.example.demo.src.board.model.GetPostRes;
import com.example.demo.src.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter // Entity에서는 Setter를 만들면 안됨!!!! => 별도의 메서드를 만들기!!
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "ORDERED") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class Order extends BaseEntity {

    @Id // PK를 의미하는 어노테이션
    @Column(name = "ORDER_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderIdx;

    // 조회 시 해당연관 객체 정보까지 다가져올지 결정. EAGER이면 가져오고, LAZY는 안가져옴.(이때는, jpql의 fetch문법 사용하기)
    @JsonIgnore // json 만들때 post-user 간 무한루프 제거
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID") // 다대일
    private User user; // 이 게시글을 작성한 회원
    // 진짜 매핑 - 연관관계의 주인!!
    // 연관관계 주인 => USER_ID . 연관관계 주인의 데이터가 바뀔때 JPA에서 데이터베이스에서 반영. ( 기준 )

    @Column(name="amount")
    private int amount; // 지불 비용

    @Builder
    public Order(User user, int amount){
        this.user = user;
        this.amount = amount;
    }


    public void deleteOrder() {
        this.state = State.INACTIVE;
    }

}
