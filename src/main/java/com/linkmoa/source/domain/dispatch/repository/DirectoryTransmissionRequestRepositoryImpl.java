package com.linkmoa.source.domain.dispatch.repository;

import com.linkmoa.source.domain.dispatch.dto.response.DispatchDetailResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import static com.linkmoa.source.domain.dispatch.entity.QDirectoryTransmissionRequest.directoryTransmissionRequest;
import static com.linkmoa.source.domain.dispatch.entity.QSharePageInvitationRequest.sharePageInvitationRequest;

@RequiredArgsConstructor
public class DirectoryTransmissionRequestRepositoryImpl implements DirectoryTransmissionRequestRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<DispatchDetailResponse> findAllDirectoryTransmissionRequestByReceiverEmail(String receiverEmail) {

        List<DispatchDetailResponse> result = jpaQueryFactory.select(
                        Projections.constructor(
                                DispatchDetailResponse.class,
                                directoryTransmissionRequest.id,
                                directoryTransmissionRequest.senderEmail,
                                Expressions.stringTemplate(
                                        "{0}님이 디렉토리를 전송했습니다.",
                                        directoryTransmissionRequest.senderEmail
                                ),
                                directoryTransmissionRequest.requestStatus,
                                directoryTransmissionRequest.notificationType
                        ))
                .from(directoryTransmissionRequest)
                .where(receiverEmailEq(receiverEmail))
                .fetch();

        return result;
    }
    private BooleanExpression receiverEmailEq(String receiverEmail){
        return receiverEmail==null ? null :directoryTransmissionRequest.receiverEmail.eq(receiverEmail);
    }
}

//git commit -m "LP-119 feat: 알람 수신 목록 조회 시 사용되는 쿼리 dsl 구현 "
//디렉토리 전송 알람 수신 목록 조회 로직 구현
