package com.example.demo.common.entity;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.sql.Timestamp;
import java.time.LocalDateTime;


@Getter
@MappedSuperclass
public class BaseEntity {

    @CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt", nullable = false)
    private Timestamp updatedAt;

    @Enumerated(EnumType.STRING) // ENUM 타입을 STRING으로 저장함!!
    @Column(name = "state", nullable = false, length = 10)
    protected State state = State.ACTIVE;

    public enum State {
        ACTIVE, INACTIVE;
    }
}
