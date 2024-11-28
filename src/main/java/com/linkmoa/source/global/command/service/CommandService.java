package com.linkmoa.source.global.command.service;


import com.linkmoa.source.domain.memberPageLink.constant.PermissionType;
import com.linkmoa.source.domain.memberPageLink.entity.MemberPageLink;
import com.linkmoa.source.domain.memberPageLink.repository.MemberPageLinkRepository;
import com.linkmoa.source.global.command.constant.CommandType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class CommandService {

    private final Map<PermissionType, Set<CommandType>> permissionToCommandsMap = new EnumMap<>(PermissionType.class);
    private final MemberPageLinkRepository memberPageLinkRepository;
    public CommandService(MemberPageLinkRepository memberPageLinkRepository) {

        this.memberPageLinkRepository=memberPageLinkRepository;

        permissionToCommandsMap.put(PermissionType.ADMIN, Set.of(
                CommandType.VIEW, CommandType.EDIT,CommandType.CREATE,
                CommandType.DIRECTORY_TRANSMISSION, CommandType.SHARED_PAGE_INVITATION,
                CommandType.SHARED_PAGE_DELETION, CommandType.SHARED_PAGE_USER_REMOVAL
        ));

        permissionToCommandsMap.put(PermissionType.HOST, Set.of(
                CommandType.VIEW, CommandType.EDIT,CommandType.CREATE,
                CommandType.DIRECTORY_TRANSMISSION, CommandType.SHARED_PAGE_INVITATION,
                CommandType.SHARED_PAGE_DELETION, CommandType.SHARED_PAGE_USER_REMOVAL
        ));

        permissionToCommandsMap.put(PermissionType.EDITOR, Set.of(
                CommandType.CREATE, CommandType.VIEW, CommandType.EDIT, CommandType.DIRECTORY_TRANSMISSION
        ));

        permissionToCommandsMap.put(PermissionType.VIEWER, Set.of(
                CommandType.VIEW
        ));
    }

    public boolean canExecute(PermissionType permissionType, CommandType commandType) {
        return permissionToCommandsMap.getOrDefault(permissionType, Set.of()).contains(commandType);
    }

    public PermissionType getUserPermissionType(Long memberId, Long pageId) {
        return memberPageLinkRepository.findPermissionTypeByMemberIdAndPageId(memberId, pageId);


    }






}
