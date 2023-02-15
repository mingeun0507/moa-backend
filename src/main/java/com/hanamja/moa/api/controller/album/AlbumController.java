package com.hanamja.moa.api.controller.album;

import com.hanamja.moa.api.dto.group.response.AlbumRespDto;
import com.hanamja.moa.api.dto.group.response.CardRespDto;
import com.hanamja.moa.api.service.album.AlbumService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
    @GetMapping(value = "/all")
    public ResponseEntity<?> getMyAlbum(@RequestParam(value = "uid")Long uid){
        List<AlbumRespDto> myAlbumInfo = albumService.getMyAlbumInfo(uid);
        return ResponseEntity.ok().body(myAlbumInfo);
    }

    @GetMapping(value = "/card")
    public ResponseEntity<?> getCardInfo(@RequestParam(value = "uid")Long uid,
                                         @RequestParam(value = "card_id")Long cardId){
        List<CardRespDto> cardInfo = albumService.getCardInfo(uid, cardId);
        return ResponseEntity.ok().body(cardInfo);
    }
}
