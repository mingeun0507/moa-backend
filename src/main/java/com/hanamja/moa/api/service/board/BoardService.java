package com.hanamja.moa.api.service.board;

import com.hanamja.moa.api.dto.board.request.NewBoardCategoryRequestDto;
import com.hanamja.moa.api.dto.board.response.DepartmentBoardInfoResponseDto;
import com.hanamja.moa.api.entity.board.Board;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;

public interface BoardService {

    /* resolve 함수 - 객체의 id를 validate한 뒤 resolve 해오는 내부 함수들 */
    Board resolveBoardById(Long boardId);

    /* Controller에서 사용할 함수들 */
    DepartmentBoardInfoResponseDto getBoardInfoByDepartment(UserAccount userAccount);

    void makeNewBoardCategory(UserAccount userAccount, NewBoardCategoryRequestDto newBoardCategoryRequestDto);
}
