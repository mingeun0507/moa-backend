package com.hanamja.moa.api.service.group;

import com.hanamja.moa.api.dto.group.request.MakingGroupRequestDto;
import com.hanamja.moa.api.dto.group.request.ModifyingGroupRequestDto;
import com.hanamja.moa.api.dto.group.request.RemovingGroupRequestDto;
import com.hanamja.moa.api.dto.group.response.GroupCompleteRespDto;
import com.hanamja.moa.api.dto.group.response.GroupDetailInfoResponseDto;
import com.hanamja.moa.api.dto.group.response.GroupInfoListResponseDto;
import com.hanamja.moa.api.dto.group.response.GroupInfoResponseDto;
import com.hanamja.moa.api.entity.album.Album;
import com.hanamja.moa.api.entity.album.AlbumRepository;
import com.hanamja.moa.api.entity.group.Group;
import com.hanamja.moa.api.entity.group.GroupRepository;
import com.hanamja.moa.api.entity.group.State;
import com.hanamja.moa.api.entity.group_hashtag.GroupHashtag;
import com.hanamja.moa.api.entity.group_hashtag.GroupHashtagRepository;
import com.hanamja.moa.api.entity.hashtag.Hashtag;
import com.hanamja.moa.api.entity.hashtag.HashtagRepository;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.user.UserRepository;
import com.hanamja.moa.api.entity.user_group.UserGroup;
import com.hanamja.moa.api.entity.user_group.UserGroupRepository;
import com.hanamja.moa.exception.custom.InvalidMaxPeopleNumberException;
import com.hanamja.moa.exception.custom.InvalidParameterException;
import com.hanamja.moa.exception.custom.NotFoundException;
import com.hanamja.moa.exception.custom.UserInputException;
import com.hanamja.moa.utils.s3.AmazonS3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class GroupService {
    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final GroupRepository groupRepository;
    private final GroupHashtagRepository groupHashtagRepository;
    private final HashtagRepository hashtagRepository;
    private final AmazonS3Uploader amazonS3Uploader;

    @Transactional
    public GroupInfoResponseDto makeNewGroup(MakingGroupRequestDto makingGroupRequestDto) {
        // TODO: 로그인 구현 후 @AuthenticationPrincipal User user 추가 필요
        Long userId = 1L;
        User user = validateUser(userId);

        // 재학생인지 검증
        validateSenior(user);

        // Hashtag #으로 파싱 후 분리, 저장 (다른 메소드로 분리 구현)
        List<Hashtag> hashtagList = saveHashtags(makingGroupRequestDto.getHashtags());

        Group newGroup = MakingGroupRequestDto.toEntity(makingGroupRequestDto, user);
        groupRepository.save(newGroup);

        // GroupHashtag 생성 및 Group과의 관계 설정, 저장
        hashtagList.stream().map(
                x -> GroupHashtag
                        .builder()
                        .group(newGroup)
                        .hashtag(x)
                        .build()
        ).forEach(groupHashtagRepository::save);

        return GroupInfoResponseDto.from(newGroup, hashtagList.stream().map(Hashtag::getName).collect(Collectors.toList()));
    }

    @Transactional
    public GroupInfoResponseDto modifyExistingGroup(ModifyingGroupRequestDto modifyingGroupRequestDto) {
        // TODO: 로그인 구현 후 @AuthenticationPrincipal User user 추가 필요

        Long userId = 1L;
        User user = validateUser(userId);

        // 재학생인지 검증
        validateSenior(user);

        // groupId로 group 찾아오기
        Group existingGroup = groupRepository.findById(modifyingGroupRequestDto.getId()).orElseThrow(
                () -> NotFoundException
                        .builder()
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message("존재하지 않는 groupId입니다.")
                        .build()
        );

        validateMaker(user, existingGroup);

        if (existingGroup.getCurrentPeopleNum() > modifyingGroupRequestDto.getMaxPeopleNum()) {
            throw InvalidMaxPeopleNumberException
                    .builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("모임에 신청한 인원 수보다 적게 줄일 수 없습니다.")
                    .build();
        }

        // group 정보 수정
        existingGroup.modifyGroupInfo(
                modifyingGroupRequestDto.getName(),
                modifyingGroupRequestDto.getDescription(),
                modifyingGroupRequestDto.getMeetingAt(),
                modifyingGroupRequestDto.getMaxPeopleNum()
        );

        // 기존 해시태그 정보 삭제
        groupHashtagRepository.deleteAllByGroup_Id(existingGroup.getId());

        // 새로운 해시태그 정보 등록 및 관계 테이블에 저장
        List<Hashtag> hashtagList = saveHashtags(modifyingGroupRequestDto.getHashtags());
        hashtagList.stream().map(
                x -> GroupHashtag
                        .builder()
                        .group(existingGroup)
                        .hashtag(x)
                        .build()
        ).forEach(groupHashtagRepository::save);

        // 수정된 group 정보 반영
        return GroupInfoResponseDto.from(existingGroup, hashtagList.stream().map(Hashtag::getName).collect(Collectors.toList()));
    }

    @Transactional
    public GroupInfoResponseDto removeExistingGroup(RemovingGroupRequestDto removingGroupRequestDto) {
        Long userId = 1L;
        User user = validateUser(userId);

        // 재학생인지 검증
        validateSenior(user);

        // groupId로 group 찾아오기
        Group existingGroup = groupRepository.findById(removingGroupRequestDto.getId()).orElseThrow(
                () -> NotFoundException
                        .builder()
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message("일시적 오류입니다. 다시 시도해주세요.")
                        .build()
        );

        List<String> hashtagStringList = getHashtagStringList(existingGroup);

        GroupInfoResponseDto removedGroupDto =
                GroupInfoResponseDto.from(existingGroup, hashtagStringList);


        // UserGroup, GroupHashtag에서 해당 Group 모두 삭제
        userGroupRepository.deleteAllByGroup_Id(removingGroupRequestDto.getId());
        groupHashtagRepository.deleteAllByGroup_Id(removingGroupRequestDto.getId());

        // Group 삭제
        groupRepository.deleteById(removingGroupRequestDto.getId());

        return removedGroupDto;
    }

    private User validateUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> NotFoundException
                        .builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("유효하지 않은 사용자입니다.")
                        .build()
        );
    }

    private void validateSenior(User user) {
        if (user.isFreshman()) {
            log.info("신입생의 모임 생성 시도 발생 - 학번: {}, 이름: {}", user.getStudentId(), user.getName());
            throw UserInputException
                    .builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .message("신입생은 모임을 생성할 수 없습니다.")
                    .build();
        }
    }

    private void validateMaker(User user, Group group) {
        if (!group.getMaker().getId().equals(user.getId())) {
            throw UserInputException
                    .builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .message("해당 모임에 접근할 권한이 없습니다.")
                    .build();
        }
    }

    private List<String> getHashtagStringList(Group existingGroup) {
        return groupHashtagRepository.findAllByGroup_Id(existingGroup.getId())
                .stream().map(x -> hashtagRepository.findById(x.getHashtag().getId()))
                .map(x -> x.orElseThrow().getName()).collect(Collectors.toList());
    }

    @Transactional
    protected List<Hashtag> saveHashtags(String hashtagString) {
        List<String> hashtagStringList = new java.util.ArrayList<>(List.of(hashtagString.split("#")));
        hashtagStringList.remove(0);

        return hashtagStringList.stream().map(
                x -> {
                    if (hashtagRepository.existsByName(x)) {
                        // 이미 같은 이름의 해시태그가 존재하는 경우 변경 시간 업데이트
                        Hashtag existingHashtag = hashtagRepository.findByName(x).orElseThrow();
                        existingHashtag.updateTouchedAt();

                        // 찾아온 해시태그를 리턴
                        return hashtagRepository.findByName(x).orElseThrow(
                                () -> NotFoundException
                                        .builder()
                                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .message("일시적인 오류입니다. 다시 시도해주세요.")
                                        .build()
                        );
                    } else {
                        // 같은 이름의 해시태그가 존재하지 않는 경우 새로운 해시태그 생성
                        return hashtagRepository.save(
                                Hashtag
                                        .builder()
                                        .name(x)
                                        .build()
                        );
                    }
                }
        ).collect(Collectors.toList());
    }

    public List<GroupInfoResponseDto> getExistingGroups(String sortedBy) {
        if (sortedBy.equals("recent")) {
            return groupRepository
                    .findAllByStateOrderByCreatedAtDesc(State.RECRUITING)
                    .stream().map(x -> GroupInfoResponseDto.from(x, getHashtagStringList(x)))
                    .collect(Collectors.toList());
        } else if (sortedBy.equals("soon")) {
            return groupRepository
                    .findAllByStateAndMeetingAtAfterOrderByMeetingAtAscCreatedAtDesc(State.RECRUITING, LocalDateTime.now())
                    .stream().map(x -> GroupInfoResponseDto.from(x, getHashtagStringList(x)))
                    .collect(Collectors.toList());
        } else {
            throw InvalidParameterException
                    .builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("올바르지 않은 Query String입니다.")
                    .build();
        }
    }

    public GroupDetailInfoResponseDto getExistingGroupDetail(Long id) {
        // TODO: 로그인 구현 후 @AuthenticationPrincipal User user 추가 필요
        Long userId = 1L;
        User user = validateUser(userId);

        Group existingGroup = groupRepository.findById(id).orElseThrow(
                () -> NotFoundException
                        .builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("groupId로 group을 찾을 수 없습니다.")
                        .build()
        );

        // 참여자들의 간단한 프로필 추가
        List<GroupDetailInfoResponseDto.SimpleUserInfoDto> simpleUserInfoDtoList =
                userGroupRepository.findAllByGroup_Id(id).stream()
                        .map(x -> GroupDetailInfoResponseDto.SimpleUserInfoDto.from(x.getJoiner()))
                        .collect(Collectors.toList());

        // 생성자의 간단한 프로필 추가
        simpleUserInfoDtoList.add(0, GroupDetailInfoResponseDto.SimpleUserInfoDto.from(existingGroup.getMaker()));

        // 포인트 정산
        int point = 0;

        // 모임 참여 3회까지는 점수 부여 - 300, 400, 500
        switch (userGroupRepository.countAllByJoiner_Id(user.getId())) {
            case 0:
                point += 300;
                break;
            case 1:
                point = 400;
                break;
            case 2:
                point = 500;
                break;
        }

        // 현재 참여자가 내 앨범에 저장된 사람이면 50, 아니면 100
        for (var x : simpleUserInfoDtoList) {
            if (albumRepository.existsByOwner_IdAndMetUser_Id(user.getId(), x.getId())) {
                point += 50;
            } else if (!user.getId().equals(x.getId())) {
                point += 100;
            }
        }

        return GroupDetailInfoResponseDto.from(existingGroup, getHashtagStringList(existingGroup), simpleUserInfoDtoList, point);
    }
        
    public GroupInfoResponseDto join(Long groupId, User user) {

        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new NotFoundException("해당 그룹을 찾을 수 없습니다.")
        );

        UserGroup userGroup = UserGroup
                .builder()
                .progress("")
                .joiner(user)
                .group(group)
                .build();

        userGroupRepository.save(userGroup);

        return GroupInfoResponseDto.from(group, getHashtagStringList(group));
    }

    public GroupInfoListResponseDto getMyGroupList(User user) {
        Long userId = user.getId();
        List<Group> groupList = groupRepository.findAllByUserId(userId);

        List<GroupInfoResponseDto> items = groupList.stream().map(x -> GroupInfoResponseDto.from(x, getHashtagStringList(x))).collect(Collectors.toList());

        return GroupInfoListResponseDto.of(items);
    }

    public GroupInfoResponseDto quit(Long groupId, User user) {

        UserGroup userGroup = userGroupRepository.findByGroupIdAndJoinerId(groupId, user.getId())
                .orElseThrow(() -> new NotFoundException("해당 그룹을 찾을 수 없습니다."));

        Group group = userGroup.getGroup();
        userGroupRepository.delete(userGroup);

        return GroupInfoResponseDto.from(group, getHashtagStringList(group));
    }

    @Transactional
    public GroupCompleteRespDto completeGroup(Long uid, Long gid, MultipartFile image) throws IOException {
        if(groupRepository.existsByIdAndMaker_Id(gid, uid)){
            String imageLink = amazonS3Uploader.saveFileAndGetUrl(image);

            // Group 이랑 UserGroup 이랑 join 해서 업데이트 같이하고싶은데 생각이 안난다...
            groupRepository.updateGroupImage(imageLink, gid);
            userGroupRepository.updateProgress("DONE", gid);

            // gid 받아서 group 에 속해있는 user 앨범 findAll
            // 앨범마다 group 유저 정보 체크해서 없으면 카드 추가
            // 있으면 badged -> true 로 변경
            List<User> groupJoinUsers = userGroupRepository.findAllByGroup_Id(gid).stream()
                    .map(UserGroup::getJoiner).collect(Collectors.toList());

            groupJoinUsers.forEach(albumOwner -> {
                List<User> metUsers = albumRepository.findAllByOwner_Id(albumOwner.getId()).stream()
                        .map(Album::getMetUser).collect(Collectors.toList());

                groupJoinUsers.forEach(groupJoinUser -> {
                    if(albumOwner.getId() != groupJoinUser.getId() && !metUsers.contains(groupJoinUser)){
                        albumRepository.save(Album.builder()
                                .owner(albumOwner).metUser(groupJoinUser).isBadged(true)
                                .build());
                    }else{
                        albumRepository.updateBadgeState(true, groupJoinUser.getId(), albumOwner.getId());
                    }
                });
            });

            // 위에 update 과정 끝나면 gid 로 user info 토대로 response data 구축하기
            // 모임에 참여했던 user 의 프사, 이름, 만난 날짜, 위에서 저장한 모임 사진 url
            List<GroupCompleteRespDto.MemberInfo> memberInfoList = new ArrayList<>();

            List<Long> groupIdList = userGroupRepository.findAllByJoiner_IdAndProgress(uid,"DONE").stream()
                    .map(UserGroup::getId).collect(Collectors.toList());

            groupJoinUsers.forEach(user -> {
                if (!user.getId().equals(uid)) {
                    // 만난 횟수 : uid 가 참여한 group_id 리스트 중에 user.getId()가
                    // 속한 group_id 리스트 중에 겹치는거 중에 progress 가 DONE 인 것
                    List<Long> userGroupIdList = userGroupRepository.findAllByJoiner_IdAndProgress(user.getId(), "DONE").stream()
                            .map(UserGroup::getId).collect(Collectors.toList());

                    userGroupIdList.retainAll(groupIdList); // 교집합

                    memberInfoList.add(GroupCompleteRespDto.MemberInfo.builder()
                            .userId(user.getId()).username(user.getUsername())
                            .imageLink(user.getImageLink()).meetingCnt((long) userGroupIdList.size())
                            .build());
                }
            });
            return GroupCompleteRespDto.builder()
                    .memberInfoList(memberInfoList).groupImageLink(imageLink)
                    .build();
        }else{
            // throw new 잘못된 요청 case 리턴하기
            return GroupCompleteRespDto.builder()
                    .memberInfoList(null).groupImageLink(null)
                    .build();
        }
    }
}
