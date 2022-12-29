package com.hangoutwithus.hangoutwithus.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntityCreatedOnly {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;
}
