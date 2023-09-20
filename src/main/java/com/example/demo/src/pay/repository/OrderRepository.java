package com.example.demo.src.pay.repository;

import com.example.demo.src.board.entity.Comment;
import com.example.demo.src.pay.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import static com.example.demo.common.entity.BaseEntity.State;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
