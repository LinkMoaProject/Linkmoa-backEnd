package com.linkmoa.source.auth.annotation;


import com.linkmoa.source.auth.security.WithMockUserSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String userEmail() default "testUser@google.com";
    String userPassword() default "password";
    String userRole() default "ROLE_USER";

}
