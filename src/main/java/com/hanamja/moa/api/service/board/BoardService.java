package com.hanamja.moa.api.service.board;

import com.hanamja.moa.api.dto.board.request.NewBoardCategoryRequestDto;
import com.hanamja.moa.api.dto.board.response.DepartmentBoardInfoResponseDto;
import com.hanamja.moa.api.dto.post.response.PostInfoResponseDto;
import com.hanamja.moa.api.dto.util.DataResponseDto;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BoardService {

    /* validate 함수 */
    void validateDepartmentByUserAccount(Long departmentId, UserAccount userAccount);

    /* Controller에서 사용할 함수들 */
    DepartmentBoardInfoResponseDto getBoardInfoByDepartment(UserAccount userAccount);

    DataResponseDto<Slice<PostInfoResponseDto>> getPostListByBoardId(UserAccount userAccount, Long boardId, Long cursor, Pageable pageable, Long categoryId);

    DataResponseDto<Slice<PostInfoResponseDto>> searchPostListByBoardId(UserAccount userAccount, Long boardId, String keyword, Long cursor, Pageable pageable, Long categoryId);

    void makeNewBoardCategory(UserAccount userAccount, NewBoardCategoryRequestDto newBoardCategoryRequestDto);

}
