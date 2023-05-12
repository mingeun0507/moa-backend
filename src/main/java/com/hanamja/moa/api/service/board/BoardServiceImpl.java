package com.hanamja.moa.api.service.board;

import com.hanamja.moa.api.dto.board.request.NewBoardCategoryRequestDto;
import com.hanamja.moa.api.dto.board.response.DepartmentBoardInfoResponseDto;
import com.hanamja.moa.api.dto.board_category.CategoryByBoardAndDepartmentDto;
import com.hanamja.moa.api.entity.board.Board;
import com.hanamja.moa.api.entity.board.BoardRepository;
import com.hanamja.moa.api.entity.board_category.BoardCategoryRepository;
import com.hanamja.moa.api.entity.board_category.BoardCategoryRepositoryCustom;
import com.hanamja.moa.api.entity.board_category_req.BoardCategoryRequest;
import com.hanamja.moa.api.entity.board_category_req.BoardCategoryRequestRepository;
import com.hanamja.moa.api.entity.department.Department;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.service.util.UtilServiceImpl;
import com.hanamja.moa.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardCategoryRepository boardCategoryRepository;
    private final BoardCategoryRequestRepository boardCategoryRequestRepository;
    private final BoardCategoryRepositoryCustom boardCategoryRepositoryCustom;
    private final UtilServiceImpl utilService;

    @Override
    public Board resolveBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(
                        () -> CustomException
                                .builder()
                                .httpStatus(HttpStatus.NOT_FOUND)
                                .message("해당 게시판이 존재하지 않습니다.")
                                .build());
    }

    @Override
    public DepartmentBoardInfoResponseDto getBoardInfoByDepartment(UserAccount userAccount) {
        Department resolvedDepartment = utilService.resolveDepartmentById(userAccount);

        List<CategoryByBoardAndDepartmentDto> resolvedCategoryList = boardCategoryRepositoryCustom.findAllCategoryByBoardAndDepartmentId(resolvedDepartment.getId());

        log.info("resolvedCategoryList: {}", resolvedCategoryList);

        return DepartmentBoardInfoResponseDto.from(resolvedCategoryList);
    }

    @Override
    @Transactional
    public void makeNewBoardCategory(UserAccount userAccount, NewBoardCategoryRequestDto newBoardCategoryRequestDto) {
        boardCategoryRequestRepository.save(
                BoardCategoryRequest.builder()
                        .user(utilService.resolveUserById(userAccount))
                        .board(resolveBoardById(newBoardCategoryRequestDto.getBoardId()))
                        .name(newBoardCategoryRequestDto.getCategory())
                        .build()
        );
    }
}
