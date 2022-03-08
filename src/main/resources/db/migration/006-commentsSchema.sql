create table if not exists comments (
    id         bigint       not null primary key,
    created_at datetime(6)  null,
    text       varchar(280) not null,
    post_id    bigint       null,
    user_id    bigint       null,
    constraint FK8omq0tc18jd43bu5tjh6jvraq foreign key (user_id) references users (id),
    constraint FKh4c7lvsc298whoyd4w9ta25cr foreign key (post_id) references posts (id)
);

