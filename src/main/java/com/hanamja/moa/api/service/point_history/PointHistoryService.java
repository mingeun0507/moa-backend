package com.hanamja.moa.api.service.point_history;

import com.hanamja.moa.api.dto.point_history.response.PointHistoryInfoResponseDto;
import com.hanamja.moa.api.dto.user.response.UserPointRankResponseDto;
import com.hanamja.moa.api.dto.util.DataResponseDto;
import com.hanamja.moa.api.entity.point_history.PointHistory;
import com.hanamja.moa.api.entity.point_history.PointHistoryRepository;
import com.hanamja.moa.api.entity.user.Role;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.entity.user.UserRepository;
import com.hanamja.moa.exception.custom.NotFoundException;
import com.hanamja.moa.exception.custom.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class PointHistoryService {
    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public DataResponseDto<List<PointHistoryInfoResponseDto>> getHistoryList(UserAccount userAccount) {
        User user = validateUser(userAccount.getUserId());

        return DataResponseDto.<List<PointHistoryInfoResponseDto>>builder()
                .data(pointHistoryRepository
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

    public PointHistoryInfoResponseDto removeHistory(UserAccount userAccount, Long historyId) {
        User user = validateUser(userAccount.getUserId());

        PointHistory pointHistory = pointHistoryRepository.findById(historyId).orElseThrow(
                () -> NotFoundException
                        .builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("해당하는 historyId로 history를 찾을 수 없습니다.")
                        .build()
        );

        if (!pointHistory.getOwner().getId().equals(user.getId())) {
            throw UnauthorizedException
                    .builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("본인의 포인트 적립 내역만 삭제할 수 있습니다.")
                    .build();
        }

        pointHistoryRepository.deleteById(pointHistory.getId());

        return PointHistoryInfoResponseDto.from(pointHistory);
    }

    private User validateUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> NotFoundException
                        .builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("유효하지 않은 사용자입니다.")
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public DataResponseDto<List<UserPointRankResponseDto>> getUserPointRank(){
        List<UserPointRankResponseDto> responseDtos = new ArrayList<>();

        responseDtos.add(UserPointRankResponseDto.of("FRESHMAN",
                userRepository.findTop20ByRoleAndIsOnboardedAndIsActiveOrderByPointDesc(Role.ROLE_FRESHMEN, true, true)));
        responseDtos.add(UserPointRankResponseDto.of("SENIORS",
                userRepository.findTop20ByRoleAndIsOnboardedAndIsActiveOrderByPointDesc(Role.ROLE_SENIOR, true, true)));

        return DataResponseDto.<List<UserPointRankResponseDto>>builder().data(responseDtos).build();
    }

    @Transactional(readOnly = true)
    public DataResponseDto<UserPointRankResponseDto.RankTab> getRank(Long userId){
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new NotFoundException(HttpStatus.BAD_REQUEST, "해당 유저가 없습니다. id=" + userId));
        int rank = userRepository.getUserRank(userId, user.getRole());
        return DataResponseDto.<UserPointRankResponseDto.RankTab>builder()
                .data(UserPointRankResponseDto.RankTab.of(user, Long.valueOf(rank)))
                .build();
    }
}
