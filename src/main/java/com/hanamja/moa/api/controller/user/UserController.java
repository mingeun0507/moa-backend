package com.hanamja.moa.api.controller.user;

import com.hanamja.moa.api.dto.user.response.UserInfoResponseDto;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.service.user.UserService;
import com.hanamja.moa.utils.s3.AmazonS3Uploader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Tag(name = "user", description = "유저 관련 API")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    private final AmazonS3Uploader amazonS3Uploader;

    @Operation(summary = "내 정보 조회")
    @GetMapping(value = "/my-info")
    public ResponseEntity<UserInfoResponseDto> myInfo(@Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount) {

        UserInfoResponseDto responseDto = userService.getMyInfo(userAccount.getUserId());

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "프로필 사진 수정")
    @PutMapping(value = "/profile-image")
    public ResponseEntity<UserInfoResponseDto> updateProfileImage(
            @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount, MultipartFile profileImage) throws IOException {

        String profileImageUrl = amazonS3Uploader.saveFileAndGetUrl(profileImage);

        UserInfoResponseDto responseDto = userService.updateProfileImage(userAccount.getUserId(), profileImageUrl);

        return ResponseEntity.ok(responseDto);
    }
}
