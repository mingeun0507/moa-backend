package com.hanamja.moa.api.controller.point;

import com.hanamja.moa.api.dto.point.request.AddPointRequestDto;
import com.hanamja.moa.api.dto.point.response.PointResponseDto;
import com.hanamja.moa.api.entity.user.Role;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.service.point.PointService;
import com.hanamja.moa.exception.custom.UnauthorizedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "album", description = "포인트 관련 API")
@RestController
@RequestMapping("/api/point")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @Operation(summary = "포인트 추가", description = "포인트 추가(어드민 계정으로만 호출 가능)")
    @PostMapping("/add")
    public ResponseEntity<PointResponseDto> addPoint(
            @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
            @Validated @RequestBody AddPointRequestDto addPointRequestDto)
    {
        //TODO 어드민 권한 필터 추가하기

        if (userAccount.getRole() != Role.ROLE_ADMIN) {
            log.error(userAccount.getUserId() + " " + userAccount.getStudentId() + " " + "어드민 권한의 유저가 아닙니다.");
            throw UnauthorizedException
                    .builder()
                        .httpStatus(HttpStatus.UNAUTHORIZED)
                        .message("어드민 권한의 유저가 아닙니다.")
                    .build();
        }

        return ResponseEntity.ok(pointService.addPoint(addPointRequestDto));
    }

}
