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

    @Column(nullable = false)
    private String slug;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column
    private String addressLine1;

    @Column
    private String addressLine2;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String postalCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryCode", referencedColumnName = "countryCode")
    private Country country;

    @Transient
    private String countryCode;

    @Column
    private String website;

    @Column
    private String companyID;

    @Column
    private String jurisdictionCode;

    @Column
    private String industryType;

    @Column
    private String companyType;

    @Column
    private String companySize;

    @Column
    private String fbProfileUrl;

    @Column
    private String twitterProfileUrl;

    @Column
    private String linkedInProfileUrl;

    @Column
    private String gstInNumber;

    @Column
    private String officeEmail;

    @Column
    private String phoneNUmber;

    @Column(nullable = false)
    private String contactNumber;

    // One-to-many relationship with HostUser
    @OneToMany(mappedBy = "host", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HostUser> hostUsers = new ArrayList<>();

}
