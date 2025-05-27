package com.project.valetparking.entity;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.slf4j.MDC;

import java.io.Serializable;
import java.time.OffsetDateTime;

import static com.project.valetparking.constants.ValetParkingConstants.APP_NAME;
import static com.project.valetparking.constants.ValetParkingConstants.USER;

@MappedSuperclass
@Data
public class BaseEntity implements Serializable {

    @Column
    private String status = "A";

    @Column
    @CreationTimestamp
    private OffsetDateTime createdDate;

    @Column
    private String createdUser;

    @Column
    @UpdateTimestamp
    private String lastUpdatedDate;

    @Column
    private String lastUpdatedUser;

    @PreUpdate
    public void onUpdate() {
        lastUpdatedUser = getUser();
    }

    @PrePersist
    public void onPersist() {
        if (createdUser == null) {
            createdUser = getUser();
        }
        lastUpdatedUser = createdUser;
    }

    private String getUser() {
        return MDC.get(USER) != null ? MDC.get(USER) : APP_NAME;
    }

}
