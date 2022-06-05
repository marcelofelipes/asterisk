package com.asterisk.backend.application.common;

import com.asterisk.backend.domain.User;
import com.asterisk.backend.store.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserManager userManager;

    @Autowired
    public UserDetailsServiceImpl(final UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = this.userManager.findUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Did not find User for email: " + username);
        }
        return UserDetailsImpl.of(user);
    }
}
