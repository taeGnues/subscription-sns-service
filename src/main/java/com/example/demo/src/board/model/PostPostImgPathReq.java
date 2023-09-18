package com.example.demo.src.board.model;

import com.example.demo.src.board.entity.PostImgPath;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostPostImgPathReq {
    @JsonProperty("imgPath")
    private String imgPath;

    public PostImgPath toEntity() {
        return PostImgPath.builder()
                .imgPath(imgPath)
                .build();
    }
}
