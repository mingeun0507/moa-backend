package com.hanamja.moa.api.service.util;

import com.hanamja.moa.api.entity.board.Board;
import com.hanamja.moa.api.entity.department.Department;
import com.hanamja.moa.api.entity.user.User;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;

public interface UtilService {
    /* resolve 함수 - 객체의 id를 validate한 뒤 resolve 해오는 내부 함수들 */
    User resolveUserById(UserAccount userAccount);

    Department resolveDepartmentById(UserAccount userAccount);

    Board resolveBoardById(Long boardId);

}
