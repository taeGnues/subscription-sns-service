package com.example.demo.src.board.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.board.model.GetPostRes;
import com.example.demo.src.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter // Entity에서는 Setter를 만들면 안됨!!!! => 별도의 메서드를 만들기!!
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "POST") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class Post extends BaseEntity {

    @Id // PK를 의미하는 어노테이션
    @Column(name = "POST_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postIdx;

    // 조회 시 해당연관 객체 정보까지 다가져올지 결정. EAGER이면 가져오고, LAZY는 안가져옴.(이때는, jpql의 fetch문법 사용하기)
    @JsonIgnore // json 만들때 post-user 간 무한루프 제거
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID") // 다대일
    private User user; // 이 게시글을 작성한 회원
    // 진짜 매핑 - 연관관계의 주인!!
    // 연관관계 주인 => USER_ID . 연관관계 주인의 데이터가 바뀔때 JPA에서 데이터베이스에서 반영. ( 기준 )

    @Column(nullable = false, length = 2200)
    private String content;

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostImgPath> postImgPaths = new ArrayList<PostImgPath>(); // 이미지

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<Comment>(); // 댓글

    @JsonIgnore
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Report> reports = new ArrayList<Report>(); // 신고 내역

    @Builder
    public Post(User user, String content, List<PostImgPath> postImgPaths) {
        for(PostImgPath postImgPath : postImgPaths){
           postImgPath.setPost(this); // post_id 설정
           // postImgPaths.add(postImgPath);
        }

        this.user = user;
        this.content = content;
        this.postImgPaths = postImgPaths;
    }

    public GetPostRes toGetPostRes(){
        return GetPostRes.builder()
                .postIdx(postIdx)
                .userName(user.getName())
                .imgPaths(postImgPaths)
                .userIdx(user.getUserIdx())
                .content(content)
                .build();
    }


    /* 연관관계 메소드 */


    public void updatePost(String content){
        this.content = content;
    }

    public void deletePost() {
        this.state = State.INACTIVE;
    }
    public void setUser(User user) {
        this.user = user;
        user.getPosts().add(this); // 가짜 매핑.
    }

    public void addComment(Comment comment){
        comments.add(comment);
        comment.setPost(this);
    }

    public void addReport(Report report){
        reports.add(report);
        report.setPost(this);
    }


}
