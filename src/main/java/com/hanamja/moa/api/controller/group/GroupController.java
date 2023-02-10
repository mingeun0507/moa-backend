package com.hanamja.moa.api.controller.group;

import com.hanamja.moa.api.dto.group.GroupInfoResponseDto;
import com.hanamja.moa.api.dto.group.GroupMakingRequestDto;
import com.hanamja.moa.api.service.group.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/group")
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupInfoResponseDto> makeNewGroup(@RequestBody GroupMakingRequestDto groupMakingRequestDto) {
        // TODO: 로그인 구현 후 @AuthenticationPrincipal User user 추가 필요

        return ResponseEntity.ok(groupService.makeNewGroup(groupMakingRequestDto));
    }

}
