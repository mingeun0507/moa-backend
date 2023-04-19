package com.hanamja.moa.api.entity.board_category;

import com.hanamja.moa.api.dto.board_category.CategoryByBoardAndDepartmentDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.hanamja.moa.api.entity.board.QBoard.board;
import static com.hanamja.moa.api.entity.board_category.QBoardCategory.boardCategory;
import static com.hanamja.moa.api.entity.department.QDepartment.department;

@RequiredArgsConstructor
@Repository
public class BoardCategoryRepositoryCustomImpl implements BoardCategoryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CategoryByBoardAndDepartmentDto> findAllCategoryByBoardAndDepartmentId(Long departmentId) {
        List<CategoryByBoardAndDepartmentDto> resolvedCategoryList = jpaQueryFactory
                .select(Projections.constructor(CategoryByBoardAndDepartmentDto.class,
                        department.id, department.name, board.id, board.name, boardCategory.id, boardCategory.name))
                .from(department)
                .join(board).on(department.id.eq(board.department.id))
                .join(boardCategory).on(board.id.eq(boardCategory.board.id))
                .where(department.id.eq(departmentId))
                .fetch();

        return resolvedCategoryList;
    }
}
