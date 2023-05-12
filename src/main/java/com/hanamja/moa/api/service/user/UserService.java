package com.hanamja.moa.api.service.user;

import com.hanamja.moa.api.dto.user.request.ModifyingUserInfoRequestDto;
import com.hanamja.moa.api.dto.user.response.UserInfoResponseDto;
import com.hanamja.moa.api.entity.department.Department;
import com.hanamja.moa.api.entity.department.DepartmentRepository;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.entity.user.UserRepository;
import com.hanamja.moa.api.entity.user_department.UserDepartment;
import com.hanamja.moa.api.entity.user_department.UserDepartmentRepository;
import com.hanamja.moa.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    private final DepartmentRepository departmentRepository;
    private final UserDepartmentRepository userDepartmentRepository;

    public UserInfoResponseDto getUserInfo(UserAccount userAccount) {
        User user = userRepository.findById(userAccount.getUserId()).orElseThrow(() -> new NotFoundException(HttpStatus.BAD_REQUEST, "해당 유저가 없습니다."));
        int rank = userRepository.getUserRank(userAccount.getUserId(), user.getRole());
        List<Department> departments = userDepartmentRepository.findByUser_Id(user.getId()).stream().map(UserDepartment::getDepartment).collect(Collectors.toList());
        return UserInfoResponseDto.from(user, departments, rank);
    }

    public UserInfoResponseDto updateProfileImage(Long userId, String profileImageUrl) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(HttpStatus.BAD_REQUEST, "해당 유저가 없습니다. id=" + userId));

        user.updateProfileImage(profileImageUrl);

        return UserInfoResponseDto.from(user);
    }

    @Transactional
    public UserInfoResponseDto modifyUserInfo(UserAccount userAccount, ModifyingUserInfoRequestDto modifyingUserInfoRequestDto) {
        User user = userRepository.findById(userAccount.getUserId()).orElseThrow(() -> new NotFoundException(HttpStatus.BAD_REQUEST, "해당 유저가 없습니다. id=" + userAccount.getUserId()));

        user.modifyUserInfo(
                modifyingUserInfoRequestDto.getGender(),
//              modifyingUserInfo 메서드 인자 3개인데 왜 4개 들어있는지?
//                departmentRepository.findByName(modifyingUserInfoRequestDto.getDepartment()).orElseThrow(
//                    () -> new NotFoundException(HttpStatus.BAD_REQUEST, "해당 학과가 없습니다. name=" + modifyingUserInfoRequestDto.getDepartment())),
                modifyingUserInfoRequestDto.getIntro(),
                modifyingUserInfoRequestDto.getImageLink());
        user.updateProfileImage(modifyingUserInfoRequestDto.getImageLink());
        userRepository.save(user);

        return UserInfoResponseDto.from(user);
    }
}
