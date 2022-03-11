create table if not exists messages (
        id              bigint       not null primary key,
        content         varchar(255) null,
        created_at      datetime(6)  null,
        conversation_id bigint       null,
        recipient_id    bigint       null,
        sender_id       bigint       null,
        constraint FK4ui4nnwntodh6wjvck53dbk9m foreign key (sender_id) references users (id),
        constraint FKhdkwfnspwb3s60j27vpg0rpg6 foreign key (recipient_id) references users (id),
        constraint FKt492th6wsovh1nush5yl5jj8e foreign key (conversation_id) references conversations (id)
);

