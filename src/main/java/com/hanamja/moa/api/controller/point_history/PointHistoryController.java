package com.hanamja.moa.api.controller.point_history;

import com.hanamja.moa.api.dto.point_history.response.PointHistoryInfoResponseDto;
import com.hanamja.moa.api.dto.user.response.UserPointRankResponseDto;
import com.hanamja.moa.api.dto.util.DataResponseDto;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.service.point_history.PointHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/point-history")
public class PointHistoryController {
    private final PointHistoryService pointHistoryService;

    @Operation(summary = "포인트 내역 조회", description = "포인트 내역 조회")
    @GetMapping
    public ResponseEntity<DataResponseDto<List<PointHistoryInfoResponseDto>>> getHistoryList(
            @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount)
    {

        return ResponseEntity.ok(pointHistoryService.getHistoryList(userAccount));
    }


    @Operation(summary = "포인트 내역 삭제", description = "포인트 내역 삭제")
    @DeleteMapping("/{historyId}")
    public ResponseEntity<PointHistoryInfoResponseDto> removeHistory(
            @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
            @NotNull @PathVariable Long historyId) {

        return ResponseEntity.ok(pointHistoryService.removeHistory(userAccount, historyId));
    }

    @Operation(summary = "유저 포인트 랭킹 조회", description = "유저 포인트 랭킹 조회")
    @GetMapping("/rank/{user_id}")
    public ResponseEntity<DataResponseDto<UserPointRankResponseDto.RankTab>> getMyPointRanking(
            @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
            @PathVariable(value = "user_id")Long userId
    )
    {
        DataResponseDto<UserPointRankResponseDto.RankTab> rank = pointHistoryService.getRank(userId);
        return ResponseEntity.ok().body(rank);
    }

    @Operation(summary = "전체 포인트 랭킹 조회", description = "전체 포인트 랭킹 조회")
    @GetMapping("/rank")
    public ResponseEntity<DataResponseDto<List<UserPointRankResponseDto>>> getPointRanking()
    {
        DataResponseDto<List<UserPointRankResponseDto>> userPointRank = pointHistoryService.getUserPointRank();
        return ResponseEntity.ok().body(userPointRank);
    }
}
