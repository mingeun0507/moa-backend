package com.hanamja.moa.api.controller.group;

import com.hanamja.moa.api.dto.group.request.MakingGroupRequestDto;
import com.hanamja.moa.api.dto.group.request.ModifyingGroupRequestDto;
import com.hanamja.moa.api.dto.group.request.RemovingGroupRequestDto;
import com.hanamja.moa.api.dto.group.response.GroupDetailInfoResponseDto;
import com.hanamja.moa.api.dto.group.response.GroupInfoResponseDto;
import com.hanamja.moa.api.service.group.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/group")
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupInfoResponseDto> makeNewGroup(@RequestBody MakingGroupRequestDto makingGroupRequestDto) {
        // TODO: 로그인 구현 후 @AuthenticationPrincipal User user 추가 필요

        return ResponseEntity.ok(groupService.makeNewGroup(makingGroupRequestDto));
    }

    @PutMapping
    public ResponseEntity<GroupInfoResponseDto> modifyExistingGroup(@RequestBody ModifyingGroupRequestDto modifyingGroupRequestDto) {
        // TODO: 로그인 구현 후 @AuthenticationPrincipal User user 추가 필요

        return ResponseEntity.ok(groupService.modifyExistingGroup(modifyingGroupRequestDto));
    }

    @DeleteMapping
    public ResponseEntity<GroupInfoResponseDto> removeExistingGroup(@RequestBody RemovingGroupRequestDto removingGroupRequestDto) {
        // TODO: 로그인 구현 후 @AuthenticationPrincipal User user 추가 필요

        return ResponseEntity.ok(groupService.removeExistingGroup(removingGroupRequestDto));
    }

    @GetMapping
    public ResponseEntity<List<GroupInfoResponseDto>> getExistingGroups(@RequestParam String sortedBy) {

        return ResponseEntity.ok(groupService.getExistingGroups(sortedBy));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDetailInfoResponseDto> getExistingGroupDetail(@PathVariable Long id) {
        // TODO: 로그인 구현 후 @AuthenticationPrincipal User user 추가 필요

        return ResponseEntity.ok(groupService.getExistingGroupDetail(id));
    }
}
