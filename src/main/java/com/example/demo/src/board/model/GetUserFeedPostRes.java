package com.example.demo.src.board.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetUserFeedPostRes {
    private List<GetPostRes> getPostResList;
    private int pageNo;
    private int pageSize;
}
