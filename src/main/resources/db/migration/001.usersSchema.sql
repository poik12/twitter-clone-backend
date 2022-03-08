create table if not exists users (
    id                 bigint       auto_increment not null primary key,
    background_picture longblob     null,
    created_at         datetime(6)  not null,
    description        varchar(280) null,
    email_address      varchar(255) not null,
    enabled            bit          null,
    follower_no        bigint       null,
    following_no       bigint       null,
    name               varchar(255) not null,
    password           varchar(255) not null,
    phone_number       varchar(255) not null,
    profile_picture    longblob     null,
    tweet_no           bigint       null,
    updated_at         datetime(6)  not null,
    user_role          varchar(255) null,
    username           varchar(255) not null
);

