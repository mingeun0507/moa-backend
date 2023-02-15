package com.hanamja.moa.api.controller.album;

import com.hanamja.moa.api.service.album.AlbumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequestMapping(value = "/album")
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
    public ResponseEntity<?> getMyAlbum(@RequestParam(value = "uid")Long uid){
        return ResponseEntity.ok().body(albumService.getMyAlbumInfo(uid));
    }

    // TODO: uid -> @AuthenticationPrincipal 변경 필요
    @GetMapping(value = "/card")
    public ResponseEntity<?> getCardInfo(@RequestParam(value = "uid")Long uid,
                                         @RequestParam(value = "card_id")Long cardId){
        return ResponseEntity.ok().body(albumService.getCardInfo(uid, cardId));
    }
}
