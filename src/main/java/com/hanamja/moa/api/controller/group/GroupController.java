package com.hanamja.moa.api.controller.group;

import com.hanamja.moa.api.dto.comment.request.WritingCommentRequestDto;
import com.hanamja.moa.api.dto.comment.response.CommentInfoResponseDto;
import com.hanamja.moa.api.dto.group.request.KickOutRequestDto;
import com.hanamja.moa.api.dto.group.request.MakingGroupRequestDto;
import com.hanamja.moa.api.dto.group.request.ModifyingGroupRequestDto;
import com.hanamja.moa.api.dto.group.response.*;
import com.hanamja.moa.api.dto.util.DataResponseDto;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.service.group.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@Tag(name = "groups", description = "모임 관련 API")
@RestController
@RequestMapping("/api/group")
public class GroupController {

    private final GroupService groupService;

    @Operation(summary = "새로운 모임 만들기", description = "새로운 모임 만들기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK")
    }

    )
    @PostMapping
    public ResponseEntity<GroupInfoResponseDto> makeNewGroup(@Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount, @Validated @RequestBody MakingGroupRequestDto makingGroupRequestDto) {

        return ResponseEntity.ok(groupService.makeNewGroup(userAccount, makingGroupRequestDto));
    }

    @Operation(summary = "기존 모임 수정하기", description = "기존 모임 수정하기")
    @PutMapping
    public ResponseEntity<GroupInfoResponseDto> modifyExistingGroup(@Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount, @Validated @RequestBody ModifyingGroupRequestDto modifyingGroupRequestDto) {

        return ResponseEntity.ok(groupService.modifyExistingGroup(userAccount, modifyingGroupRequestDto));
    }

    @Operation(summary = "기존 모임 삭제하기", description = "기존 모임 삭제하기")
    @DeleteMapping("/{groupId}")
    public ResponseEntity<GroupInfoResponseDto> removeExistingGroup(@Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount, @NotNull @PathVariable Long groupId) {

        return ResponseEntity.ok(groupService.removeExistingGroup(userAccount, groupId));
    }

    @Operation(summary = "기존 모임 조회하기", description = "기존 모임 조회하기")
    @GetMapping
    public ResponseEntity<DataResponseDto<List<GroupInfoResponseDto>>> getExistingGroups(@RequestParam SortedBy sortedBy) {

        return ResponseEntity.ok(groupService.getExistingGroups(sortedBy));
    }

    @Operation(summary = "기존 모임 조회하기(채널분리버전) - ver2.0", description = "기존 모임 조회하기(채널분리버전) - ver2.0")
    @GetMapping("/channel")
    public ResponseEntity<DataResponseDto<List<GroupInfoResponseDto>>> getExistingGroups2
            (@AuthenticationPrincipal UserAccount userAccount,
             @RequestParam SortedBy sortedBy,
             @Nullable @RequestParam(required = false) Long cursor,
             @PageableDefault(size = 2) Pageable pageable)
    {
        return ResponseEntity.ok(groupService.getExistingGroups2(userAccount, sortedBy, cursor, pageable));
    }

    @Operation(summary = "기존 모임 상세 조회하기", description = "기존 모임 상세 조회하기")
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDetailInfoResponseDto> getExistingGroupDetail(
            @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
            @NotNull @PathVariable Long groupId) {

        return ResponseEntity.ok(groupService.getExistingGroupDetail(userAccount, groupId));
    }

    @Operation(summary = "공유하기 기능 - 모임 정보 보기", description = "공유하기 기능 - 모임 정보 보기")
    @GetMapping("/public/{groupId}")
    public ResponseEntity<GroupDetailInfoResponseDto> getPublicExistingGroupDetail(
            @NotNull @PathVariable Long groupId) {

        return ResponseEntity.ok(groupService.getPublicExistingGroupDetail(groupId));
    }

    @Operation(summary = "모임 참가하기", description = "모임 참가하기")
    @PostMapping("/{groupId}")
    public ResponseEntity<GroupInfoResponseDto> join(
            @PathVariable("groupId") Long groupId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount
    ) {

        GroupInfoResponseDto response = groupService.join(groupId, userAccount);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내 모임 조회하기", description = "내 모임 조회하기")
    @GetMapping("/my")
    public ResponseEntity<DataResponseDto<List<GroupStateInfoResponseDto>>> getMyGroupList(
            @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount) {

        DataResponseDto<List<GroupStateInfoResponseDto>> myGroupList = groupService.getMyGroupList(userAccount.getUserId());

        return ResponseEntity.ok().body(myGroupList);
    }

    @Operation(summary = "모임 탈퇴하기", description = "모임 탈퇴하기")
    @DeleteMapping("/my/{groupId}")
    public ResponseEntity<GroupInfoResponseDto> quit(
            @NotNull @PathVariable("groupId") Long groupId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount
    ) {
        GroupInfoResponseDto response = groupService.quit(groupId, userAccount);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "내가 만든 모임 보기", description = "내가 만든 모임 보기")
    @GetMapping(value = "/maker")
    public ResponseEntity<GroupInfoListResponseDto> getGroupListMadeMyMe(@Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount){
        GroupInfoListResponseDto groupListMadeByMe = groupService.getGroupListMadeByMe(userAccount.getUserId());
        return ResponseEntity.ok().body(groupListMadeByMe);
    }

    @Operation(summary = "모임원 강퇴하기", description = "모임원 강퇴하기")
    @DeleteMapping(value = "/out")
    public ResponseEntity<?> outMemberFromGroup(@Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
                                                @Validated @ModelAttribute KickOutRequestDto kickOutRequestDto){
        return ResponseEntity.ok(groupService.kickOutMemberFromGroup(userAccount.getUserId(), kickOutRequestDto));
    }

    @Operation(summary = "모임 취소하기", description = "모임 취소하기")
    @DeleteMapping(value = "/cancel/{groupId}")
    public ResponseEntity<?> cancelGroup(@Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
                                         @NotNull @PathVariable Long groupId){
        return ResponseEntity.ok(groupService.cancelGroup(userAccount.getUserId(), groupId));
    }

    @Operation(summary = "모임 모집 마감하기", description = "모임 모집 마감하기")
    @PutMapping(value = "/recruit/end/{groupId}")
    public ResponseEntity<?> recruitDone(@Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
                                         @NotNull @PathVariable Long groupId){
        groupService.groupRecruitDone(userAccount.getUserId(), groupId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "카드 만들기", description = "카드 만들기")
    @PostMapping(value = "/complete")
    public ResponseEntity<GroupCompleteRespDto> makeCard
    (
            @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
            @RequestParam(value = "gid") Long gid,
            @RequestParam(value = "image") MultipartFile image
    ) throws Exception {
        // 모임 완료 후 -> card 생성하는 로직
        GroupCompleteRespDto groupCompleteRespDto = groupService.completeGroup(userAccount, gid, image);
        return ResponseEntity.ok().body(groupCompleteRespDto);
    }

//    @Operation(summary = "모임 검색하기", description = "모임 검색하기")
//    @GetMapping("/search")
//    public ResponseEntity<DataResponseDto<List<GroupInfoResponseDto>>> searchGroupByKeyword(@RequestParam String keyword) {
//        return ResponseEntity.ok(groupService.searchGroupByKeyword(keyword));
//    }

    @Operation(summary = "모임 검색하기 - 정렬", description = "모임 검색하기 - 정렬")
    @GetMapping("/search")
    public ResponseEntity<DataResponseDto<List<GroupInfoResponseDto>>> searchAndSortGroupByKeyword(@RequestParam String keyword, @RequestParam SortedBy sortedBy) {
        return ResponseEntity.ok(groupService.searchAndSortGroupByKeyword(keyword, sortedBy));
    }

    @Operation(summary = "모임 댓글 작성하기", description = "모임 댓글 작성하기")
    @PostMapping("/{groupId}/comment")
    public ResponseEntity<DataResponseDto<CommentInfoResponseDto>> writeComment(@Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount, @NotNull @PathVariable Long groupId, @Validated @RequestBody WritingCommentRequestDto writingCommentRequestDto) {

        return ResponseEntity.ok(groupService.writeComment(userAccount, groupId, writingCommentRequestDto));
    }

    @Operation(summary = "모임 댓글 수정하기", description = "모임 댓글 수정하기")
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<DataResponseDto<CommentInfoResponseDto>> updateComment(@Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount, @NotNull @PathVariable Long commentId, @Validated @RequestBody WritingCommentRequestDto writingCommentRequestDto) {

        return ResponseEntity.ok(groupService.updateComment(userAccount, commentId, writingCommentRequestDto));
    }

    @Operation(summary = "모임 댓글 삭제하기", description = "모임 댓글 삭제하기")
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<DataResponseDto<CommentInfoResponseDto>> deleteComment(@Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount, @NotNull @PathVariable Long commentId) {

        return ResponseEntity.ok(groupService.deleteComment(userAccount, commentId));
    }

    @Operation(summary = "모임 댓글 리스트 조회하기", description = "모임 댓글 리스트 조회하기")
    @GetMapping("/{groupId}/comment")
    public ResponseEntity<DataResponseDto<Page<CommentInfoResponseDto>>> getPagedCommentList(@NotNull @PathVariable Long groupId,
                                                                                             @Nullable @RequestParam Long cursor,
                                                                                             @PageableDefault(size = 20, sort = "comment_id", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(groupService.getCommentList(groupId, cursor, pageable));
    }
}
