package com.example.demo.src.board.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetReportRes {

    private Long reportIdx;
    private Long postIdx;
    private Long commentIdx;
    private String category;
    private String reportContent;

    @Builder
    public GetReportRes(Long reportIdx, Long postIdx, Long commentIdx, String reportContent, String category) {
        this.reportIdx = reportIdx;
        this.postIdx = postIdx;
        this.commentIdx = commentIdx;
        this.category = category;
        this.reportContent = reportContent;
    }
}
