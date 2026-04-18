package com.putraawali.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.putraawali.auth.entity.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Session findByTokenHash(String tokenHash);
}
