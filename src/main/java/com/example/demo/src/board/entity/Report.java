package com.example.demo.src.board.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.board.model.GetPostRes;
import com.example.demo.src.board.model.GetReportRes;
import com.example.demo.src.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter // Entity에서는 Setter를 만들면 안됨!!!! => 별도의 메서드를 만들기!!
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "REPORT") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
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

    @Builder
    public Report(Post post, Comment comment, String reportContent) {
        this.post = post;
        this.comment = comment;
        this.reportContent = reportContent;
    }


    public GetReportRes toGetReportRes(Long reportIdx, String str){

        if (str.equals("게시글")) {
            return GetReportRes.builder()
                    .reportIdx(reportIdx)
                    .postIdx(post.getPostIdx())
                    .category(str)
                    .reportContent(reportContent)
                    .build();
        }

        return GetReportRes.builder()
                .reportIdx(reportIdx)
                .commentIdx(comment.getCommentIdx())
                .category(str)
                .reportContent(reportContent)
                .build();
    }



    /* 연관관계 메소드 & 기타 메소드*/

    public void deleteReport(){
        this.state = State.INACTIVE;
    }
    public void setPost(Post post) {
        this.post = post;
        post.getReports().add(this); // 가짜 매핑.
    }

    public void setComment(Comment comment) {
        this.comment = comment;
        comment.getReports().add(this); // 가짜 매핑.
    }
}
