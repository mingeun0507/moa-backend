package com.hanamja.moa.api.service.history;

import com.hanamja.moa.api.dto.history.response.HistoryInfoResponseDto;
import com.hanamja.moa.api.entity.history.HistoryRepository;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class HistoryService {
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;

    public List<HistoryInfoResponseDto> getHistoryList() {
        User user = userRepository.findById(1L).orElseThrow();

        return historyRepository
                .findAllByOwner_Id(user.getId())
                .stream()
                .map(HistoryInfoResponseDto::from)
                .collect(Collectors.toList());
    }
}
