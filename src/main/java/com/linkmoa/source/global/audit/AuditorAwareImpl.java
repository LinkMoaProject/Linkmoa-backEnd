package com.linkmoa.source.global.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;

public class AuditorAwareImpl implements AuditorAware<Long> {

	@Override
	public Optional<Long> getCurrentAuditor() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (null == authentication || !authentication.isAuthenticated()) {
			return null;
		}

		PrincipalDetails userDetails = (PrincipalDetails)authentication.getPrincipal();

		return Optional.of(userDetails.getId());
	}
}
