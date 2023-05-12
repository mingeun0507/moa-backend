package com.hanamja.moa.api.service.util;

import com.hanamja.moa.api.entity.department.Department;
import com.hanamja.moa.api.entity.department.DepartmentRepository;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.entity.user.UserRepository;
import com.hanamja.moa.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UtilServiceImpl implements UtilService{
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public User resolveUserById(UserAccount userAccount) {
        return userRepository
                .findById(userAccount.getUserId())
                .orElseThrow(
                        () -> NotFoundException
                                .builder()
                                .httpStatus(HttpStatus.NOT_FOUND)
                                .message("해당 유저가 존재하지 않습니다.")
                                .build()
                );
    }

    @Override
    public Department resolveDepartmentById(UserAccount userAccount) {
        return departmentRepository
                .findById(userAccount.getDepartmentId())
                .orElseThrow(
                        () -> NotFoundException
                                .builder()
                                .httpStatus(HttpStatus.NOT_FOUND)
                                .message("해당 학과가 존재하지 않습니다.")
                                .build()
                );
    }
}
