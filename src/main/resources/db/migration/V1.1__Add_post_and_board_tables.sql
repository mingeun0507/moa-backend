create table if not exists moa_board
(
       board_id bigint auto_increment
       primary key,
       dept_id BIGINT NOT NULL,
       name VARCHAR(255) NOT NULL,
       CONSTRAINT FKbp7j4np4hmn4qon3v8c80y7ks FOREIGN KEY (dept_id) REFERENCES moa_dept (dept_id)
);

create table if not exists moa_board_category
(
    bc_id bigint auto_increment
    primary key,
    board_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT FK6elkv9iclv9i8edk5wlxv5c5w FOREIGN KEY (board_id) REFERENCES moa_board (board_id)
);

create table if not exists moa_post
(
    post_id bigint auto_increment
    primary key,
    user_id BIGINT NOT NULL,
    dept_id BIGINT NOT NULL,
    bc_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content VARCHAR(255) NOT NULL,
    is_modified BOOLEAN NOT NULL,
    thumbnail VARCHAR(255),
    created_at  datetime(6) null,
    modified_at datetime(6) null,
    FOREIGN KEY (user_id) REFERENCES moa_user (user_id),
    FOREIGN KEY (dept_id) REFERENCES moa_dept (dept_id),
    FOREIGN KEY (bc_id) REFERENCES moa_board_category (bc_id)
);

create table if not exists moa_board_category_req
(
        bc_req_id bigint auto_increment
        primary key,
        user_id BIGINT NOT NULL,
        board_id BIGINT NOT NULL,
        name VARCHAR(255) NOT NULL,
        CONSTRAINT FKcwywiab89bf7n0dhrn79mwhui FOREIGN KEY (board_id) REFERENCES moa_board (board_id),
        CONSTRAINT FKq3eyuq7q3ogtwodwci7a0f0h5 FOREIGN KEY (user_id) REFERENCES moa_user (user_id)
);

create table if not exists moa_post_bookmark
(
           pb_id bigint auto_increment
           primary key,
           post_id BIGINT NOT NULL,
           user_id BIGINT NOT NULL,
           created_at  datetime(6) null,
           modified_at datetime(6) null,
           CONSTRAINT FKlmdbt46v6j3nrg3g3q1yfsxwh FOREIGN KEY (post_id) REFERENCES moa_post (post_id),
           CONSTRAINT FKoxxntt7pb4tj13ygdxxk1t2ga FOREIGN KEY (user_id) REFERENCES moa_user (user_id)
);

create table if not exists moa_post_comment
(
          pc_id bigint auto_increment
          primary key,
          post_id BIGINT NOT NULL,
          user_id BIGINT NOT NULL,
          content VARCHAR(255) NOT NULL,
          is_modified BIT NOT NULL,
          is_deleted BIT,
          parent_cm_id BIGINT,
          is_reply BIT,
          comment_order INT,
          created_at  datetime(6) null,
          modified_at datetime(6) null,
          CONSTRAINT FK4ol4c1ivnxkj4f4u8yhkvybo7 FOREIGN KEY (parent_cm_id) REFERENCES moa_post_comment (pc_id),
          CONSTRAINT FKjgi06uc81rhvhkl0ap3a3o94w FOREIGN KEY (post_id) REFERENCES moa_post (post_id),
          CONSTRAINT FKt3jme8b8oc40h1bfbrl2wvivv FOREIGN KEY (user_id) REFERENCES moa_user (user_id)
);

create table if not exists moa_image
(
           pi_id bigint auto_increment
           primary key,
           post_id BIGINT NOT NULL,
           img_link VARCHAR(255) NOT NULL,
           CONSTRAINT FKlqdvvuamulc8v6tpy4ds99f4k FOREIGN KEY (post_id) REFERENCES moa_post (post_id)
);

create table if not exists moa_post_like
(
           pl_id bigint auto_increment
           primary key,
           post_id BIGINT NOT NULL,
           user_id BIGINT NOT NULL,
           created_at  datetime(6) null,
           modified_at datetime(6) null,
           CONSTRAINT FK7ktrvny8knc7xl9dmjpm34pf7 FOREIGN KEY (post_id) REFERENCES moa_post (post_id),
           CONSTRAINT FKk5ayfw2d9sm56ghu5tjtllp4x FOREIGN KEY (user_id) REFERENCES moa_user (user_id)
)



