package com.hanamja.moa.api.service.user;

import com.hanamja.moa.api.dto.user.response.UserInfoResponseDto;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.user.UserRepository;
import com.hanamja.moa.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserInfoResponseDto getMyInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("해당 유저가 없습니다. id=" + userId));

        return UserInfoResponseDto.from(user);
    }

    public UserInfoResponseDto updateProfileImage(Long userId, String profileImageUrl) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("해당 유저가 없습니다. id=" + userId));

        user.updateProfileImage(profileImageUrl);

        return UserInfoResponseDto.from(user);
    }
}
