package com.hanamja.moa.api.entity.board_category_req;

import com.hanamja.moa.api.entity.board.Board;
import com.hanamja.moa.api.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "MOA_BOARD_CATEGORY_REQ")
public class BoardCategoryRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bc_req_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(name = "name", nullable = false)
    private String name;

    public BoardCategoryRequest(User user, Board board, String name) {
        this.user = user;
        this.board = board;
        this.name = name;
    }
}
