create table if not exists messages (
        id           bigint       not null primary key,
        content      varchar(255) null,
        from_user_fk bigint       null,
        to_user_fk   bigint       null,
        constraint FK651q3hnn6kdn6osav4phju8j7 foreign key (to_user_fk) references users (id),
        constraint FK91161gmoi2m34j0w4fdxc8ws3 foreign key (from_user_fk) references users (id)
);

