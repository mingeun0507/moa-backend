package com.hanamja.moa.api.controller.board;

import com.hanamja.moa.api.dto.board.request.NewBoardCategoryRequestDto;
import com.hanamja.moa.api.dto.board.response.DepartmentBoardInfoResponseDto;
import com.hanamja.moa.api.dto.post.response.PostInfoResponseDto;
import com.hanamja.moa.api.dto.util.DataResponseDto;
import com.hanamja.moa.api.entity.user.UserAccount.UserAccount;
import com.hanamja.moa.api.service.board.BoardService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
            @PageableDefault(size = 20, sort = "post_id", direction = Sort.Direction.DESC) Pageable pageable,
            @Nullable @RequestParam Long categoryId
    ) {

        return ResponseEntity.ok(boardService.getPostListByBoardId(userAccount, boardId, cursor, pageable, categoryId));
    }

    @GetMapping("/search/{boardId}")
    public ResponseEntity<DataResponseDto<Slice<PostInfoResponseDto>>> searchPostListByBoardId(
            @AuthenticationPrincipal UserAccount userAccount,
            @PathVariable Long boardId,
            @RequestParam String keyword,
            @Nullable @RequestParam Long cursor,
            @PageableDefault(size = 20, sort = "post_id", direction = Sort.Direction.DESC) Pageable pageable,
            @Nullable @RequestParam Long categoryId
    ) {

        return ResponseEntity.ok(boardService.searchPostListByBoardId(userAccount, boardId, keyword, cursor, pageable, categoryId));
    }

    @PostMapping(value = "/category")
    public ResponseEntity<?> makeNewBoardCategory
            (
                    @Parameter(hidden = true) @AuthenticationPrincipal UserAccount userAccount,
                    @RequestBody NewBoardCategoryRequestDto newBoardCategoryRequestDto
            ) {
        boardService.makeNewBoardCategory(userAccount, newBoardCategoryRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
