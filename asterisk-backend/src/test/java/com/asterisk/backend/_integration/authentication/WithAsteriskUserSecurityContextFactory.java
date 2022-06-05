package com.asterisk.backend._integration.authentication;

import com.asterisk.backend._factory.UserTestFactory;
import com.asterisk.backend.application.common.UserDetailsImpl;
import com.asterisk.backend.mapper.UserMapper;
import com.asterisk.backend.store.user.UserEntity;
import com.asterisk.backend.store.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

@Component
public class WithAsteriskUserSecurityContextFactory implements WithSecurityContextFactory<WithAsteriskUser> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public WithAsteriskUserSecurityContextFactory(final UserRepository userRepository, final PasswordEncoder passwordEncoder, final UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    public SecurityContext createSecurityContext(final WithAsteriskUser customUser) {
        final SecurityContext context = SecurityContextHolder.createEmptyContext();

        final String password = "passwordpassword";
        UserEntity user = new UserTestFactory()
                .setEmail(customUser.email())
                .setPassword(this.passwordEncoder.encode(password))
                .newUserEntity();
        user = this.userRepository.save(user);
        final UserDetailsImpl principal = UserDetailsImpl.of(this.userMapper.toUser(user));
        final Authentication auth =
                new UsernamePasswordAuthenticationToken(principal, password, principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
