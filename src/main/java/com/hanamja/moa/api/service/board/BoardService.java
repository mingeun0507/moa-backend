package com.hanamja.moa.api.service.board;

import com.hanamja.moa.api.dto.board.request.NewBoardCategoryRequestDto;
import com.hanamja.moa.api.dto.board.response.DepartmentBoardInfoResponseDto;
import com.hanamja.moa.api.dto.post.response.PostInfoResponseDto;
import com.hanamja.moa.api.dto.util.DataResponseDto;
import com.hanamja.moa.api.entity.board.Board;
import com.hanamja.moa.api.entity.department.Department;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BoardService {

    /* validate 함수 */
    void validateDepartmentByUserAccount(Long departmentId, UserAccount userAccount);

    /* resolve 함수 - 객체의 id를 validate한 뒤 resolve 해오는 내부 함수들 */
    Department resolveDepartmentByUserAccount(UserAccount userAccount);

    Board resolveBoardById(Long boardId);

    /* Controller에서 사용할 함수들 */
    DepartmentBoardInfoResponseDto getBoardInfoByDepartment(UserAccount userAccount);

    DataResponseDto<Slice<PostInfoResponseDto>> getPostListByBoardId(UserAccount userAccount, Long boardId, Long cursor, Pageable pageable);

    void makeNewBoardCategory(UserAccount userAccount, NewBoardCategoryRequestDto newBoardCategoryRequestDto);
}
