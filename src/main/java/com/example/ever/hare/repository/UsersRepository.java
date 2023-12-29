package com.example.ever.hare.repository;

import com.example.ever.hare.entity.Role;
import com.example.ever.hare.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    @Query("SELECT u FROM Users u WHERE u.session.id = :sessionId AND u.role = :role")
    Users findUsersBySessionIdAndRole(@Param("sessionId") UUID uuid, @Param("role") Role role);

    Set<Users> findUsersBySessionId(UUID sessionId);
}
