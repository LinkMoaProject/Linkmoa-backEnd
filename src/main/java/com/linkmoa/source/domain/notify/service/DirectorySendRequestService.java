package com.linkmoa.source.domain.notify.service;


import com.linkmoa.source.domain.notify.aop.annotation.NotifyApplied;
import com.linkmoa.source.domain.notify.dto.request.DirectorySendRequestDto;
import com.linkmoa.source.domain.notify.entity.DirectorySendRequest;
import com.linkmoa.source.domain.notify.repository.DirectorySendRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DirectorySendRequestService {

    private final DirectorySendRequestRepository directorySendRequestRepository;


    @NotifyApplied
    @Transactional
    public DirectorySendRequest createDirectorySendRequest(DirectorySendRequestDto directorySendRequestDto){

        DirectorySendRequest directorySendRequest = DirectorySendRequest.builder()
                .senderEmail(directorySendRequestDto.senderEmail())
                .receiverEmail(directorySendRequestDto.receiverEmail())
                .directoryId(directorySendRequestDto.directoryId())
                .build();


        return directorySendRequestRepository.save(directorySendRequest);
    }



}
