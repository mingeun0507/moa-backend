package com.hanamja.moa.api.service.group;

import com.hanamja.moa.api.dto.group.GroupInfoResponseDto;
import com.hanamja.moa.api.dto.group.GroupMakingRequestDto;
import com.hanamja.moa.api.entity.group.Group;
import com.hanamja.moa.api.entity.group.GroupRepository;
import com.hanamja.moa.api.entity.group_hashtag.GroupHashtag;
import com.hanamja.moa.api.entity.group_hashtag.GroupHashtagRepository;
import com.hanamja.moa.api.entity.hashtag.Hashtag;
import com.hanamja.moa.api.entity.hashtag.HashtagRepository;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class GroupService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupHashtagRepository groupHashtagRepository;
    private final HashtagRepository hashtagRepository;

    @Transactional
    public GroupInfoResponseDto makeNewGroup(GroupMakingRequestDto groupMakingRequestDto) {
        // TODO: 로그인 구현 후 @AuthenticationPrincipal User user 추가 필요

        User user = userRepository.findById(1L).orElseThrow(
                // TODO: Exception 구현 후 사용자를 찾지 못한 경우 unchecked Exception 던지기
        );

        if (user.isFreshman()) {
            log.info("신입생의 모임 생성 시도 발생 - 학번: {}, 이름: {}", user.getStudentId(), user.getName());
            // TODO: 예외 처리 구현 필요
            // 신입생이 만드는지?
        }


        // Hashtag #으로 파싱 후 분리, 저장 (다른 메소드로 분리 구현)
        List<Hashtag> hashtagList = saveHashtags(groupMakingRequestDto.getHashtags());

        Group newGroup = GroupMakingRequestDto.toEntity(groupMakingRequestDto, user);
        groupRepository.save(newGroup);

        // GroupHashtag 생성 및 Group과의 관계 설정, 저장
        hashtagList.stream().map(
                x -> GroupHashtag
                        .builder()
                        .group(newGroup)
                        .hashtag(x)
                        .build()
        ).forEach(groupHashtagRepository::save);

        return GroupInfoResponseDto.from(newGroup);
    }

    @Transactional
    public List<Hashtag> saveHashtags(String hashtagString) {
        List<String> hashtagStringList = new java.util.ArrayList<>(List.of(hashtagString.split("#")));
        hashtagStringList.remove(0);

        return hashtagStringList.stream().map(
                x -> {
                    if (hashtagRepository.existsByName(x)) {
                        // 이미 같은 이름의 해시태그가 존재하는 경우 변경 시간 업데이트
                        Hashtag existingHastag = hashtagRepository.findByName(x).orElseThrow();
                        existingHastag.updateTouchedAt();

                        // 찾아온 해시태그를 리턴 - TODO: orElseThrow에 500 Internal 에러 처리 추가
                        return hashtagRepository.findByName(x).orElseThrow();
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

}
