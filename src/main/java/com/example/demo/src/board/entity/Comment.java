package com.example.demo.src.board.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter // Entity에서는 Setter를 만들면 안됨!!!! => 별도의 메서드를 만들기!!
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "COMMENT") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class Comment extends BaseEntity {

    @Id // PK를 의미하는 어노테이션
    @Column(name = "COMMENT_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentIdx;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID") // 다대일
    private Post post;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID") // 다대일
    private User user;


    @Column(nullable = false, length = 100)
    private String content;

    @JsonIgnore
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<Report> reports = new ArrayList<Report>(); // 신고 내역

    public void setPost(Post post) {
        this.post = post;
        post.getComments().add(this); // 가짜 매핑.
    }

    public void addReport(Report report){
        reports.add(report);
        report.setComment(this);
    }

}

