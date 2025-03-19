package com.linkmoa.source.global.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

	@Bean(name = "threadPoolTaskExecutor")
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(20); // 기본 스레드 수
		executor.setMaxPoolSize(100); // 최대 스레드 수
		executor.setQueueCapacity(1000); // 대기열 크기
		executor.setThreadNamePrefix("Executor-");
		executor.initialize();

		return executor;
	}
}
