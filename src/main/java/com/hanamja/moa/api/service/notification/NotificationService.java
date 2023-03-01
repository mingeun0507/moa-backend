package com.hanamja.moa.api.service.notification;

import com.hanamja.moa.api.dto.notification.response.NotificationResponseDto;
import com.hanamja.moa.api.dto.util.DataResponseDto;
import com.hanamja.moa.api.entity.notification.NotificationRepository;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.entity.user.UserRepository;
import com.hanamja.moa.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;

    @Transactional
    public DataResponseDto<List<NotificationResponseDto>> getNotifications(UserAccount userAccount) {
        User existingUser = validateUser(userAccount);

        List<NotificationResponseDto> notificationList = notificationRepository.findAllByReceiver_Id(existingUser.getId())
                .stream()
                .map(NotificationResponseDto::from)
                .collect(Collectors.toList());

        existingUser.unNotifyUser();
        notificationRepository.updateNotificationState(existingUser.getId());
        userRepository.save(existingUser);

        return DataResponseDto.<List<NotificationResponseDto>>builder()
                .data(notificationList)
                .build();
    }

    private User validateUser(UserAccount userAccount) {
        return userRepository.findById(userAccount.getUserId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다."));
    }

    public String checkNotifications(UserAccount userAccount) {
        User existingUser = validateUser(userAccount);

        return existingUser.getIsNotified() ? "true" : "false";
    }
}
