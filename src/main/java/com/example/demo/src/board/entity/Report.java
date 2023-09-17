package com.example.demo.src.board.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter // Entity에서는 Setter를 만들면 안됨!!!! => 별도의 메서드를 만들기!!
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "REPORT_ID") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class Report extends BaseEntity {

    @Id // PK를 의미하는 어노테이션
    @Column(name = "REPORT_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportIdx;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID") // 다대일
    private Post post;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMMENT_ID") // 다대일
    private Comment comment;

    @Column(nullable = false, length = 45)
    private String reportContent; // 신고한 이유(신고내용)

    /* 연관관계 메소드 */
    public void setPost(Post post) {
        this.post = post;
        post.getReports().add(this); // 가짜 매핑.
    }

    public void setComment(Comment comment) {
        this.comment = comment;
        comment.getReports().add(this); // 가짜 매핑.
    }
}
