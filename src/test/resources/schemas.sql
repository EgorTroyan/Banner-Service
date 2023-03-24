SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS banner;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS category_banner;
DROP TABLE IF EXISTS record;
DROP TABLE IF EXISTS record_category_ids;
SET FOREIGN_KEY_CHECKS = 1;

create table banner
(
    id        bigint auto_increment
        primary key,
    is_active bit          not null,
    name      varchar(255) null,
    price     float        not null,
    text      varchar(255) null,
    constraint UK2sdt8tslu2bp7ppmaim7mmcfk
        unique (name)
);

create table category
(
    id        bigint auto_increment
        primary key,
    is_active bit          not null,
    name      varchar(255) null,
    name_id   varchar(255) null,
    constraint UK_46ccwnsi9409t36lurvtyljak
        unique (name),
    constraint UK_ecgmaay6wpwhhdpx5p95capn7
        unique (name_id)
);

create table category_banner
(
    banner_id   bigint not null,
    category_id bigint not null,
    primary key (banner_id, category_id),
    constraint FK8a9as5t94hdo9d62mgk1hjued
        foreign key (category_id) references category (id),
    constraint FKcsbhp1h2lfc0pvsr7xwxwt048
        foreign key (banner_id) references banner (id)
);

create table record
(
    id                bigint auto_increment
        primary key,
    user_agent        varchar(255) not null,
    banner_id         bigint       null,
    date              datetime(6)  not null,
    ip_adress         varchar(255) not null,
    no_content_reason varchar(255) null,
    price             float        not null
);

create table record_category_ids
(
    record_id    bigint not null,
    category_ids bigint null,
    constraint FKedhp1s6w7fl6eflhf67uv4jva
        foreign key (record_id) references record (id)
);