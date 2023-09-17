package com.example.demo.src.board.model;

import com.example.demo.src.board.entity.PostImgPath;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetPostRes {

    private Long postIdx;
    private Long userIdx;
    private String userName;
    private String content;
    private List<PostImgPath> imgPaths;

    @Builder
    public GetPostRes(Long postIdx, Long userIdx, String userName, String content, List<PostImgPath> imgPaths) {
        this.postIdx = postIdx;
        this.userIdx = userIdx;
        this.userName = userName;
        this.content = content;
        this.imgPaths = imgPaths;
    }
}
