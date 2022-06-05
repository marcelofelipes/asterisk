package com.asterisk.backend.store.user.forgotpassword;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ForgotPasswordTokenRepository extends JpaRepository<ForgotPasswordTokenEntity, UUID> {
}
