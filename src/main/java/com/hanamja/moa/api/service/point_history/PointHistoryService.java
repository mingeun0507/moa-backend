package com.hanamja.moa.api.service.point_history;

import com.hanamja.moa.api.dto.point_history.response.PointHistoryDetailInfoResponseDto;
import com.hanamja.moa.api.dto.point_history.response.PointHistoryInfoResponseDto;
import com.hanamja.moa.api.dto.util.ListResponseDto;
import com.hanamja.moa.api.entity.point_history.PointHistory;
import com.hanamja.moa.api.entity.point_history.PointHistoryRepository;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.user.UserRepository;
import com.hanamja.moa.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class PointHistoryService {
    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public ListResponseDto<PointHistoryInfoResponseDto> getHistoryList() {
        // TODO: 로그인 구현 후 @AuthenticationPrincipal User user 추가 필요
        User user = userRepository.findById(1L).orElseThrow();

        return ListResponseDto.<PointHistoryInfoResponseDto>builder()
                .items(pointHistoryRepository
                        .findAllByOwner_Id(user.getId())
                        .stream()
                        .map(PointHistoryInfoResponseDto::from)
                        .collect(Collectors.toList()))
                .build();
    }

//    public HistoryDetailInfoResponseDto getHistoryDetail(Long historyId) {
//        User user = userRepository.findById(1L).orElseThrow();
//
//        return HistoryDetailInfoResponseDto.from(historyRepository
//                .findById(historyId)
//                .orElseThrow(
//                        () -> NotFoundException
//                                .builder()
//                                .httpStatus(HttpStatus.BAD_REQUEST)
//                                .message("해당하는 historyId로 history를 찾을 수 없습니다.")
//                                .build()
//
//                )
//        );
//    }

    public PointHistoryDetailInfoResponseDto removeHistory(Long historyId) {
        // TODO: 로그인 구현 후 @AuthenticationPrincipal User user 추가 필요
        User user = userRepository.findById(1L).orElseThrow();
        PointHistory pointHistory = null;

        if (!pointHistoryRepository.existsById(historyId)) {
            throw NotFoundException
                    .builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("해당하는 historyId로 history를 찾을 수 없습니다.")
                    .build();
        } else {
            pointHistory = pointHistoryRepository.findById(historyId).orElseThrow();
        }

        pointHistoryRepository.deleteById(pointHistory.getId());

        return PointHistoryDetailInfoResponseDto.from(pointHistory);
    }
}
