package com.asterisk.backend._integration.authentication;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithAsteriskUserSecurityContextFactory.class)
public @interface WithAsteriskUser {

    String email() default "john@doe.com";;
}
