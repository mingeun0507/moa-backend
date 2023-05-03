package com.hanamja.moa.api.dto.board.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewBoardCategoryRequestDto {
    private Long boardId;
    private String category;
}
