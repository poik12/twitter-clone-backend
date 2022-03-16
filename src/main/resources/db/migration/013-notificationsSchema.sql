create table if not exists notifications (
        id                bigint       not null primary key,
        created_at        datetime(6)  null,
        material_id       bigint       null,
        notification_type varchar(255) null,
        publisher_id      bigint       null,
        subscriber_id     bigint       null,
        constraint FK7fxuahvgdjq4ax24tncik4s01 foreign key (subscriber_id) references users (id),
        constraint FKce10ddn279nugs983aruwyt1d foreign key (publisher_id) references users (id)
);