package com.linkmoa.source.domain.dispatch.repository.impl;

import com.linkmoa.source.domain.dispatch.dto.response.DispatchDetailResponse;
import com.linkmoa.source.domain.dispatch.repository.custom.DirectoryTransmissionRequestRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import static com.linkmoa.source.domain.dispatch.entity.QDirectoryTransmissionRequest.directoryTransmissionRequest;

@RequiredArgsConstructor
public class DirectoryTransmissionRequestRepositoryImpl implements DirectoryTransmissionRequestRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<DispatchDetailResponse> findAllDirectoryTransmissionRequestByReceiverEmail(String receiverEmail) {

        List<DispatchDetailResponse> result = jpaQueryFactory.select(
                        Projections.constructor(
                                DispatchDetailResponse.class,
                                directoryTransmissionRequest.id,
                                directoryTransmissionRequest.sender.email,
                                directoryTransmissionRequest.requestStatus,
                                directoryTransmissionRequest.notificationType
                        ))
                .from(directoryTransmissionRequest)
                .where(receiverEmailEq(receiverEmail))
                .fetch();

        return result;
    }
    private BooleanExpression receiverEmailEq(String receiverEmail){
        return receiverEmail==null ? null :directoryTransmissionRequest.receiver.email.eq(receiverEmail);
    }
}

