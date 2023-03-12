package com.hanamja.moa.api.dto.group.response;

import com.hanamja.moa.api.entity.group.Group;
import com.hanamja.moa.api.entity.group.State;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "모임 List 응답 DTO")

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupInfoResponseDto {
    @Schema(description = "모임 ID")
    private Long id;

    @Schema(description = "모임 이름")
    private String name;

    @Schema(description = "모임 설명")
    private String description;

    @Schema(description = "모임 상태")
    private State state;

    @Schema(description = "최대 참여 가능한 인원 수")
    private Long maxPeopleNum;

    @Schema(description = "현재 모임에 참여한 인원 수")
    private Long currentPeopleNum;

    @Schema(description = "모임 생성 일시")
    private LocalDateTime createdAt;

    @Schema(description = "모임 수정 일시")
    private LocalDateTime modifiedAt;

    @Schema(description = "모임 만남 일시")
    private LocalDateTime meetingAt;

    @Schema(description = "모임 이미지 링크")
    private String imageLink;

    @Schema(description = "모임 댓글 수")
    private int commentCount;

    @Schema(description = "모임 해시태그 리스트")
    private List<String> hashtags;

    @Schema(description = "모임 생성자 정보")
    private SimpleUserInfoResponseDto groupMakerInfo;

    @Builder
    public GroupInfoResponseDto(Long id, String name, String description, State state, Long maxPeopleNum, Long currentPeopleNum, LocalDateTime createdAt, LocalDateTime modifiedAt, LocalDateTime meetingAt, String imageLink, int commentCount, List<String> hashtags, SimpleUserInfoResponseDto groupMakerInfo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.state = state;
        this.maxPeopleNum = maxPeopleNum;
        this.currentPeopleNum = currentPeopleNum;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.meetingAt = meetingAt;
        this.imageLink = imageLink;
        this.commentCount = commentCount;
        this.hashtags = hashtags;
        this.groupMakerInfo = groupMakerInfo;
    }

    public static GroupInfoResponseDto from(Group group, List<String> hashtags) {
        return GroupInfoResponseDto
                .builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .state(group.getState())
                .maxPeopleNum(group.getMaxPeopleNum())
                .currentPeopleNum(group.getCurrentPeopleNum())
                .createdAt(group.getCreatedAt())
                .modifiedAt(group.getModifiedAt())
                .meetingAt(group.getMeetingAt())
                .imageLink(group.getImageLink())
                .commentCount(group.getCommentCount())
                .hashtags(hashtags)
                .groupMakerInfo(SimpleUserInfoResponseDto.from(group.getMaker()))
                .commentCount(group.getCommentCount())
                .build();
    }

}
