package com.hanamja.moa.api.controller.point_history;

import com.hanamja.moa.api.dto.point_history.response.PointHistoryInfoResponseDto;
import com.hanamja.moa.api.dto.util.DataResponseDto;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.service.point_history.PointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/point-history")
public class PointHistoryController {
    private final PointHistoryService pointHistoryService;

    @GetMapping
    public ResponseEntity<DataResponseDto<List<PointHistoryInfoResponseDto>>> getHistoryList(@AuthenticationPrincipal UserAccount userAccount) {

        return ResponseEntity.ok(pointHistoryService.getHistoryList(userAccount));
    }


    @DeleteMapping("/{historyId}")
    public ResponseEntity<PointHistoryInfoResponseDto> removeHistory(@AuthenticationPrincipal UserAccount userAccount, @PathVariable Long historyId) {

        return ResponseEntity.ok(pointHistoryService.removeHistory(userAccount, historyId));
    }
}
