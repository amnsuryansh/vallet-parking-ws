package com.project.valletparking.repository;
import com.project.valletparking.entity.Host;
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