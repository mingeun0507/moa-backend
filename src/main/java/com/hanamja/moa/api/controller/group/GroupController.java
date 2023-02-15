package com.hanamja.moa.api.controller.group;

import com.hanamja.moa.api.dto.group.request.MakingGroupRequestDto;
import com.hanamja.moa.api.dto.group.request.ModifyingGroupRequestDto;
import com.hanamja.moa.api.dto.group.request.RemovingGroupRequestDto;
import com.hanamja.moa.api.dto.group.response.GroupCompleteRespDto;
import com.hanamja.moa.api.dto.group.response.GroupDetailInfoResponseDto;
import com.hanamja.moa.api.dto.group.response.GroupInfoListResponseDto;
import com.hanamja.moa.api.dto.group.response.GroupInfoResponseDto;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.service.group.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
        
    @PostMapping("/{groupId}")
    public ResponseEntity<GroupInfoResponseDto> join(
            @PathVariable("groupId") Long groupId
            /*, @AuthenticationPrincipal UserAccount userAccount */
    ) {

        User user = User.builder().build();
        GroupInfoResponseDto response = groupService.join(groupId, user);

        return ResponseEntity.ok(response);
    }

    @GetMapping("my")
    public ResponseEntity<GroupInfoListResponseDto> getMyGroupList(
            /*, @AuthenticationPrincipal UserAccount userAccount */) {

        User user = User.builder().build();
        GroupInfoListResponseDto response = groupService.getMyGroupList(user);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("my")
    public ResponseEntity<GroupInfoResponseDto> quit(
            @PathVariable("groupId") Long groupId
            /*, @AuthenticationPrincipal UserAccount userAccount */
    ) {
        User user = User.builder().build();
        GroupInfoResponseDto response = groupService.quit(groupId, user);

        return ResponseEntity.ok(response);
    }


    /**
     * 승민 TODO
     * 1. 인증샷 업로드가 되면 imageLink 및 진행상황 complete 로 업데이트 - PUT
     */
    @PostMapping(value = "/complete")
    public ResponseEntity<?> makeCard(@RequestPart(value = "uid") Long uid,
                                      @RequestPart(value = "gid") Long gid,
                                      @RequestPart(value = "image") MultipartFile image) throws IOException {
        // 모임 완료 후 -> card 생성하는 로직
        GroupCompleteRespDto groupCompleteRespDto = groupService.completeGroup(uid, gid, image);
        return ResponseEntity.ok().body(groupCompleteRespDto);
    }
}
