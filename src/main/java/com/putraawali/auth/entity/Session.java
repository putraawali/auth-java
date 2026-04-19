package com.putraawali.auth.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private UUID sessionId;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "token_hash", nullable = false)
    private String tokenHash;

    @Column(name = "absolute_expiration", nullable = false)
    private LocalDateTime absoluteExpiration;

    @Column(name = "is_revoked")
    private Boolean isRevoked = false;

    // Lifecycle hooks
    @PrePersist
    public void prePersist() {
        if (this.absoluteExpiration == null) {
            this.absoluteExpiration = LocalDateTime.now().plusWeeks(2);
        }
    }
}
