create table if not exists posts_hashtags
(
        post_id    bigint not null,
        hashtag_id bigint not null,
        constraint FKoer1wg3281f1913lt69c0t7uv foreign key (post_id) references tweets (id),
        constraint FKt4lg0lomjpxr5r0f33h3on0ob foreign key (hashtag_id) references hashtags (id)
);