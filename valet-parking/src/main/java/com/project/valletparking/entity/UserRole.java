package com.project.valletparking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "kychub_user_role")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleId;

    @Column(unique = true)
    private String slug;

    @Column
    private String displayName;

    @Column
    private String description;

    @Override
    public String toString() {
        return "UserRole{" +
                "description='" + description + '\'' +
                ", displayName='" + displayName + '\'' +
                ", slug='" + slug + '\'' +
                ", roleId=" + roleId +
                '}';
    }
}

