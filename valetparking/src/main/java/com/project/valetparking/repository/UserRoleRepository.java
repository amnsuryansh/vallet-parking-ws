package com.project.valetparking.repository;

import com.project.valetparking.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Serializable> {
    List<UserRole> findAll();
    UserRole findByRoleId(int roleId);
    UserRole findByDisplayNameAndStatus(String displayName, String status);
}
