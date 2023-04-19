package com.hanamja.moa.config;

import com.hanamja.moa.api.entity.album.Album;
import com.hanamja.moa.api.entity.album.AlbumRepository;
import com.hanamja.moa.api.entity.board.Board;
import com.hanamja.moa.api.entity.board.BoardRepository;
import com.hanamja.moa.api.entity.board_category.BoardCategory;
import com.hanamja.moa.api.entity.board_category.BoardCategoryRepository;
import com.hanamja.moa.api.entity.board_category.BoardCategoryRepositoryCustom;
import com.hanamja.moa.api.entity.department.Department;
import com.hanamja.moa.api.entity.department.DepartmentRepository;
import com.hanamja.moa.api.entity.group.Group;
import com.hanamja.moa.api.entity.group.GroupRepository;
import com.hanamja.moa.api.entity.group_hashtag.GroupHashtag;
import com.hanamja.moa.api.entity.group_hashtag.GroupHashtagRepository;
import com.hanamja.moa.api.entity.hashtag.Hashtag;
import com.hanamja.moa.api.entity.hashtag.HashtagRepository;
import com.hanamja.moa.api.entity.point_history.PointHistory;
import com.hanamja.moa.api.entity.point_history.PointHistoryRepository;
import com.hanamja.moa.api.entity.user.Gender;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.user.UserRepository;
import com.hanamja.moa.api.entity.user_department.UserDepartment;
import com.hanamja.moa.api.entity.user_department.UserDepartmentRepository;
import com.hanamja.moa.api.entity.user_group.UserGroup;
import com.hanamja.moa.api.entity.user_group.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
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

        private final UserDepartmentRepository userDepartmentRepository;

        private final GroupRepository groupRepository;

        private final DepartmentRepository departmentRepository;

        private final AlbumRepository albumRepository;

        private final UserGroupRepository userGroupRepository;

        private final GroupHashtagRepository groupHashtagRepository;

        private final HashtagRepository hashtagRepository;

        private final PointHistoryRepository pointHistoryRepository;

        private final PasswordEncoder passwordEncoder;

        private final BoardCategoryRepository boardCategoryRepository;

        private final BoardCategoryRepositoryCustom boardCategoryRepositoryCustom;

        private final BoardRepository boardRepository;

        @Transactional
        public void initUser() {

            Department software = Department
                    .builder()
                    .name("소프트웨어학부")
                    .build();

            departmentRepository.save(software);

            User mingeun = User
                    .builder()
//                    .department(software)
                    .studentId("20180268")
                    .password(passwordEncoder.encode("12345678"))
                    .name("김민근")
                    .gender(Gender.MALE)
                    .imageLink("http://example.com")
                    .point(0L)
                    .build();

            User changjin = User
                    .builder()
//                    .department(software)
                    .studentId("20232011")
                    .password(passwordEncoder.encode("12345678"))
                    .name("서창진")
                    .gender(Gender.MALE)
                    .imageLink("http://example2.com")
                    .point(0L)
                    .build();

            User seokmin = User
                    .builder()
//                    .department(software)
                    .studentId("20232023")
                    .password(passwordEncoder.encode("12345678"))
                    .name("윤석민")
                    .gender(Gender.FEMALE)
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

            UserGroup mingeunToCC = UserGroup
                    .builder()
                    .joiner(mingeun)
                    .group(CC)
                    .progress("대기")
                    .build();

            if (!CC.isFull()) CC.addCurrentPeopleNum();

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
            userGroupRepository.save(mingeunToCC);
            userGroupRepository.save(seokminToCC);
            userGroupRepository.save(changjinToCC);
            userGroupRepository.save(mingeunToLOL);

            Album mingeunSeokminAlbum = Album
                    .builder()
                    .owner(mingeun)
                    .metUser(seokmin)
                    .isBadged(true)
                    .updatedAt(LocalDateTime.now())
                    .build();

            Album mingeunChangjinAlbum = Album
                    .builder()
                    .owner(mingeun)
                    .metUser(changjin)
                    .isBadged(false)
                    .updatedAt(LocalDateTime.now())
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

            PointHistory mingeunPointHistory1 = PointHistory
                    .builder()
                    .title("CC 강의")
                    .message("모임 점수: 400점\n" +
                            "카드 점수: 윤석민 100점, 서창진 100점\n" +
                            "총 점수: 600점")
                    .point(300L)
                    .owner(mingeun)
                    .build();

            PointHistory mingeunPointHistory2 = PointHistory
                    .builder()
                    .title("협곡 데이트")
                    .message("모임 점수: 400점\n" +
                            "카드 점수: 윤석민 100점, 서창진 100점\n" +
                            "총 점수: 600점")
                    .point(500L)
                    .owner(mingeun)
                    .build();

            PointHistory seokminPointHistory1 = PointHistory
                    .builder()
                    .title("협곡 데이트")
                    .message("모임 점수: 400점\n" +
                            "카드 점수: 윤석민 100점, 서창진 100점\n" +
                            "총 점수: 600점")
                    .point(400L)
                    .owner(seokmin)
                    .build();

            pointHistoryRepository.save(mingeunPointHistory1);
            pointHistoryRepository.save(mingeunPointHistory2);
            pointHistoryRepository.save(seokminPointHistory1);
        }

        public void init() {

            Department software = Department
                    .builder()
                    .name("소프트웨어학부")
                    .build();

            departmentRepository.save(software);

            User mingeun = User
                    .builder()
                    .studentId("20180268")
                    .password(passwordEncoder.encode("12345678"))
                    .name("김민근")
                    .gender(Gender.MALE)
                    .imageLink("http://example.com")
                    .point(0L)
                    .build();

            userRepository.save(mingeun);

            UserDepartment mingeunSoft = UserDepartment
                    .builder()
                    .user(mingeun)
                    .department(software)
                    .build();

            userDepartmentRepository.save(mingeunSoft);

            Board board = Board
                    .builder()
                    .department(software)
                    .name("자유게시판")
                    .build();

            boardRepository.save(board);

            Board board2 = Board
                    .builder()
                    .department(software)
                    .name("공지사항")
                    .build();

            boardRepository.save(board2);

            Board board3 = Board
                    .builder()
                    .department(software)
                    .name("후기게시판")
                    .build();

            boardRepository.save(board3);

            BoardCategory boardCategory = BoardCategory
                    .builder()
                    .name("동아리")
                    .board(board)
                    .build();

            boardCategoryRepository.save(boardCategory);

            BoardCategory boardCategory2 = BoardCategory
                    .builder()
                    .name("학회")
                    .board(board)
                    .build();

            boardCategoryRepository.save(boardCategory2);

            BoardCategory boardCategory3 = BoardCategory
                    .builder()
                    .name("몰루몰루")
                    .board(board)
                    .build();

            boardCategoryRepository.save(boardCategory3);

            BoardCategory boardCategory4 = BoardCategory
                    .builder()
                    .name("후기")
                    .board(board2)
                    .build();

            boardCategoryRepository.save(boardCategory4);

            BoardCategory boardCategory5 = BoardCategory
                    .builder()
                    .name("우하하")
                    .board(board2)
                    .build();

            boardCategoryRepository.save(boardCategory5);

//            BoardCategory boardCategory6 = BoardCategory
//                    .builder()
//                    .name("후기")
//                    .board(board3)
//                    .build();
//
//            boardCategoryRepository.save(boardCategory6);
        }
    }


}
