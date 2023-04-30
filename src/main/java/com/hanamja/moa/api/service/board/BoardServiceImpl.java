package com.hanamja.moa.api.service.board;

import com.hanamja.moa.api.dto.board.response.DepartmentBoardInfoResponseDto;
import com.hanamja.moa.api.dto.board_category.CategoryByBoardAndDepartmentDto;
import com.hanamja.moa.api.entity.board.BoardRepository;
import com.hanamja.moa.api.entity.board_category.BoardCategoryRepository;
import com.hanamja.moa.api.entity.board_category.BoardCategoryRepositoryCustom;
import com.hanamja.moa.api.entity.department.Department;
import com.hanamja.moa.api.entity.department.DepartmentRepository;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.exception.custom.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardCategoryRepository boardCategoryRepository;
    private final BoardCategoryRepositoryCustom boardCategoryRepositoryCustom;
    private final DepartmentRepository departmentRepository;

    @Override
    public Department resolveDepartmentById(UserAccount userAccount) {
        return departmentRepository
                .findById(userAccount.getDepartmentId())
                .orElseThrow(
                        () -> NotFoundException
                                .builder()
                                .httpStatus(HttpStatus.NOT_FOUND)
                                .message("해당 부서가 존재하지 않습니다.")
                                .build()
                );
    }

    @Override
    public DepartmentBoardInfoResponseDto getBoardInfoByDepartment(UserAccount userAccount) {
        Department resolvedDepartment = resolveDepartmentById(userAccount);

        List<CategoryByBoardAndDepartmentDto> resolvedCategoryList = boardCategoryRepositoryCustom.findAllCategoryByBoardAndDepartmentId(resolvedDepartment.getId());

        log.info("resolvedCategoryList: {}", resolvedCategoryList);

        return DepartmentBoardInfoResponseDto.from(resolvedCategoryList);
    }
}
