package com.hanamja.moa.api.controller.notification;

import com.hanamja.moa.api.dto.notification.response.NotificationResponseDto;
import com.hanamja.moa.api.dto.util.DataResponseDto;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.service.notification.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RequiredArgsConstructor
@Tag(name = "notifications", description = "알림 관련 API")
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "알림 조회하기", description = "알림 조회하기")
    @GetMapping
    public ResponseEntity<DataResponseDto<List<NotificationResponseDto>>> getNotifications(
            @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount
    ) {

        return ResponseEntity.ok(notificationService.getNotifications(userAccount));
    }

    @Operation(summary = "알림 확인하기", description = "알림 확인하기")
    @GetMapping("/check")
    public ResponseEntity<String> checkNotifications(
            @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount
    ) {

        return ResponseEntity.ok(notificationService.checkNotifications(userAccount));
    }
}
