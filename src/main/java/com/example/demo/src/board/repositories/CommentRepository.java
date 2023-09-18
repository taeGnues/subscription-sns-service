package com.example.demo.src.board.repositories;

import com.example.demo.src.board.entity.Comment;
import com.example.demo.src.board.entity.Post;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import static com.example.demo.common.entity.BaseEntity.State;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByCommentIdxAndState(Long commentIdx, State state);
    @Query("select c from Comment c where c.post.postIdx = :postIdx")
    List<Comment> findCommentsByPostIdx(@Param("postIdx") Long postIdx, Pageable pageable);
}
