package com.example.demo.src.board.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAllReportRes {
    private List<GetReportRes> getReportResList;
    private int pageNo;
    private int pageSize;
}
