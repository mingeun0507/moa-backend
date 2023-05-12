package com.hanamja.moa.api.controller.board;

import com.hanamja.moa.api.dto.board.request.NewBoardCategoryRequestDto;
import com.hanamja.moa.api.dto.board.response.DepartmentBoardInfoResponseDto;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.service.board.BoardService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<DepartmentBoardInfoResponseDto> getBoardInfoByDepartment(@AuthenticationPrincipal UserAccount userAccount) {
        return ResponseEntity.ok(boardService.getBoardInfoByDepartment(userAccount));
    }

    @PostMapping(value = "/category")
    public ResponseEntity<?> makeNewBoardCategory
            (
                    @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
                    @RequestBody NewBoardCategoryRequestDto newBoardCategoryRequestDto
            )
    {
        boardService.makeNewBoardCategory(userAccount, newBoardCategoryRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
