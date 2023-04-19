package com.hanamja.moa.api.dto.board.response;

import com.hanamja.moa.api.dto.board_category.CategoryByBoardAndDepartmentDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DepartmentBoardInfoResponseDto {

    Long departmentId;
    String departmentName;
    List<BoardInfo> boardInfoList;

    @Builder
    public DepartmentBoardInfoResponseDto(Long departmentId, String departmentName, List<BoardInfo> boardInfoList) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.boardInfoList = boardInfoList;
    }

    public static DepartmentBoardInfoResponseDto from(List<CategoryByBoardAndDepartmentDto> categoryList) {

        if (categoryList == null || categoryList.isEmpty()) {
            throw new IllegalArgumentException("categoryList must not be null or empty");
        }

        // 첫 번째 CategoryByBoardAndDepartmentDto 객체를 가져옵니다.
        CategoryByBoardAndDepartmentDto firstDto = categoryList.get(0);
        // 반환할 DepartmentBoardInfoResponseDto에 포함될 BoardInfo 목록을 초기화합니다.
        List<BoardInfo> boardInfoList = new ArrayList<>();

        // categoryList를 boardId를 기준으로 그룹화하고, 각 그룹에 대해 다음 작업을 수행합니다.
        categoryList.stream()
                .collect(Collectors.groupingBy(CategoryByBoardAndDepartmentDto::getBoardId))
                .forEach((boardId, boardCategories) -> {
                    // 각 그룹의 첫 번째 CategoryByBoardAndDepartmentDto 객체를 가져옵니다.
                    CategoryByBoardAndDepartmentDto firstBoardCategory = boardCategories.get(0);
                    // 그룹의 CategoryByBoardAndDepartmentDto 객체들을 CategoryInfo 목록으로 변환합니다.
                    List<CategoryInfo> categoryInfoList = boardCategories.stream()
                            .map(category -> CategoryInfo.builder()
                                    .categoryId(category.getCategoryId())
                                    .categoryName(category.getCategoryName())
                                    .build())
                            .collect(Collectors.toList());

                    // 변환된 CategoryInfo 목록과 함께 BoardInfo 객체를 생성하고, boardInfoList에 추가합니다.
                    boardInfoList.add(BoardInfo.builder()
                            .boardId(boardId)
                            .boardName(firstBoardCategory.getBoardName())
                            .categoryInfoList(categoryInfoList)
                            .build());
                });

        // 생성된 boardInfoList와 함께 DepartmentBoardInfoResponseDto 객체를 반환합니다.
        return DepartmentBoardInfoResponseDto.builder()
                .departmentId(firstDto.getDepartmentId())
                .departmentName(firstDto.getDepartmentName())
                .boardInfoList(boardInfoList)
                .build();
    }

    @Getter
    @Builder
    static class BoardInfo {
        private Long boardId;
        private String boardName;
        private List<CategoryInfo> categoryInfoList;
    }

    @Getter
    @Builder
    static class CategoryInfo {
        private Long categoryId;
        private String categoryName;
    }
}
