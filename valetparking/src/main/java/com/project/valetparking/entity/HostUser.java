package com.project.valetparking.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EqualsAndHashCode(callSuper = true)
@Entity(name = "host_user")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HostUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hostUserId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hostId", nullable = false, referencedColumnName = "hostId")
    private Host host;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId", nullable = false, referencedColumnName = "roleId")
    private UserRole role;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column
    private String middleName;

    @Column
    private String lastName;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private Boolean isEmailVerified;

    @Column(nullable = false)
    private Boolean isApproved;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private String contactNumber;

    @Column(nullable = false)
    private String dlNumber;

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
    //todo: add driving licence id proof along w address
    // near future: validate driving licence. validate cin, gst number number of host

}
