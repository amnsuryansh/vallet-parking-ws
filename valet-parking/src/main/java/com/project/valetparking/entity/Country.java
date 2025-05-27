package com.project.valletparking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "country")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Country extends BaseEntity {

    @Id
    private String countryCode;

    @Column(unique = true, nullable = false)
    private String countryName;

    @Column
    private String nationality;

    @Column
    private String isoCode;

    @Column
    private String dialCode;

    // add jurisdictions - in india it will be states

}
