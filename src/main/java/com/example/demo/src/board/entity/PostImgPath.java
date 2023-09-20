package com.example.demo.src.board.entity;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.src.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = false)
@Getter // Entity에서는 Setter를 만들면 안됨!!!! => 별도의 메서드를 만들기!!
@Entity // 필수, Class 를 Database Table화 해주는 것이다
@Table(name = "POST_IMG_PATH") // Table 이름을 명시해주지 않으면 class 이름을 Table 이름으로 대체한다.
public class PostImgPath extends BaseEntity {

    @Id // PK를 의미하는 어노테이션
    @Column(name = "POST_IMG_PATH_ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postImgPathIdx;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID") // 다대일
    private Post post;

    @Column(nullable = false)
    private String imgPath;

    @Builder
    public PostImgPath(Post post, String imgPath) {
        this.imgPath = imgPath;

    }

    public void setPost(Post post) {
        this.post = post;
        post.getPostImgPaths().add(this); // 가짜 매핑.
    }

}
