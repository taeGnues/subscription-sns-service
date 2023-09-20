package com.example.demo.src.board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatchPostReq {
    @NotNull(message = "유저 Idx값을 입력해주세요.")
    private Long userIdx;

    @NotBlank(message = "게시물 내용을 작성해주세요.")
    private String content;
}
