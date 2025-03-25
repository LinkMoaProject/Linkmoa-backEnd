package com.linkmoa.source.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.linkmoa.source.global.audit.AuditorAwareImpl;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
	@Bean
	public AuditorAware<Long> auditorProvier() {
		return new AuditorAwareImpl();
	}
}
