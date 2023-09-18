package com.example.demo.src.board.model;

import com.example.demo.src.board.entity.PostImgPath;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetCommentRes {

    private Long userIdx;
    private String userId;
    private String content;

    @Builder
    public GetCommentRes(Long userIdx, String userId, String content) {
        this.userIdx = userIdx;
        this.userId = userId;
        this.content = content;
    }
}
