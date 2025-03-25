package com.linkmoa.source.domain.member.entity;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.member.constant.Role;

public class WithMockPrincipalDetailsSecurityContextFactory
	implements WithSecurityContextFactory<WithMockPrincipalDetails> {
	@Override
	public SecurityContext createSecurityContext(WithMockPrincipalDetails annotation) {

		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

		Member member = Member.builder()
			.email(annotation.email())
			.providerId("google")
			.provider("12345")
			.role(Role.USER)
			.build();

		PrincipalDetails principalDetails = new PrincipalDetails(member);

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
			principalDetails, null, principalDetails.getAuthorities());

		securityContext.setAuthentication(authenticationToken);

		return null;
	}
}
