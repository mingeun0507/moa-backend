package com.hanamja.moa.api.controller.board;

import com.hanamja.moa.api.dto.board.request.NewBoardCategoryRequestDto;
import com.hanamja.moa.api.dto.board.response.DepartmentBoardInfoResponseDto;
import com.hanamja.moa.api.dto.post.response.PostInfoResponseDto;
import com.hanamja.moa.api.dto.util.DataResponseDto;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.service.board.BoardService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
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

    @GetMapping("/{boardId}")
    public ResponseEntity<DataResponseDto<Slice<PostInfoResponseDto>>> getPostListByBoardId(
            @AuthenticationPrincipal UserAccount userAccount,
            @PathVariable Long boardId,
            @Nullable @RequestParam Long cursor,
            @PageableDefault(size = 20, sort = "post_id", direction = Sort.Direction.DESC) Pageable pageable
    ) {

        return ResponseEntity.ok(boardService.getPostListByBoardId(userAccount, boardId, cursor, pageable));
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
