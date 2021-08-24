package com.rujavacours.auth.repos;

import com.rujavacours.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepos extends JpaRepository<User, Long> {
    @Query("select case when count(u)> 0 then true else false end from User u where Lower(u.email) = Lower(:email)")
    boolean existsByEmail(@Param("email") String email);

    @Query("select case when count(u)> 0 then true else false end from User u where Lower(u.username) = Lower(:username)")
    boolean existsByUsername(@Param("username") String username);

    Optional<User> findByUsername(String username);

    // используем обертку Optional - контейнер, который хранит значение или null - позволяет избежать ошибки NullPointerException
    Optional<User> findByEmail(String email); // поиск по email


    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password WHERE u.username = :username")
    int updatePassword(@Param("password") String password, @Param("username") String username);

}
