package com.hanamja.moa.api.entity.board_category;

import com.hanamja.moa.api.dto.board_category.CategoryByBoardAndDepartmentDto;

import java.util.List;

public interface BoardCategoryRepositoryCustom {
    List<CategoryByBoardAndDepartmentDto> findAllCategoryByBoardAndDepartmentId(Long departmentId);
}
