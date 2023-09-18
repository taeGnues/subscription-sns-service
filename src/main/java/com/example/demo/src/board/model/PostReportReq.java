package com.example.demo.src.board.model;

import com.example.demo.src.board.entity.Comment;
import com.example.demo.src.board.entity.Post;
import com.example.demo.src.board.entity.Report;
import com.example.demo.src.user.entity.User;
import com.google.firebase.database.core.Repo;
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
public class PostReportReq {

    private Long postIdx;

    private Long commentIdx;

    @NotBlank(message = "신고 내역이 필요합니다.")
    private String reportContent;

    public Report toEntity(Comment comment, Post post) {

        return Report.builder()
                .post(post)
                .comment(comment)
                .reportContent(reportContent)
                .build();
    }


}
