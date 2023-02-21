package com.hanamja.moa.api.controller.point_history;

import com.hanamja.moa.api.dto.point_history.response.PointHistoryInfoResponseDto;
import com.hanamja.moa.api.dto.util.DataResponseDto;
import com.hanamja.moa.api.service.point_history.PointHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/point-history")
public class PointHistoryController {
    private final PointHistoryService pointHistoryService;

    @GetMapping
    public ResponseEntity<DataResponseDto<List<PointHistoryInfoResponseDto>>> getHistoryList() {
        // TODO: 로그인 구현 후 @AuthenticationPrincipal User user 추가 필요

        return ResponseEntity.ok(pointHistoryService.getHistoryList());
    }

//    @GetMapping("/{historyId}")
//    public ResponseEntity<HistoryDetailInfoResponseDto> getHistoryDetail(@PathVariable Long historyId) {
//        // TODO: 로그인 구현 후 @AuthenticationPrincipal User user 추가 필요
//
//        return ResponseEntity.ok(historyService.getHistoryDetail(historyId));
//    }

    @DeleteMapping("/{historyId}")
    public ResponseEntity<PointHistoryInfoResponseDto> removeHistory(@PathVariable Long historyId) {
        // TODO: 로그인 구현 후 @AuthenticationPrincipal User user 추가 필요

        return ResponseEntity.ok(pointHistoryService.removeHistory(historyId));
    }
}
