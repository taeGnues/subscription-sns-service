package com.example.demo.src.board.repositories;

import com.example.demo.src.board.entity.Post;
import com.example.demo.src.board.entity.Report;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import static com.example.demo.common.entity.BaseEntity.State;

public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query("select r from Report r where r.state = 'ACTIVE' order by r.createdAt ASC")
    List<Report> findReports(Pageable pageable);

    Optional<Report> findByReportIdxAndState(Long reportIdx, State state);
}
