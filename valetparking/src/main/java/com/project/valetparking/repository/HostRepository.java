package com.project.valetparking.repository;
import com.project.valetparking.entity.Host;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Host entity
 */
@Repository
public interface HostRepository extends JpaRepository<Host, Long> {
    Optional<Host> findByUserName(String userName);
    boolean existsByUserName(String userName);
}