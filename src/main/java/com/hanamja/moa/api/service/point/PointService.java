package com.hanamja.moa.api.service.point;

import com.hanamja.moa.api.dto.point.request.AddPointRequestDto;
import com.hanamja.moa.api.dto.point.response.PointResponseDto;
import com.hanamja.moa.api.entity.point_history.PointHistory;
import com.hanamja.moa.api.entity.point_history.PointHistoryRepository;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.user.UserRepository;
import com.hanamja.moa.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PointService {

    private final PointHistoryRepository pointHistoryRepository;

    private final UserRepository userRepository;

    public PointResponseDto addPoint(AddPointRequestDto addPointRequestDto) {
        User user = userRepository.findUserById(addPointRequestDto.getUserId()).orElseThrow(() -> NotFoundException.builder()
                                                        .httpStatus(HttpStatus.NOT_FOUND)
                                                        .message("유저를 찾을 수 없습니다.").build());

        String message = "모임 점수: 0점\n" +
                addPointRequestDto.getTitle() + " 점수: " + addPointRequestDto.getPoint() + "점\n"
                + "총 점수: " + addPointRequestDto.getPoint() + "점";


        PointHistory pointHistory = PointHistory.builder()
                .owner(user)
                .title(addPointRequestDto.getTitle())
                .message(message)
                .point(addPointRequestDto.getPoint())
                .build();

        pointHistoryRepository.save(pointHistory);

        user.addPoint(addPointRequestDto.getPoint());
        User savedUser = userRepository.save(user);

        return PointResponseDto.from(savedUser, pointHistory);
    }
}
