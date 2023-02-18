package com.hanamja.moa.api.service.history;

import com.hanamja.moa.api.dto.history.response.HistoryDetailInfoResponseDto;
import com.hanamja.moa.api.dto.history.response.HistoryInfoResponseDto;
import com.hanamja.moa.api.dto.util.ListResponseDto;
import com.hanamja.moa.api.entity.history.History;
import com.hanamja.moa.api.entity.history.HistoryRepository;
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
public class HistoryService {
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;

    public ListResponseDto<HistoryInfoResponseDto> getHistoryList() {
        // TODO: 로그인 구현 후 @AuthenticationPrincipal User user 추가 필요
        User user = userRepository.findById(1L).orElseThrow();

        return ListResponseDto.<HistoryInfoResponseDto>builder()
                .items(historyRepository
                        .findAllByOwner_Id(user.getId())
                        .stream()
                        .map(HistoryInfoResponseDto::from)
                        .collect(Collectors.toList()))
                .build();
    }

    public HistoryDetailInfoResponseDto getHistoryDetail(Long historyId) {
        // TODO: 로그인 구현 후 @AuthenticationPrincipal User user 추가 필요
        User user = userRepository.findById(1L).orElseThrow();

        return HistoryDetailInfoResponseDto.from(historyRepository
                .findById(historyId)
                .orElseThrow(
                        () -> NotFoundException
                                .builder()
                                .httpStatus(HttpStatus.BAD_REQUEST)
                                .message("해당하는 historyId로 history를 찾을 수 없습니다.")
                                .build()

                )
        );
    }

    public HistoryDetailInfoResponseDto removeHistory(Long historyId) {
        // TODO: 로그인 구현 후 @AuthenticationPrincipal User user 추가 필요
        User user = userRepository.findById(1L).orElseThrow();
        History history = null;

        if (!historyRepository.existsById(historyId)) {
            throw NotFoundException
                    .builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("해당하는 historyId로 history를 찾을 수 없습니다.")
                    .build();
        } else {
            history = historyRepository.findById(historyId).orElseThrow();
        }

        historyRepository.deleteById(history.getId());

        return HistoryDetailInfoResponseDto.from(history);
    }
}
