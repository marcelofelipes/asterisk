package com.asterisk.backend.store.user.confirmation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RegisterConfirmationTokenRepository extends JpaRepository<RegisterConfirmationTokenEntity, UUID> {
}
