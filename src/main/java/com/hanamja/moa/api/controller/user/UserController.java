package com.hanamja.moa.api.controller.user;

import com.hanamja.moa.api.dto.user.response.UserInfoResponseDto;
import com.hanamja.moa.api.service.user.UserService;
import com.hanamja.moa.utils.s3.AmazonS3Uploader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "user", description = "유저 관련 API")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    private final AmazonS3Uploader amazonS3Uploader;

    @Operation(summary = "유저 정보 조회")
    @GetMapping(value = "/{userId}/info")
    public ResponseEntity<UserInfoResponseDto> myInfo(@PathVariable("userId") Long userId) {

        UserInfoResponseDto responseDto = userService.getUserInfo(userId);

        return ResponseEntity.ok(responseDto);
    }
}