create table if not exists moa_dept
(
    dept_id bigint auto_increment
    primary key,
    name    varchar(255) not null
    );

create table if not exists moa_hashtag
(
    hashtag_id bigint auto_increment
    primary key,
    name       varchar(255) not null,
    touched_at datetime(6)  not null,
    constraint UK_rirq5c9iybr7xvlqoijusnuh9
    unique (name)
    );

create table if not exists moa_user
(
    user_id      bigint auto_increment
    primary key,
    gender       varchar(255)         null,
    image_link   varchar(255)         null,
    intro        varchar(255)         null,
    name         varchar(255)         not null,
    pwd          varchar(255)         not null,
    point        bigint               null,
    role         varchar(255)         null,
    student_id   varchar(255)         not null,
    dept_id      bigint               null,
    is_active    tinyint(1) default 0 not null,
    is_onboarded tinyint(1) default 0 not null,
    is_notified  bit                  not null,
    constraint UK_mh293myj5vuwqw7n25slsfll2
    unique (student_id),
    constraint FKgrr9yk1b18m9g85ytlqd19pno
    foreign key (dept_id) references moa_dept (dept_id)
    );

create table if not exists moa_album
(
    album_id    bigint auto_increment
    primary key,
    is_badged   bit         not null,
    met_user_id bigint      not null,
    owner_id    bigint      not null,
    updated_at  datetime(6) null,
    constraint FK3dkyxsarwbwk4j25j5kyhm2vm
    foreign key (met_user_id) references moa_user (user_id),
    constraint FKir0vqw22shu9ax28anq2s2cmj
    foreign key (owner_id) references moa_user (user_id)
    );

create table if not exists moa_group
(
    group_id    bigint auto_increment
    primary key,
    created_at  datetime(6)  not null,
    current_num bigint       not null,
    description varchar(255) null,
    image_link  varchar(255) null,
    max_num     bigint       not null,
    meeting_at  datetime(6)  null,
    modified_at datetime(6)  not null,
    name        varchar(255) not null,
    state       varchar(20)  null,
    user_id     bigint       not null,
    dept_id     bigint       not null,
    constraint FK958s1tumjyjirnbri7nsn8dgs
    foreign key (user_id) references moa_user (user_id)
    );

create table if not exists moa_comment
(
    comment_id  bigint auto_increment
    primary key,
    content     varchar(255) not null,
    created_at  datetime(6)  not null,
    is_modified bit          not null,
    modified_at datetime(6)  not null,
    group_id    bigint       not null,
    user_id     bigint       not null,
    constraint FKid9ab8bmix6cs8296j3uj8r6
    foreign key (user_id) references moa_user (user_id),
    constraint FKqa551ft3e3lbnplpq58c563ll
    foreign key (group_id) references moa_group (group_id)
    );

create table if not exists moa_group_hashtag
(
    gh_id      bigint auto_increment
    primary key,
    group_id   bigint not null,
    hashtag_id bigint not null,
    constraint FK4sb3xy14hceaghferlxec6ge5
    foreign key (hashtag_id) references moa_hashtag (hashtag_id),
    constraint FK9h2ypki974ttfsp1k03fa2l59
    foreign key (group_id) references moa_group (group_id)
    );

create table if not exists moa_notification
(
    notification_id bigint auto_increment
    primary key,
    content         varchar(255) null,
    created_at      datetime(6)  not null,
    is_badged       bit          not null,
    reason          varchar(255) null,
    receiver_id     bigint       not null,
    sender_id       bigint       not null,
    constraint FK8eij7d7eqkpormjpsf6mpca6c
    foreign key (sender_id) references moa_user (user_id),
    constraint FKpr92hbjv024s4r76jbi37y9bt
    foreign key (receiver_id) references moa_user (user_id)
    );

create table if not exists moa_point_history
(
    p_history_id bigint auto_increment
    primary key,
    created_at   datetime(6)  not null,
    msg          varchar(255) null,
    point        bigint       not null,
    title        varchar(255) null,
    owner_id     bigint       not null,
    constraint FK61udthypar1uiluknjda9yi0r
    foreign key (owner_id) references moa_user (user_id)
    );

create table if not exists moa_user_department
(
    user_department_id bigint auto_increment
    primary key,
    dept_id            bigint not null,
    user_id            bigint not null,
    constraint FKfhayh0u9pyoecvfswptwy9c4x
    foreign key (dept_id) references moa_dept (dept_id),
    constraint FKft7a6757prlhf0kmeejx0h2pe
    foreign key (user_id) references moa_user (user_id)
    );

create table if not exists moa_user_group
(
    ug_id       bigint auto_increment
    primary key,
    join_at     datetime(6)  not null,
    progress    varchar(255) not null,
    group_id    bigint       not null,
    user_id     bigint       not null,
    meeting_img varchar(255) null,
    constraint idx__group_id__user_id
    unique (group_id, user_id),
    constraint idx__user_id__group_id
    unique (user_id, group_id),
    constraint FKein90lybfrt1kiscgjg2emoeo
    foreign key (group_id) references moa_group (group_id),
    constraint FKsviuitpav8tyycq4ubtdc4i3o
    foreign key (user_id) references moa_user (user_id)
    );

create table if not exists moa_user_token
(
    user_token_id bigint auto_increment
    primary key,
    refresh_token varchar(255) not null,
    user_id       bigint       not null,
    constraint FKsp8hcon1ag9yl33l0wbi4jcom
    foreign key (user_id) references moa_user (user_id)
    );

