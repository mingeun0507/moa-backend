package com.hanamja.moa.api.dto.group.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GroupCompleteRespDto {
    @JsonProperty(value = "member_info_list")
    private List<MemberInfo> memberInfoList;

    @JsonProperty(value = "group_image_link")
    private String groupImageLink;

    @Data
    @Builder
    public static class MemberInfo{
        @JsonProperty(value = "user_id")
        private Long userId;

        private String username;

        @JsonProperty(value = "meeting_cnt")
        private Long meetingCnt;

        @JsonProperty(value = "image_link")
        private String imageLink;
    }

    @Builder
    public GroupCompleteRespDto(List<MemberInfo> memberInfoList, String groupImageLink) {
        this.memberInfoList = memberInfoList;
        this.groupImageLink = groupImageLink;
    }
}
