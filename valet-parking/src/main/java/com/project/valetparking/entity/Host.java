package com.project.valletparking.entity;


import com.project.valletparking.enums.HostType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


/**
 * Entity representing a Host (company/organization)
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "host")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Host extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hostId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private HostType hostType;

    @Column(nullable = false)
    private String hostName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String addressLine1;

    @Column
    private String addressLine2;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String postalCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryCode", referencedColumnName = "countryCode")
    private Country country;

    @Transient
    private String countryCode;

    @Column
    private String website;

    @Column
    private Long hostSize;

    @Column
    private String instagramProfileUrl;

    @Column
    private String twitterProfileUrl;

    @Column
    private String linkedInProfileUrl;

    @Column
    private String gstInNumber;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private Long parkingSlots;

    // One-to-many relationship with HostUser
    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HostUser> hostUsers = new ArrayList<>();

}
