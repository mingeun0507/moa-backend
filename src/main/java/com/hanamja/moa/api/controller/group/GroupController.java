package com.hanamja.moa.api.controller.group;

import com.hanamja.moa.api.dto.group.request.KickOutRequestDto;
import com.hanamja.moa.api.dto.group.request.MakingGroupRequestDto;
import com.hanamja.moa.api.dto.group.request.ModifyingGroupRequestDto;
import com.hanamja.moa.api.dto.group.request.RemovingGroupRequestDto;
import com.hanamja.moa.api.dto.group.response.GroupCompleteRespDto;
import com.hanamja.moa.api.dto.group.response.GroupDetailInfoResponseDto;
import com.hanamja.moa.api.dto.group.response.GroupInfoListResponseDto;
import com.hanamja.moa.api.dto.group.response.GroupInfoResponseDto;
import com.hanamja.moa.api.dto.util.DataResponseDto;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.service.group.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<GroupInfoResponseDto> makeNewGroup(@AuthenticationPrincipal UserAccount userAccount, @RequestBody MakingGroupRequestDto makingGroupRequestDto) {

        return ResponseEntity.ok(groupService.makeNewGroup(userAccount, makingGroupRequestDto));
    }

    @PutMapping
    public ResponseEntity<GroupInfoResponseDto> modifyExistingGroup(@AuthenticationPrincipal UserAccount userAccount, @RequestBody ModifyingGroupRequestDto modifyingGroupRequestDto) {

        return ResponseEntity.ok(groupService.modifyExistingGroup(userAccount, modifyingGroupRequestDto));
    }

    @DeleteMapping
    public ResponseEntity<GroupInfoResponseDto> removeExistingGroup(@AuthenticationPrincipal UserAccount userAccount, @RequestBody RemovingGroupRequestDto removingGroupRequestDto) {

        return ResponseEntity.ok(groupService.removeExistingGroup(userAccount, removingGroupRequestDto));
    }

    @GetMapping
    public ResponseEntity<DataResponseDto<List<GroupInfoResponseDto>>> getExistingGroups(@RequestParam String sortedBy) {

        return ResponseEntity.ok(groupService.getExistingGroups(sortedBy));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupDetailInfoResponseDto> getExistingGroupDetail(@AuthenticationPrincipal UserAccount userAccount, @PathVariable Long id) {

        return ResponseEntity.ok(groupService.getExistingGroupDetail(userAccount, id));
    }
        
    @PostMapping("/{groupId}")
    public ResponseEntity<GroupInfoResponseDto> join(
            @PathVariable("groupId") Long groupId,
            @AuthenticationPrincipal UserAccount userAccount
    ) {

        GroupInfoResponseDto response = groupService.join(groupId, userAccount);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my")
    public ResponseEntity<GroupInfoListResponseDto> getMyGroupList(
             @AuthenticationPrincipal UserAccount userAccount) {

        GroupInfoListResponseDto response = groupService.getMyGroupList(userAccount.getUserId());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/my/{groupId}")
    public ResponseEntity<GroupInfoResponseDto> quit(
            @PathVariable("groupId") Long groupId,
            @AuthenticationPrincipal UserAccount userAccount
    ) {
        GroupInfoResponseDto response = groupService.quit(groupId, userAccount);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/maker")
    public ResponseEntity<GroupInfoListResponseDto> getGroupListMadeMyMe(@AuthenticationPrincipal UserAccount userAccount){
        GroupInfoListResponseDto groupListMadeByMe = groupService.getGroupListMadeByMe(userAccount.getUserId());
        return ResponseEntity.ok().body(groupListMadeByMe);
    }

    @DeleteMapping(value = "/out")
    public ResponseEntity<?> outMemberFromGroup(@AuthenticationPrincipal UserAccount userAccount,
                                                @RequestBody KickOutRequestDto kickOutRequestDto){
        groupService.kickoutMemberFromGroup(userAccount.getUserId(), kickOutRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/cancel/{gid}")
    public ResponseEntity<?> cancelGroup(@AuthenticationPrincipal UserAccount userAccount,
                                         @PathVariable Long gid){
        groupService.cancelGroup(userAccount.getUserId(), gid);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/recruit/end/{gid}")
    public ResponseEntity<?> recruitDone(@AuthenticationPrincipal UserAccount userAccount,
                                         @PathVariable Long gid){
        groupService.groupRecruitDone(userAccount.getUserId(), gid);
        return ResponseEntity.ok().build();
    }


    /**
     * 승민 TODO
     * 1. 인증샷 업로드가 되면 imageLink 및 진행상황 complete 로 업데이트 - PUT
     */
    @PostMapping(value = "/complete")
    public ResponseEntity<?> makeCard(@AuthenticationPrincipal UserAccount userAccount,
                                      @RequestPart(value = "gid") Long gid,
                                      @RequestPart(value = "image") MultipartFile image) throws IOException {
        // 모임 완료 후 -> card 생성하는 로직
        GroupCompleteRespDto groupCompleteRespDto = groupService.completeGroup(userAccount.getUserId(), gid, image);
        return ResponseEntity.ok().body(groupCompleteRespDto);
    }
}
