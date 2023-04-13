package com.hanamja.moa.api.service.group;

import com.hanamja.moa.api.controller.group.SortedBy;
import com.hanamja.moa.api.dto.comment.request.WritingCommentRequestDto;
import com.hanamja.moa.api.dto.comment.response.CommentInfoResponseDto;
import com.hanamja.moa.api.dto.group.request.KickOutRequestDto;
import com.hanamja.moa.api.dto.group.request.MakingGroupRequestDto;
import com.hanamja.moa.api.dto.group.request.ModifyingGroupRequestDto;
import com.hanamja.moa.api.dto.group.response.*;
import com.hanamja.moa.api.dto.util.DataResponseDto;
import com.hanamja.moa.api.entity.album.Album;
import com.hanamja.moa.api.entity.album.AlbumRepository;
import com.hanamja.moa.api.entity.comment.Comment;
import com.hanamja.moa.api.entity.comment.CommentRepository;
import com.hanamja.moa.api.entity.department.Department;
import com.hanamja.moa.api.entity.department.DepartmentRepository;
import com.hanamja.moa.api.entity.group.Group;
import com.hanamja.moa.api.entity.group.GroupRepository;
import com.hanamja.moa.api.entity.group.State;
import com.hanamja.moa.api.entity.group_hashtag.GroupHashtag;
import com.hanamja.moa.api.entity.group_hashtag.GroupHashtagRepository;
import com.hanamja.moa.api.entity.hashtag.Hashtag;
import com.hanamja.moa.api.entity.hashtag.HashtagRepository;
import com.hanamja.moa.api.entity.notification.Notification;
import com.hanamja.moa.api.entity.notification.NotificationRepository;
import com.hanamja.moa.api.entity.point_history.PointHistory;
import com.hanamja.moa.api.entity.point_history.PointHistoryRepository;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class GroupService {
    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final UserGroupRepository userGroupRepository;
    private final GroupRepository groupRepository;
    private final GroupHashtagRepository groupHashtagRepository;
    private final HashtagRepository hashtagRepository;
    private final NotificationRepository notificationRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final CommentRepository commentRepository;
    private final AmazonS3Uploader amazonS3Uploader;

    public void validateChannel(UserAccount userAccount, Long groupDepartmentId){
        if (!userAccount.getDepartmentId().equals(groupDepartmentId)){
            throw InvalidParameterException
                    .builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("해당 모임의 채널이 아닙니다.")
                    .build();
        }
    }
    @Transactional
    public GroupInfoResponseDto makeNewGroup(UserAccount userAccount, MakingGroupRequestDto makingGroupRequestDto) {

        User user = validateUser(userAccount.getUserId());

        // Hashtag #으로 파싱 후 분리, 저장 (다른 메소드로 분리 구현)
        List<Hashtag> hashtagList = saveHashtags(makingGroupRequestDto.getHashtags());
        Department department = departmentRepository.findById(userAccount.getDepartmentId()).orElseThrow(
                () -> NotFoundException
                        .builder()
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message("존재하지 않는 채널입니다.")
                        .build()
        );
        Group newGroup = MakingGroupRequestDto.toEntity(makingGroupRequestDto, department, user);
        groupRepository.save(newGroup);

        UserGroup userGroup = UserGroup
                .builder()
                .progress("")
                .joiner(user)
                .group(newGroup)
                .build();

        userGroupRepository.save(userGroup);
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
    public GroupInfoResponseDto modifyExistingGroup(UserAccount userAccount, ModifyingGroupRequestDto modifyingGroupRequestDto) {
        User user = validateUser(userAccount.getUserId());

        // 재학생인지 검증
//        validateSenior(user);

        // groupId로 group 찾아오기
        Group existingGroup = groupRepository.findById(modifyingGroupRequestDto.getId()).orElseThrow(
                () -> NotFoundException
                        .builder()
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message("존재하지 않는 groupId입니다.")
                        .build()
        );

        if (!userAccount.getDepartmentId().equals(existingGroup.getDepartment().getId())){
            throw InvalidParameterException
                    .builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("해당 모임의 채널이 아닙니다.")
                    .build();
        }
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
    public GroupInfoResponseDto removeExistingGroup(UserAccount userAccount, Long groupId) {
        User user = validateUser(userAccount.getUserId());

        // 재학생인지 검증
//        validateSenior(user);

        // groupId로 group 찾아오기
        Group existingGroup = groupRepository.findById(groupId).orElseThrow(
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
        userGroupRepository.deleteAllByGroup_Id(groupId);
        groupHashtagRepository.deleteAllByGroup_Id(groupId);

        // Group 삭제
        groupRepository.deleteById(groupId);

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

    private Group validateGroup(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(
                () -> NotFoundException
                        .builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("유효하지 않은 모임입니다.")
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

    private Comment validateComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> NotFoundException
                        .builder()
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .message("존재하지 않는 댓글입니다.")
                        .build()
        );
    }

    private List<String> getHashtagStringList(Group existingGroup) {
        return groupHashtagRepository.findAllByGroup_Id(existingGroup.getId())
                .stream().map(x -> hashtagRepository.findById(x.getHashtag().getId()))
                .map(x -> x.orElseThrow().getName()).collect(Collectors.toList());
    }

    @Transactional
    protected List<Hashtag> saveHashtags(String hashtagString) {
        List<String> hashtagStringList = hashtagString != null ? new ArrayList<>(List.of(hashtagString.split("#"))) : new ArrayList<>();
        if (hashtagStringList.size() > 0) {
            hashtagStringList.remove(0);
        }

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

    public DataResponseDto<List<GroupInfoResponseDto>> getExistingGroups(SortedBy sortedBy) {
        if (sortedBy == SortedBy.RECENT) {
            List<GroupInfoResponseDto> resultDtoList = groupRepository
                    .findExistingGroupsByRECENT(State.RECRUITING, LocalDateTime.now())
                    .stream().map(x -> GroupInfoResponseDto.from(x, getHashtagStringList(x)))
                    .collect(Collectors.toList());
            return DataResponseDto.<List<GroupInfoResponseDto>>builder()
                    .data(resultDtoList).build();
        } else if (sortedBy == SortedBy.SOON) {
            List<GroupInfoResponseDto> resultDtoList = groupRepository
                    .findAllByStateAndMeetingAtAfterOrderByMeetingAtAscCreatedAtDesc(State.RECRUITING, LocalDateTime.now())
                    .stream().map(x -> GroupInfoResponseDto.from(x, getHashtagStringList(x)))
                    .collect(Collectors.toList());
            resultDtoList.addAll(groupRepository
                    .findAllByStateAndMeetingAtOrderByCreatedAtDesc(State.RECRUITING, null)
                    .stream().map(x -> GroupInfoResponseDto.from(x, getHashtagStringList(x)))
                    .collect(Collectors.toList()));
            return DataResponseDto.<List<GroupInfoResponseDto>>builder()
                    .data(resultDtoList).build();
        } else if (sortedBy == SortedBy.PAST) {
            List<GroupInfoResponseDto> resultDtoList = groupRepository
                    .findAllByStateAndMeetingAtBeforeOrderByCreatedAtDesc(State.DONE, LocalDateTime.now())
                    .stream().map(x -> GroupInfoResponseDto.from(x, getHashtagStringList(x)))
                    .collect(Collectors.toList());
            return DataResponseDto.<List<GroupInfoResponseDto>>builder()
                    .data(resultDtoList).build();
        } else {
            throw InvalidParameterException
                    .builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("올바르지 않은 Query String입니다.")
                    .build();
        }
    }

    @Transactional(readOnly = true)
    public DataResponseDto<List<GroupInfoResponseDto>> getExistingGroups2(UserAccount userAccount, SortedBy sortedBy, Long cursor, Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        if (sortedBy == SortedBy.RECENT || sortedBy == SortedBy.PAST) {
            List<GroupInfoResponseDto> resultDtoList = groupRepository.findAllByPageAndSort(userAccount, now, sortedBy, cursor, pageable).stream()
                    .map(x -> GroupInfoResponseDto.from(x, getHashtagStringList(x)))
                    .collect(Collectors.toList());
            return DataResponseDto.<List<GroupInfoResponseDto>>builder()
                    .data(resultDtoList).build();
        } else if (sortedBy == SortedBy.SOON) {
            List<GroupInfoResponseDto> resultDtoList = groupRepository
                    .findAllByStateAndDepartment_IdAndMeetingAtAfterOrderByMeetingAtAscCreatedAtDesc(State.RECRUITING, userAccount.getDepartmentId(), LocalDateTime.now())
                    .stream().map(x -> GroupInfoResponseDto.from(x, getHashtagStringList(x)))
                    .collect(Collectors.toList());
            resultDtoList.addAll(groupRepository
                    .findAllByStateAndMeetingAtOrderByCreatedAtDesc(State.RECRUITING, null)
                    .stream().map(x -> GroupInfoResponseDto.from(x, getHashtagStringList(x)))
                    .collect(Collectors.toList()));
            return DataResponseDto.<List<GroupInfoResponseDto>>builder()
                    .data(resultDtoList).build();
        } else {
            throw InvalidParameterException
                    .builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("올바르지 않은 Query String입니다.")
                    .build();
        }
    }

    public GroupDetailInfoResponseDto getExistingGroupDetail(UserAccount userAccount, Long groupId) {
        User user = validateUser(userAccount.getUserId());

        Group existingGroup = groupRepository.findById(groupId).orElseThrow(
                () -> NotFoundException
                        .builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("groupId로 group을 찾을 수 없습니다.")
                        .build()
        );

        // 참여자들의 간단한 프로필 추가
        List<SimpleUserInfoResponseDto> simpleUserInfoDtoList =
                userGroupRepository.findAllByGroup_Id(groupId).stream()
                        .map(x -> SimpleUserInfoResponseDto.from(x.getJoiner()))
                        .collect(Collectors.toList());

        // 포인트 정산
        int point = 0;

        // 모임 참여 3회까지는 점수 부여 - 300, 400, 500
        switch (userGroupRepository.countAllByJoiner_IdAndGroup_State(user.getId(), State.DONE)) {
            case 0:
                point = 300;
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
            if (x.getId().equals(user.getId())) {
                continue;
            }
            if (albumRepository.existsByOwner_IdAndMetUser_Id(user.getId(), x.getId())) {
                point += 50;
            } else if (!user.getId().equals(x.getId())) {
                point += 100;
            }
        }

        return GroupDetailInfoResponseDto.from(existingGroup, getHashtagStringList(existingGroup), simpleUserInfoDtoList, point, getRecentCommentDtoFromGroup(existingGroup));
    }

    public GroupDetailInfoResponseDto getPublicExistingGroupDetail(Long groupId) {
        Group existingGroup = groupRepository.findById(groupId).orElseThrow(
                () -> NotFoundException
                        .builder()
                        .httpStatus(HttpStatus.BAD_REQUEST)
                        .message("groupId로 group을 찾을 수 없습니다.")
                        .build()
        );

        // 참여자들의 간단한 프로필 추가
        List<SimpleUserInfoResponseDto> simpleUserInfoDtoList =
                userGroupRepository.findAllByGroup_Id(groupId).stream()
                        .map(x -> SimpleUserInfoResponseDto.from(x.getJoiner()))
                        .collect(Collectors.toList());

        return GroupDetailInfoResponseDto.from(existingGroup, getHashtagStringList(existingGroup), simpleUserInfoDtoList, getRecentCommentDtoFromGroup(existingGroup));
    }
        
    public GroupInfoResponseDto join(Long groupId, UserAccount userAccount) {

        Group group = groupRepository.findById(groupId).orElseThrow(
                () -> new NotFoundException(HttpStatus.BAD_REQUEST, "해당 그룹을 찾을 수 없습니다.")
        );

        User user = userRepository.findUserById(userAccount.getUserId())
                .orElseThrow(() -> NotFoundException.builder()
                        .httpStatus(HttpStatus.NOT_FOUND)
                        .message("해당 유저를 찾을 수 없습니다.")
                        .build());

        User groupMaker = group.getMaker();
        String groupMakerName = groupMaker.getName();
        String groupName = group.getName();

        if (userGroupRepository.existsByGroupIdAndJoinerId(groupId, userAccount.getUserId())) {
            throw UserInputException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("이미 참여한 그룹입니다.").build();
        }

        if (group.getState() == State.RECRUITED || group.getState() == State.DONE) {
            throw UserInputException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("모집이 마감된 그룹입니다.").build();
        }

        if (group.getMeetingAt() != null && group.getMeetingAt().isBefore(LocalDateTime.now())) {
            throw UserInputException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("모임이 이미 시작된 그룹입니다.").build();
        }

        if (group.getMaxPeopleNum() <= group.getUserGroupList().size()) {
            throw UserInputException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("모집인원이 초과된 그룹입니다.").build();
        }
        UserGroup userGroup = UserGroup
                .builder()
                .progress("")
                .joiner(user)
                .group(group)
                .build();
        userGroupRepository.save(userGroup);

        Long currNum = Long.valueOf(userGroupRepository.findAllByGroup_Id(groupId).size());

        // 모임 정원이 꽉 찼을 때 알림 생성
        if (currNum.equals(group.getMaxPeopleNum())) {
            userGroupRepository.findAllByGroup_Id(group.getId()).stream()
                    .map(UserGroup::getJoiner)
                    .forEach(joiner -> {
                        notificationRepository.save(
                                Notification
                                        .builder()
                                        .sender(groupMaker)
                                        .receiver(joiner)
                                        .content(groupName + " 모임의 정원이 꽉 찼어요. 우리 한번 모여볼까요?")
                                        .reason("모임 생성자: " + groupMakerName + "님")
                                        .build()
                        );

                        joiner.notifyUser();
                        userRepository.save(joiner);
                    });
        }
        group.updateCurrentPeopleNum(currNum);
        groupRepository.save(group);

        return GroupInfoResponseDto.from(group, getHashtagStringList(group));
    }

    public DataResponseDto<List<GroupStateInfoResponseDto>> getMyGroupList(Long userId) {
        List<GroupStateInfoResponseDto> dto = new ArrayList<>();

        Arrays.stream(State.values()).collect(Collectors.toList())
                        .stream()
                        .forEach(state -> {
                            List<GroupInfoResponseDto> groupInfos = groupRepository.findAllJoinGroupByUserId(userId, state).stream()
                                    .map(group -> GroupInfoResponseDto.from(group, getHashtagStringList(group)))
                                    .collect(Collectors.toList());

                            dto.add(GroupStateInfoResponseDto.builder()
                                            .state(state).groups(groupInfos)
                                            .build());
                        });
        return DataResponseDto.<List<GroupStateInfoResponseDto>>builder()
                .data(dto)
                .build();
    }

    @Transactional
    public GroupInfoResponseDto quit(Long groupId, UserAccount userAccount) {

        UserGroup userGroup = userGroupRepository.findByGroupIdAndJoinerId(groupId, userAccount.getUserId())
                .orElseThrow(() -> new NotFoundException(HttpStatus.BAD_REQUEST, "해당 그룹을 찾을 수 없습니다."));

        Group group = userGroup.getGroup();
        if(group.getState().equals(State.RECRUITING)){
            group.subtractCurrentPeopleNum();
            userGroupRepository.delete(userGroup);
            groupRepository.save(group);
        } else {
            throw UserInputException.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("모임이 이미 모집 완료되었습니다.")
                    .build();
        }

        return GroupInfoResponseDto.from(group, getHashtagStringList(group));
    }

    public void validateGroupMaker(Long userId, Long groupId){
        Group group = groupRepository.findById(groupId)
                .orElseThrow(
                        () -> NotFoundException.builder()
                                .httpStatus(HttpStatus.BAD_REQUEST)
                                .message("groupId로 group을 찾을 수 없습니다.")
                                .build());

        if (!group.getMaker().getId().equals(userId)) {
            throw NotFoundException.builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .message("Group 생성자가 아닙니다.")
                    .build();
        }
    }

    @Transactional(readOnly = true)
    public GroupInfoListResponseDto getGroupListMadeByMe(Long userId){
        List<GroupInfoResponseDto> response = groupRepository.findAllByMaker_Id(userId).stream()
                .map(group -> GroupInfoResponseDto.from(group, getHashtagStringList(group)))
                .collect(Collectors.toList());
        return GroupInfoListResponseDto.of(response);
    }

    @Transactional
    public GroupInfoResponseDto kickOutMemberFromGroup(Long uid, KickOutRequestDto kickOutRequestDto) {
        User existingUser = validateUser(uid);
        validateGroupMaker(uid, kickOutRequestDto.getGroupId());
        Group existingGroup = groupRepository.findById(kickOutRequestDto.getGroupId()).orElseThrow();

        kickOutRequestDto.getUserList()
                .forEach(userId -> {
                            userGroupRepository.deleteUserGroupByGroup_IdAndJoiner_Id(kickOutRequestDto.getGroupId(), userId);

                            // 각 유저에게 알림 보내기
                            User receiver = userRepository.findById(userId).orElseThrow(
                                    () -> NotFoundException.builder()
                                            .httpStatus(HttpStatus.BAD_REQUEST)
                                            .message("userId로 user를 찾을 수 없습니다.")
                                            .build()
                            );

                            notificationRepository.save(
                                    Notification
                                            .builder()
                                            .sender(existingUser)
                                            .receiver(receiver)
                                            .content(existingUser.getName() + "님이 '" + existingGroup.getName() + "'에서 " + receiver.getName() + "님을 내보냈어요.")
                                            .reason(kickOutRequestDto.getReason())
                                            .build()
                            );

                    receiver.notifyUser();
                    userRepository.save(receiver);
                        }
                );

        Long currNum = Long.valueOf(userGroupRepository.findAllByGroup_Id(kickOutRequestDto.getGroupId()).size());
        existingGroup.updateCurrentPeopleNum(currNum);

        return GroupInfoResponseDto.from(existingGroup, getHashtagStringList(existingGroup));
    }

    @Transactional
    public GroupInfoResponseDto cancelGroup(Long uid, Long gid) {
        User groupMaker = validateUser(uid);
        validateGroupMaker(uid, gid);
        Group existingGroup = groupRepository.findById(gid).orElseThrow();

        // 모임 취소 알림 보내기
        userGroupRepository.findAllByGroup_Id(gid).stream()
                .map(UserGroup::getJoiner)
                .forEach(joiner -> {
                    notificationRepository.save(
                            Notification
                                    .builder()
                                    .sender(groupMaker)
                                    .receiver(joiner)
                                    .content(groupMaker.getName() + "님이 '" + existingGroup.getName() + "'을 취소했어요.")
                                    .reason("모임 생성자: " + groupMaker.getName() + "님")
                                    .build()
                    );

                    joiner.notifyUser();
                    userRepository.save(joiner);
                });
        List<String> hashtagStringList = getHashtagStringList(existingGroup);

        commentRepository.deleteAllByGroup_Id(gid);
        groupHashtagRepository.deleteAllByGroup_Id(gid);
        userGroupRepository.deleteAllByGroup_Id(gid);
        groupRepository.deleteById(gid);

        return GroupInfoResponseDto.from(existingGroup, hashtagStringList);
    }

    @Transactional
    public void groupRecruitDone(Long uid, Long gid){
        validateGroupMaker(uid, gid);
        Group existingGroup = groupRepository.findById(gid).orElseThrow(
                () ->
                        NotFoundException.builder()
                                .httpStatus(HttpStatus.NOT_FOUND)
                                .message("groupId로 모임을 찾을 수 없습니다.")
                                .build()
        );

        String groupMakerName = existingGroup.getMaker().getName();

        if (existingGroup.getState() != State.RECRUITING) {
            throw NotFoundException.builder()
                    .httpStatus(HttpStatus.CONFLICT)
                    .message("모집중인 모임이 아닙니다. 모임의 상태를 확인 후 다시 시도해주세요.")
                    .build();
        }

        groupRepository.updateGroupState(State.RECRUITED, gid);
        List<User> joinerList = userGroupRepository.findAllByGroup_Id(gid).stream()
                .map(UserGroup::getJoiner).collect(Collectors.toList());

        joinerList.forEach(joiner -> {
            notificationRepository
                    .save(
                            Notification
                                    .builder()
                                    .sender(existingGroup.getMaker())
                                    .receiver(joiner)
                                    .content(existingGroup.getName() + " 모임의 모집이 마감되었어요. 모임원들과 연락해보세요!")
                                    .reason("모임 생성자: " + groupMakerName + "님")
                                    .build()
                    );
            joiner.notifyUser();
            userRepository.save(joiner);
        });
    }


    @Transactional
    public GroupCompleteRespDto completeGroup(UserAccount userAccount, Long gid, MultipartFile image) throws Exception {
        LocalDateTime now = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Asia/Seoul")).toLocalDateTime();
        // 모임 완료하면 카드 앞면 사진 업데이트
        if(!groupRepository.existsByIdAndMaker_Id(gid, userAccount.getUserId())){
            throw NotFoundException.builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .message("Group 생성자가 아닙니다.")
                    .build();
        }
        Group group = groupRepository.findGroupByGid(gid, userAccount.getDepartmentId());
        String groupMakerName = group.getMaker().getName();

        String imageLink = amazonS3Uploader.saveFileAndGetUrl(image);
        if (Optional.ofNullable(group.getMeetingAt()).isEmpty()) {
            group.updateNullMeetingAt(now);
        }

        // 앨범마다 group 유저 정보 체크해서 없으면 카드 추가, 있으면 badged -> true 로 변경
        List<User> groupJoinUsers = userGroupRepository.findGroupJoiner(gid);

        List<GroupCompleteRespDto.Card> cardList = new ArrayList<>();

        groupJoinUsers.forEach(albumOwner -> {
            List<Long> metUsersIdList = albumRepository.findAllByOwner_Id(albumOwner.getId()).stream()
                    .map(Album::getMetUser).map(User::getId).collect(Collectors.toList());

            Long albumOwnerPoint = 0L;
            StringBuilder albumOwnerPointHistoryMessage = new StringBuilder();

            switch (userGroupRepository.countAllByJoiner_IdAndGroup_State(albumOwner.getId(), State.DONE)) {
                case 0:
                    albumOwnerPoint += 300L;
                    albumOwnerPointHistoryMessage.append("모임 점수: 300점\n");
                    break;
                case 1:
                    albumOwnerPoint += 400L;
                    albumOwnerPointHistoryMessage.append("모임 점수: 400점\n");
                    break;
                case 2:
                    albumOwnerPoint += 500L;
                    albumOwnerPointHistoryMessage.append("모임 점수: 500점\n");
                    break;
                default:
                    albumOwnerPoint += 0L;
                    albumOwnerPointHistoryMessage.append("모임 점수: 0점\n");
                    break;
            }
            albumOwnerPointHistoryMessage.append("카드 점수: ");
            log.info("Initial point: Add {} ({})", albumOwnerPoint, albumOwner.getName());


            for (User groupJoinUser : groupJoinUsers) {
                userGroupRepository.updateFrontCardImg(groupJoinUser.getImageLink(), gid, groupJoinUser.getId());

                if (!Objects.equals(albumOwner.getId(), groupJoinUser.getId())) {
                    if (!metUsersIdList.contains(groupJoinUser.getId())) {
                        log.info("albumOwner : {}, joiner : {}", albumOwner.getName(), groupJoinUser.getName());
                        log.info("New card created: Add 100 point to {} ({})", albumOwnerPoint, albumOwner.getName());
                        albumOwnerPointHistoryMessage.append(groupJoinUser.getName()).append(" 100점, ");
                        albumOwnerPoint += 100;

                        albumRepository.save(Album.builder()
                                .owner(albumOwner).updatedAt(now).metUser(groupJoinUser).isBadged(true)
                                .build());
                    } else {
                        log.info("Card already exist: Add 50 point to {} ({})", albumOwnerPoint, albumOwner.getName());
                        albumOwnerPointHistoryMessage.append(groupJoinUser.getName()).append(" 50점, ");
                        albumOwnerPoint += 50;
                        albumRepository.updateBadgeState(true, now, groupJoinUser.getId(), albumOwner.getId());
                    }
                }
            }
            List<String> nameList = groupJoinUsers.stream()
                    .filter(user -> !user.getId().equals(albumOwner.getId()))
                    .map(User::getName).collect(Collectors.toList());

            notificationRepository.save(
                    Notification
                            .builder()
                            .sender(group.getMaker())
                            .receiver(albumOwner)
                            .content(String.join(", ", nameList) + "님과의 카드를 만들었어요.")
                            .reason("모임 생성자: " + groupMakerName + "님")
                            .build()
            );
            albumOwner.notifyUser();
            userRepository.save(albumOwner);

            albumOwnerPointHistoryMessage.replace(albumOwnerPointHistoryMessage.length() - 2, albumOwnerPointHistoryMessage.length(), "\n");
            albumOwnerPointHistoryMessage.append("총 점수: ").append(albumOwnerPoint).append("점");

            pointHistoryRepository.save(
                    PointHistory
                            .builder()
                            .point(albumOwnerPoint)
                            .title(group.getName())
                            .message(albumOwnerPointHistoryMessage.toString())
                            .owner(albumOwner)
                            .build()
            );

            userRepository.addUserPoint(albumOwner.getId(), albumOwnerPoint);

        });

        groupRepository.updateCompleteGroup(imageLink, now, gid, State.DONE);

        for (User albumOwner : groupJoinUsers) {
            if (!albumOwner.getId().equals(userAccount.getUserId())) {
                List<UserGroup> onePersonCard = userGroupRepository.findOnePersonCard(userAccount.getUserId(), albumOwner.getId(), State.DONE);
                cardList.add(GroupCompleteRespDto.Card.builder()
                        .userId(albumOwner.getId())
                        .username(albumOwner.getName())
                        .meetingAt(group.getMeetingAt())
                        .meetingCnt(Long.valueOf(onePersonCard.size()))
                        .frontImage(albumOwner.getImageLink())
                        .backImage(imageLink)
                        .build());
            }
        }
        return GroupCompleteRespDto.builder()
                .cardList(cardList)
                .build();

    }

    public DataResponseDto<List<GroupInfoResponseDto>> searchGroupByKeyword(String keyword) {
        List<GroupInfoResponseDto> resultDtoList = groupRepository
                .searchGroupByKeyword(keyword)
                .stream().map(x -> GroupInfoResponseDto.from(x, getHashtagStringList(x)))
                .collect(Collectors.toList());

        return DataResponseDto.<List<GroupInfoResponseDto>>builder()
                .data(resultDtoList).build();
    }

    public DataResponseDto<List<GroupInfoResponseDto>> searchAndSortGroupByKeyword(String keyword, SortedBy sortedBy) {
        List<GroupInfoResponseDto> resultDtoList;
        if (sortedBy == SortedBy.RECENT) {
            resultDtoList = groupRepository
                    .searchGroupByKeyword(keyword)
                    .stream().map(x -> GroupInfoResponseDto.from(x, getHashtagStringList(x)))
                    .collect(Collectors.toList());
        } else if (sortedBy == SortedBy.SOON) {
            resultDtoList = groupRepository
                    .searchGroupByMeetingAtAndKeyword(keyword)
                    .stream().map(x -> GroupInfoResponseDto.from(x, getHashtagStringList(x)))
                    .collect(Collectors.toList());
        } else {
            throw InvalidParameterException
                    .builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("Invalid sortedBy parameter")
                    .build();
        }

        return DataResponseDto.<List<GroupInfoResponseDto>>builder()
                .data(resultDtoList).build();
    }


    @Transactional
    public DataResponseDto<CommentInfoResponseDto> writeComment(UserAccount userAccount, Long groupId, WritingCommentRequestDto writingCommentRequestDto) {
        User existingUser = validateUser(userAccount.getUserId());
        Group existingGroup = validateGroup(groupId);

        Comment comment = Comment.builder()
                .group(existingGroup)
                .user(existingUser)
                .content(writingCommentRequestDto.getContent())
                .build();

        commentRepository.save(comment);

        return DataResponseDto.<CommentInfoResponseDto>builder()
                .data(CommentInfoResponseDto.from(comment))
                .build();
    }

    @Transactional
    public DataResponseDto<CommentInfoResponseDto> updateComment(UserAccount userAccount, Long commentId, WritingCommentRequestDto writingCommentRequestDto) {
        User existingUser = validateUser(userAccount.getUserId());
        Comment existingComment = validateComment(commentId);

        if (!existingComment.getUser().getId().equals(existingUser.getId())) {
            throw InvalidParameterException
                    .builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("댓글 작성자가 아닙니다.")
                    .build();
        }

        existingComment.modifyContent(writingCommentRequestDto.getContent());

        commentRepository.save(existingComment);

        return DataResponseDto.<CommentInfoResponseDto>builder()
                .data(CommentInfoResponseDto.from(existingComment))
                .build();
    }

    @Transactional
    public DataResponseDto<CommentInfoResponseDto> deleteComment(UserAccount userAccount, Long commentId) {
        User existingUser = validateUser(userAccount.getUserId());
        Comment existingComment = validateComment(commentId);

        if (!existingComment.getUser().getId().equals(existingUser.getId())) {
            throw InvalidParameterException
                    .builder()
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .message("댓글 작성자가 아닙니다.")
                    .build();
        }

        commentRepository.delete(existingComment);

        return DataResponseDto.<CommentInfoResponseDto>builder()
                .data(CommentInfoResponseDto.from(existingComment))
                .build();
    }

    private CommentInfoResponseDto getRecentCommentDtoFromGroup(Group group) {
        Optional<Comment> recentComment = commentRepository.findTopByGroupOrderByIdDesc(group);
        if (recentComment.isEmpty()) {
            return null;
        }
        return CommentInfoResponseDto.from(recentComment.get());
    }

    public DataResponseDto<Page<CommentInfoResponseDto>> getCommentList(Long groupId, Long cursor, Pageable pageable) {
        Group existingGroup = validateGroup(groupId);

        Page<Comment> commentPage = commentRepository.findAllByGroupAndIdGreaterThanEqual(existingGroup, cursor, pageable);;

        return DataResponseDto.<Page<CommentInfoResponseDto>>builder()
                .data(commentPage.map(CommentInfoResponseDto::from))
                .build();
    }
}
