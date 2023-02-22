package com.hanamja.moa.api.controller.album;

import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.service.album.AlbumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping(value = "/api/album")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;
    /**
     * 승민 TODO
     * 1. 본인 앨범 보기 - GET
     * 2. 본인 앨범에서 card 하나 클릭했을 때 - GET
     */

    // TODO: uid -> @AuthenticationPrincipal 변경 필요
    @GetMapping(value = "/all")
    public ResponseEntity<?> getMyAlbum(@AuthenticationPrincipal UserAccount userAccount){
        return ResponseEntity.ok().body(albumService.getMyAlbumInfo(userAccount.getUserId()));
    }

    // TODO: uid -> @AuthenticationPrincipal 변경 필요
    @GetMapping(value = "/card/{card_id}")
    public ResponseEntity<?> getCardInfo(@AuthenticationPrincipal UserAccount userAccount,
                                         @PathVariable(value = "card_id")Long cardId){
        return ResponseEntity.ok().body(albumService.getCardInfo(userAccount.getUserId(), cardId));
    }
}
