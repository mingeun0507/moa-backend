package com.hanamja.moa.api.controller.history;

import com.hanamja.moa.api.dto.history.response.HistoryDetailInfoResponseDto;
import com.hanamja.moa.api.dto.history.response.HistoryInfoResponseDto;
import com.hanamja.moa.api.service.history.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/history")
public class HistoryController {
    private final HistoryService historyService;

    @GetMapping
    public ResponseEntity<List<HistoryInfoResponseDto>> getHistoryList() {
        // TODO: 로그인 구현 후 @AuthenticationPrincipal User user 추가 필요

        return ResponseEntity.ok(historyService.getHistoryList());
    }

    @GetMapping("/{historyId}")
    public ResponseEntity<HistoryDetailInfoResponseDto> getHistoryDetail(@PathVariable Long historyId) {
        // TODO: 로그인 구현 후 @AuthenticationPrincipal User user 추가 필요

        return ResponseEntity.ok(historyService.getHistoryDetail(historyId));
    }
}
