package com.hanamja.moa.api.dto.board_category;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CategoryByBoardAndDepartmentDto {
    private Long departmentId;
    private String departmentName;
    private Long boardId;
    private String boardName;
    private Long categoryId;
    private String categoryName;
}
