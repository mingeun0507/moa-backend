package com.hanamja.moa.api.service.album;

import com.hanamja.moa.api.dto.group.response.AlbumRespDto;
import com.hanamja.moa.api.dto.group.response.CardRespDto;
import com.hanamja.moa.api.entity.album.AlbumRepository;
import com.hanamja.moa.api.entity.group.Group;
import com.hanamja.moa.api.entity.group.GroupRepository;
import com.hanamja.moa.api.entity.user.UserRepository;
import com.hanamja.moa.api.entity.user_group.UserGroup;
import com.hanamja.moa.api.entity.user_group.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlbumService {
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final AlbumRepository albumRepository;
    private final GroupRepository groupRepository;

    @Transactional(readOnly = true)
    public List<AlbumRespDto> getMyAlbumInfo(Long uid){
        List<AlbumRespDto> response = new ArrayList<>();

        List<Long> groupList = userGroupRepository.findAllByJoiner_IdAndProgress(uid, "DONE").stream()
                .map(UserGroup::getId).collect(Collectors.toList());

        albumRepository.findAllByOwner_Id(uid).stream()
                .forEach(album -> {
                    List<Long> albumUserGroupList = userGroupRepository.findAllByJoiner_IdAndProgress(album.getMetUser().getId(), "DONE").stream()
                            .map(UserGroup::getId).collect(Collectors.toList());

                    albumUserGroupList.retainAll(groupList);
                    Boolean isBadged = album.getIsBadged();

                    response.add(AlbumRespDto.builder()
                            .userId(album.getMetUser().getId())
                            .username(album.getMetUser().getUsername())
                            .imageLink(album.getMetUser().getImageLink())
                            .meetingCnt(albumUserGroupList.size())
                            .isBadged(isBadged)
                            .build());
                });

        return response;
    }

    @Transactional(readOnly = true)
    public List<CardRespDto> getCardInfo(Long uid, Long cardId){
        List<CardRespDto> response = new ArrayList<>();
        // uid, cardId 가 모두 속해있는 group_id 와 매핑된 group 의 인증사진, 만남일자 가져오기
        List<Long> uidGroupList = userGroupRepository.findAllByJoiner_IdAndProgress(uid, "DONE").stream()
                .map(UserGroup::getGroup).map(Group::getId).collect(Collectors.toList());

        userGroupRepository.findAllByJoiner_IdAndProgress(cardId, "DONE").stream()
                .map(UserGroup::getGroup).forEach(group -> {
                    if (uidGroupList.contains(group.getId())){
                        response.add(CardRespDto.builder()
                                .imageLink(group.getImageLink()).date(String.valueOf(group.getMeetingAt()))
                                .build());
                    }
                });
        return response;
    }
}
