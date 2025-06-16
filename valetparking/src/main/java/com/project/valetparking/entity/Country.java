package com.project.valetparking.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "country")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Data
@Builder
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
