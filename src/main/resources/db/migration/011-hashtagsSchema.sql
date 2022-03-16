create table if not exists hashtags(
        id         bigint       not null primary key,
        created_at datetime(6)  null,
        value      varchar(255) null
);
