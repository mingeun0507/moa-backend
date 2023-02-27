package com.hanamja.moa.api.service.album;

import com.hanamja.moa.api.dto.album.AlbumRespDto;
import com.hanamja.moa.api.dto.album.CardRespDto;
import com.hanamja.moa.api.dto.util.DataResponseDto;
import com.hanamja.moa.api.entity.album.Album;
import com.hanamja.moa.api.entity.album.AlbumRepository;
import com.hanamja.moa.api.entity.group.State;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.user.UserRepository;
import com.hanamja.moa.api.entity.user_group.UserGroup;
import com.hanamja.moa.api.entity.user_group.UserGroupRepository;
import com.hanamja.moa.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlbumService {
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final AlbumRepository albumRepository;

    @Transactional(readOnly = true)
    public DataResponseDto<List<AlbumRespDto>> getMyAlbumInfo(Long uid){
        List<AlbumRespDto> response = new ArrayList<>();
        // state가 done이고 joiner에 uid가 포함된 group 들의 user group 모두 찾기
        userGroupRepository.findAllDoneGroupJoinUserId(uid, State.DONE).stream()
                .forEach(userGroup -> {
                    Album album = albumRepository.findByOwner_IdAndMetUser_Id(uid, userGroup.getJoiner().getId())
                            .orElseThrow(() -> NotFoundException.builder()
                                    .httpStatus(HttpStatus.BAD_REQUEST)
                                    .message("album 정보를 찾을 수 없습니다.")
                                    .build());

                    int meetingCnt = userGroupRepository.findOnePersonCard(uid, userGroup.getJoiner().getId(), State.DONE).size();

                    response.add(AlbumRespDto.builder()
                            .userId(userGroup.getJoiner().getId())
                            .username(userGroup.getJoiner().getName())
                            .imageLink(userGroup.getJoiner().getImageLink())
                            .meetingCnt(meetingCnt)
                            .isBadged(album.getIsBadged())
                            .build());
                });

        return DataResponseDto.<List<AlbumRespDto>>builder()
                .data(response)
                .build();
    }

    @Transactional(readOnly = true)
    public DataResponseDto<CardRespDto> getCardInfo(Long uid, Long metUserId){
        List<CardRespDto.CardInfo> cardInfos = new ArrayList<>();
        User metUser = userRepository.findUserById(metUserId).orElseThrow(
                () -> new NotFoundException("해당하는 사용자를 찾을 수 없습니다.")
        );

        List<UserGroup> onePersonCard = userGroupRepository.findOnePersonCard(uid, metUserId, State.DONE);
        onePersonCard.stream()
                     .forEach(userGroup -> {
                            cardInfos.add(CardRespDto.CardInfo.builder()
                                            .meetingAt(userGroup.getGroup().getMeetingAt())
                                            .frontImage(userGroup.getMeetingImg())
                                            .backImage(userGroup.getGroup().getImageLink())
                                            .build());
                        });

        return DataResponseDto.<CardRespDto>builder()
                .data(CardRespDto.builder()
                        .userId(metUser.getId())
                        .username(metUser.getName())
                        .meetingCnt(Long.valueOf(onePersonCard.size()))
                        .cards(cardInfos)
                        .build())
                .build();
    }
}
