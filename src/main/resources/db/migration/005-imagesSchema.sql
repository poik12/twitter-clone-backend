create table if not exists images (
    id          bigint       not null primary key,
    content     longblob     null,
    name        varchar(255) null,
    size        bigint       null,
    upload_time datetime(6)  null,
    tweet_id     bigint       null,
    constraint FKcp0pycisii8ub3q4b7x5mfpn1 foreign key (tweet_id) references tweets (id)
);

