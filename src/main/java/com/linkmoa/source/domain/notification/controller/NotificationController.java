package com.linkmoa.source.domain.notification.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.linkmoa.source.auth.oauth2.principal.PrincipalDetails;
import com.linkmoa.source.domain.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	//@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId)
	@GetMapping(value = "/subscribe", produces = "text/event-stream")
	@PreAuthorize("isAuthenticated()")
	public SseEmitter subscribe(
		@AuthenticationPrincipal PrincipalDetails principalDetails,
		@RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {

		return notificationService.subscribe(principalDetails.getEmail(), lastEventId);
	}

}
