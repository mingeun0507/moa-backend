package com.hanamja.moa.api.service.board;

import com.hanamja.moa.api.dto.board.request.NewBoardCategoryRequestDto;
import com.hanamja.moa.api.dto.board.response.DepartmentBoardInfoResponseDto;
import com.hanamja.moa.api.dto.board_category.CategoryByBoardAndDepartmentDto;
import com.hanamja.moa.api.dto.post.response.PostInfoResponseDto;
import com.hanamja.moa.api.dto.util.DataResponseDto;
import com.hanamja.moa.api.entity.board.Board;
import com.hanamja.moa.api.entity.board.BoardRepository;
import com.hanamja.moa.api.entity.board_category.BoardCategoryRepository;
import com.hanamja.moa.api.entity.board_category.BoardCategoryRepositoryCustom;
import com.hanamja.moa.api.entity.board_category_req.BoardCategoryRequest;
import com.hanamja.moa.api.entity.board_category_req.BoardCategoryRequestRepository;
import com.hanamja.moa.api.entity.department.Department;
import com.hanamja.moa.api.entity.department.DepartmentRepository;
import com.hanamja.moa.api.entity.post.PostRepository;
import com.hanamja.moa.api.entity.post.PostRepositoryCustom;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.service.util.UtilServiceImpl;
import com.hanamja.moa.exception.custom.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    private final DepartmentRepository departmentRepository;
    private final PostRepository postRepository;
    private final PostRepositoryCustom postRepositoryCustom;
    private final UtilServiceImpl utilService;

    @Override
    public void validateDepartmentByUserAccount(Long departmentId, UserAccount userAccount) {
        if (!userAccount.getDepartmentId().equals(departmentId)) {
            throw UnauthorizedException
                    .builder()
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .message("해당 학과에 접근할 수 없습니다.")
                    .build();
        }
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
                        .board(utilService.resolveBoardById(newBoardCategoryRequestDto.getBoardId()))
                        .name(newBoardCategoryRequestDto.getCategory())
                        .build()
        );
    }

    @Override
    public DataResponseDto<Slice<PostInfoResponseDto>> getPostListByBoardId(UserAccount userAccount, Long boardId, Long cursor, Pageable pageable) {
        validateDepartmentByUserAccount(userAccount.getDepartmentId(), userAccount);
        Board resolvedBoard = utilService.resolveBoardById(boardId);

        return DataResponseDto.<Slice<PostInfoResponseDto>>builder().data(postRepositoryCustom.findAllSimplePostInfo(resolvedBoard, cursor, pageable)).build();
    }
}
