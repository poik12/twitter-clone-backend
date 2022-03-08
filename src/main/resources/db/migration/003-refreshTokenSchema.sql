create table if not exists refresh_tokens (
    id bigint auto_increment primary key,
    created_at datetime(6)  null,
    expires_at datetime(6)  null,
    token      varchar(255) null,
    user_id    bigint       null,
    constraint FK1lih5y2npsf8u5o3vhdb9y0os foreign key (user_id) references users (id)
);



