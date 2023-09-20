package com.example.demo.src.board.model;

import com.example.demo.src.board.entity.Comment;
import com.example.demo.src.board.entity.Post;
import com.example.demo.src.board.entity.PostImgPath;
import com.example.demo.src.user.entity.User;
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
public class PostCommentReq {

    @NotNull(message = "사용자 정보가 필요합니다.")
    private Long userIdx;

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

    public Comment toEntity(User user, Post post) {

        return Comment.builder()
                .post(post)
                .user(user)
                .content(content)
                .build();
    }


}
