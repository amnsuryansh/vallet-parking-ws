package com.project.valletparking.repository;

import com.project.valletparking.entity.Host;
import com.project.valletparking.entity.HostUser;
import com.project.valletparking.enums.HostUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for HostUser entity
 */
@Repository
public interface HostUserRepository extends JpaRepository<HostUser, Long> {
    Optional<HostUser> findByUserName(String userName);
    List<HostUser> findByHost(Host host);
    List<HostUser> findByHostAndRole(Host host, HostUserRole role);
    boolean existsByUserName(String userName);
}
