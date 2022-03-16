create table if not exists tweets (
    id          bigint       not null primary key,
    comment_no  bigint       null,
    created_at  datetime(6)  null,
    description varchar(280) not null,
    updated_at  datetime(6)  null,
    user_id     bigint       null,
    constraint FK5lidm6cqbc7u4xhqpxm898qme foreign key (user_id) references users (id)
);

