package com.hanamja.moa.api.dto.point.response;

import com.hanamja.moa.api.dto.point_history.response.PointHistoryInfoResponseDto;
import com.hanamja.moa.api.dto.user.response.UserInfoResponseDto;
import com.hanamja.moa.api.entity.point_history.PointHistory;
import com.hanamja.moa.api.entity.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class PointResponseDto {

    private UserInfoResponseDto userInfo;

    private PointHistoryInfoResponseDto pointInfo;

    public static PointResponseDto from(User user, PointHistory pointHistory) {
        PointResponseDto pointResponseDto = new PointResponseDto();
        pointResponseDto.userInfo = UserInfoResponseDto.from(user);
        pointResponseDto.pointInfo = PointHistoryInfoResponseDto.from(pointHistory);

        return pointResponseDto;
    }
}
