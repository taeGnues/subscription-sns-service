package com.example.demo.src.user;

import com.example.demo.src.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import static com.example.demo.common.entity.BaseEntity.*;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserIdxAndState(Long userIdx, State state);
    Optional<User> findByUserIDAndState(String userID, State state);
    Optional<User> findByPhoneNum(String phoneNum);

    Optional<User> findByUserID(String userID);
    List<User> findAllByState(State state);

}
