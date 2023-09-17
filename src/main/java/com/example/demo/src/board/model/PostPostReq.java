package com.example.demo.src.board.model;

import com.example.demo.src.board.entity.Post;
import com.example.demo.src.board.entity.PostImgPath;
import com.example.demo.src.user.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostPostReq {

    @NotNull(message = "유저 Idx값을 입력해주세요.")
    private Long userIdx;

    @NotBlank(message = "게시물 내용을 작성해주세요.")
    private String content;

    @JsonProperty("postImgPaths")
    @NotNull(message = "게시물 이미지가 필요합니다.")
    private List<PostPostImgPathReq> postImgPaths;

    @Override
    public String toString() {
        return "PostPostReq{" +
                "userIdx=" + userIdx +
                ", content='" + content + '\'' +
                ", postImgPaths=" + postImgPaths +
                '}';
    }

    public Post toEntity(User user) {
        List<PostImgPath> postImgPathLists = new ArrayList<>();

        for(PostPostImgPathReq postPostImgPathReq : this.postImgPaths){
            postImgPathLists.add(postPostImgPathReq.toEntity());
        }

        return Post.builder()
                .user(user)
                .content(content)
                .postImgPaths(postImgPathLists)
                .build();
    }
}
