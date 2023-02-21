package com.hanamja.moa.config;

import com.hanamja.moa.api.entity.album.Album;
import com.hanamja.moa.api.entity.album.AlbumRepository;
import com.hanamja.moa.api.entity.department.Department;
import com.hanamja.moa.api.entity.department.DepartmentRepository;
import com.hanamja.moa.api.entity.group.Group;
import com.hanamja.moa.api.entity.group.GroupRepository;
import com.hanamja.moa.api.entity.group_hashtag.GroupHashtag;
import com.hanamja.moa.api.entity.group_hashtag.GroupHashtagRepository;
import com.hanamja.moa.api.entity.hashtag.Hashtag;
import com.hanamja.moa.api.entity.hashtag.HashtagRepository;
import com.hanamja.moa.api.entity.history.History;
import com.hanamja.moa.api.entity.history.HistoryRepository;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.user.UserRepository;
import com.hanamja.moa.api.entity.user_group.UserGroup;
import com.hanamja.moa.api.entity.user_group.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Profile("local")
@Component
public class InitDatabaseForLocal {

    private final InitDatabaseForLocalService initDatabaseForLocalService;

    @PostConstruct
    private void init() {
        this.initDatabaseForLocalService.init();
    }

    @RequiredArgsConstructor
    @Component
    private static class InitDatabaseForLocalService {

        private final UserRepository userRepository;

        private final GroupRepository groupRepository;

        private final DepartmentRepository departmentRepository;

        private final AlbumRepository albumRepository;

        private final UserGroupRepository userGroupRepository;

        private final GroupHashtagRepository groupHashtagRepository;

        private final HashtagRepository hashtagRepository;

        private final HistoryRepository historyRepository;

        @Transactional
        public void init() {

            Department software = Department
                    .builder()
                    .name("소프트웨어학부")
                    .build();

            departmentRepository.save(software);

            User mingeun = User
                    .builder()
                    .department(software)
                    .studentId("20180268")
                    .password("12345678")
                    .name("김민근")
                    .gender("남자")
                    .imageLink("http://example.com")
                    .point(0L)
                    .build();

            User changjin = User
                    .builder()
                    .department(software)
                    .studentId("20112011")
                    .password("12345678")
                    .name("서창진")
                    .gender("남자")
                    .imageLink("http://example2.com")
                    .point(0L)
                    .build();

            User seokmin = User
                    .builder()
                    .department(software)
                    .studentId("20232023")
                    .password("12345678")
                    .name("윤석민")
                    .gender("여자")
                    .imageLink("http://example3.com")
                    .point(1L)
                    .build();

            userRepository.save(mingeun);
            userRepository.save(changjin);
            userRepository.save(seokmin);

            Group CC = Group
                    .builder()
                    .maker(mingeun)
                    .name("CC 강의")
                    .description("CC 강의해드립니다")
                    .maxPeopleNum(6L)
                    .currentPeopleNum(1L)
                    .meetingAt(LocalDateTime.now())
                    .imageLink("http://example.com")
                    .build();

            Group LOL = Group
                    .builder()
                    .maker(seokmin)
                    .name("협곡 데이트")
                    .description("원딜 강의해드립니다")
                    .maxPeopleNum(8L)
                    .currentPeopleNum(2L)
                    .meetingAt(LocalDateTime.now())
                    .imageLink("http://example2.com")
                    .build();

            groupRepository.save(CC);

            UserGroup seokminToCC = UserGroup
                    .builder()
                    .joiner(seokmin)
                    .group(CC)
                    .progress("대기")
                    .build();

            if (!CC.isFull()) CC.addCurrentPeopleNum();
            // else throw CustomException

            UserGroup changjinToCC = UserGroup
                    .builder()
                    .joiner(changjin)
                    .group(CC)
                    .progress("대기2")
                    .build();

            if (!CC.isFull()) CC.addCurrentPeopleNum();
            // else throw CustomException

            UserGroup mingeunToLOL = UserGroup
                    .builder()
                    .joiner(mingeun)
                    .group(LOL)
                    .progress("대기2")
                    .build();

            if (!LOL.isFull()) LOL.addCurrentPeopleNum();
            // else throw CustomException

            groupRepository.save(LOL);
            userGroupRepository.save(seokminToCC);
            userGroupRepository.save(changjinToCC);
            userGroupRepository.save(mingeunToLOL);

            Album mingeunSeokminAlbum = Album
                    .builder()
                    .owner(mingeun)
                    .metUser(seokmin)
                    .isBadged(true)
                    .build();

            Album mingeunChangjinAlbum = Album
                    .builder()
                    .owner(mingeun)
                    .metUser(changjin)
                    .isBadged(false)
                    .build();

            albumRepository.save(mingeunSeokminAlbum);
            albumRepository.save(mingeunChangjinAlbum);

            Hashtag love = Hashtag
                    .builder()
                    .name("연애")
                    .build();

            Hashtag alcohol = Hashtag
                    .builder()
                    .name("술")
                    .build();

            Hashtag game = Hashtag
                    .builder()
                    .name("게임")
                    .build();

            hashtagRepository.save(love);
            hashtagRepository.save(alcohol);
            hashtagRepository.save(game);

            GroupHashtag CCHashtagLove = GroupHashtag
                    .builder()
                    .hashtag(love)
                    .group(CC)
                    .build();

            GroupHashtag CCHashtagAlcohol = GroupHashtag
                    .builder()
                    .hashtag(alcohol)
                    .group(CC)
                    .build();

            GroupHashtag LOLHashtagGame = GroupHashtag
                    .builder()
                    .hashtag(game)
                    .group(LOL)
                    .build();

            groupHashtagRepository.save(CCHashtagLove);
            groupHashtagRepository.save(CCHashtagAlcohol);
            groupHashtagRepository.save(LOLHashtagGame);

            History mingeunHistory1 = History
                    .builder()
                    .title("CC 강의")
                    .message("윤석민 50점, 윤석민 100점\n모임 참여점수 150점")
                    .point(300L)
                    .owner(mingeun)
                    .build();

            History mingeunHistory2 = History
                    .builder()
                    .title("협곡 데이트")
                    .message("윤석민 50점, 윤석민 100점\n모임 참여점수 150점")
                    .point(500L)
                    .owner(mingeun)
                    .build();

            History seokminHistory1 = History
                    .builder()
                    .title("협곡 데이트")
                    .message("윤석민 50점, 윤석민 100점\n모임 참여점수 150점")
                    .point(400L)
                    .owner(seokmin)
                    .build();

            historyRepository.save(mingeunHistory1);
            historyRepository.save(mingeunHistory2);
            historyRepository.save(seokminHistory1);
        }

    }


}
