package com.example.demo.src.board.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetUserFeedCommentRes {
    private List<GetCommentRes> getCommentResList;
    private int pageNo;
    private int pageSize;
}
