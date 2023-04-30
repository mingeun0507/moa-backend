package com.hanamja.moa.api.controller.board;

import com.hanamja.moa.api.dto.board.response.DepartmentBoardInfoResponseDto;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<DepartmentBoardInfoResponseDto> getBoardInfoByDepartment(@AuthenticationPrincipal UserAccount userAccount) {
        return ResponseEntity.ok(boardService.getBoardInfoByDepartment(userAccount));
    }

}
