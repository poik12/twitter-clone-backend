create table if not exists user_post_likes(
    user_id bigint not null,
    post_id bigint not null,
    constraint FK16qt5xsftec1ev8e9xmlr5cat foreign key (user_id) references users (id),
    constraint FKqvflokix9srpr672owwce1q56 foreign key (post_id) references posts (id)
);
