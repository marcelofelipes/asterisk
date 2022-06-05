package com.asterisk.backend.application.security.filter;

import com.asterisk.backend.application.common.UserDetailsImpl;
import com.asterisk.backend.application.common.UserDetailsServiceImpl;
import com.asterisk.backend.application.security.jwt.JwtHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
public class AccessTokenFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessTokenFilter.class);
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtHelper jwtHelper;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public AccessTokenFilter(final JwtHelper jwtHelper,
                           final UserDetailsServiceImpl userDetailsService) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull final HttpServletRequest request,
                                    @NonNull final HttpServletResponse response,
                                    @NonNull final FilterChain filterChain) throws ServletException, IOException {
        final String accessJwt = this.parseBearer(request);
        final String fingerprintCookie = this.parseCookie(request);
        if (accessJwt != null && fingerprintCookie != null && this.jwtHelper.validateAccessJwt(accessJwt, fingerprintCookie)) {
            final String email = this.jwtHelper.getEmailFromAccessJwt(accessJwt);
            final UserDetailsImpl principal = (UserDetailsImpl) this.userDetailsService.loadUserByUsername(email);
            LOGGER.info("Granted Access for Method {} | Path {} | User {} | Roles {}",
                    request.getMethod(), request.getServletPath(), principal.getEmail(), principal.getAuthorities());
            final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null,
                    principal.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Attempts to parse the fingerprint cookie from the request
     *
     * @param httpServletRequest the incoming http request
     * @return the value of the cookie if found, else null
     */
    private String parseCookie(final HttpServletRequest httpServletRequest) {
        final Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        final Cookie fgp = Arrays.stream(httpServletRequest.getCookies())
                .filter(cookie -> cookie.getName().equals("fgp"))
                .findFirst()
                .orElse(null);
        if (fgp == null || fgp.getValue() == null) {
            return null;
        }
        return fgp.getValue();

    }

    /**
     * Parses the access jwt from the authorization header
     *
     * @param request Incoming http request
     * @return Value extracted from header or null if no token was found
     */
    private String parseBearer(final HttpServletRequest request) {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith(BEARER_PREFIX)) {
            return authorizationHeader.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}
