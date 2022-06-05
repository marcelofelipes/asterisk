package com.asterisk.backend.store.user;

import com.asterisk.backend.domain.User;

import java.util.UUID;

public interface UserManager {
    User findUserByEmail(String email);

    User save(User user);

    User findUserById(UUID userId);
}
