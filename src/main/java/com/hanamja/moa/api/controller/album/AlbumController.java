package com.hanamja.moa.api.controller.album;

import com.hanamja.moa.api.dto.album.AlbumRespDto;
import com.hanamja.moa.api.dto.util.DataResponseDto;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.service.album.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "album", description = "앨범 관련 API")
@RestController
@RequestMapping("/api/album")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;
    /**
     * 승민 TODO
     * 1. 본인 앨범 보기 - GET
     * 2. 본인 앨범에서 card 하나 클릭했을 때 - GET
     */

    @Operation(summary = "앨범 정보 가져오기", description = "앨범 정보 가져오기")
    @GetMapping(value = "/all")
    public ResponseEntity<DataResponseDto<List<AlbumRespDto>>> getMyAlbum(@AuthenticationPrincipal UserAccount userAccount){
        return ResponseEntity.ok().body(albumService.getMyAlbumInfo(userAccount.getUserId()));
    }

    @Operation(summary = "카드 정보 가져오기", description = "카드 정보 가져오기")
    @GetMapping(value = "/card/{met_user_id}")
    public ResponseEntity<?> getCardInfo(@AuthenticationPrincipal UserAccount userAccount,
                                         @PathVariable(value = "met_user_id") Long metUserId){
        return ResponseEntity.ok().body(albumService.getCardInfo(userAccount.getUserId(), metUserId));
    }
}
