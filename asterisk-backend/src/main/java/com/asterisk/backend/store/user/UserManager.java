package com.asterisk.backend.store.user;

import com.asterisk.backend.domain.User;
import com.asterisk.backend.infrastructure.exception.UserNotFoundException;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

public interface UserManager {

    /**
     * Attempts to find a {@code User} for a given {@code email}
     * @param email email of the user to find
     * @return {@code User} for {@code email}
     * @throws UserNotFoundException if no {@code User} is found
     */
    User findUserByEmail(String email) throws UserNotFoundException;

    /**
     * Saves a given {@code User}
     *
     * Updates the specific cache value of this user
     *
     * @param user to save
     * @return the updated {@code User instance}
     */
    User save(User user);

    /**
     * Attempts to find a {@code User} for a given {@code userId}
     *
     * After the user was successfully found it the object is cached for 10 minutes.
     *
     * @param userId id of the user to find
     * @return {@code User} for {@code userId}
     * @throws UserNotFoundException if no {@code User} is found
     */
    User findUserById(UUID userId) throws UserNotFoundException;
}
