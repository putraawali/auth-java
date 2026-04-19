package com.putraawali.auth.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.putraawali.auth.entity.Session;

import jakarta.persistence.LockModeType;

public interface SessionRepository extends JpaRepository<Session, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Session findByTokenHash(String tokenHash);

    @Transactional
    @Modifying
    @Query("UPDATE Session s SET s.isRevoked = true WHERE s.sessionId = :sessionId")
    int revokeAllBySessionId(@Param("sessionId") UUID sessionId);

    @Transactional
    @Modifying
    @Query("UPDATE Session s SET s.isRevoked = true WHERE s.userId = :userId")
    int revokeAllByUserId(@Param("userId") Long userId);
}
