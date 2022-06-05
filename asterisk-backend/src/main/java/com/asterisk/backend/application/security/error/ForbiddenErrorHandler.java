package com.asterisk.backend.application.security.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ForbiddenErrorHandler implements AccessDeniedHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ForbiddenErrorHandler.class);

    @Override
    public void handle(final HttpServletRequest httpServletRequest,
                       final HttpServletResponse httpServletResponse,
                       final AccessDeniedException e) throws IOException {
        LOGGER.info("Forbidden request on protected resource {}", httpServletRequest.getServletPath());
        httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Error: Forbidden");
    }
}
