package com.example.demo.src.pay.repository;

import com.example.demo.src.board.entity.Comment;
import com.example.demo.src.pay.entity.Subscribe;
import com.example.demo.src.pay.model.GetPaymentSubsRes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import static com.example.demo.common.entity.BaseEntity.State;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {
    @Query("select s from Subscribe s where s.user.userIdx = :userIdx")
    Optional<Subscribe> findSubsByUserIdx(@Param("userIdx") Long userIdx);

    @Query("select new com.example.demo.src.pay.model.GetPaymentSubsRes(u.userIdx, u.userID, u.name, s.subsStartAt, s.subsEndAt, s.subsState) from Subscribe s join s.user u")
    List<GetPaymentSubsRes> findAllSubs(Pageable pageable);

}
