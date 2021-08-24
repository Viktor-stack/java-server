package com.rujavacours.auth.repos;

import com.rujavacours.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepos extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
