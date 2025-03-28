package com.linkmoa.source.domain.dispatch.repository.impl;

import static com.linkmoa.source.domain.dispatch.entity.QSharePageInvitationRequest.*;

import java.util.List;

import com.linkmoa.source.domain.dispatch.dto.response.DispatchDetailResponse;
import com.linkmoa.source.domain.dispatch.repository.custom.SharePageInvitationRequestRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SharePageInvitationRequestRepositoryImpl implements SharePageInvitationRequestRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<DispatchDetailResponse> findAllSharePageInvitationsByReceiverEmail(String receiverEmail) {

		List<DispatchDetailResponse> result = jpaQueryFactory.select
				(Projections.constructor(
					DispatchDetailResponse.class,
					sharePageInvitationRequest.id,
					sharePageInvitationRequest.sender.email,
					sharePageInvitationRequest.requestStatus,
					sharePageInvitationRequest.notificationType
				))
			.from(sharePageInvitationRequest)
			.where(receiverEmailEq(receiverEmail))
			.fetch();

		return result;
	}

	private BooleanExpression receiverEmailEq(String receiverEmail) {
		return receiverEmail == null ? null : sharePageInvitationRequest.receiver.email.eq(receiverEmail);
	}
}
