package com.example.demo.src.board;

import com.example.demo.src.board.entity.Post;
import com.example.demo.src.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import static com.example.demo.common.entity.BaseEntity.State;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByPostIdxAndState(Long postIdx, State state);

    @Query("select p from Post p where p.user.userIdx <> :userIdx")
    List<Post> findOtherPosts(@Param("userIdx") Long userIdx, Pageable pageable);

    @Query("select p from Post p where p.user.userIdx = :userIdx")
    List<Post> findMyPosts(@Param("userIdx") Long userIdx, Pageable pageable);
}
