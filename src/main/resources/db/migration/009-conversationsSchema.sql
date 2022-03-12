create table if not exists conversations (
        id                     bigint       not null primary key,
        created_at             datetime(6)  null,
        latest_message_content varchar(255) null,
        latest_message_read    bit          null,
        latest_message_time    datetime(6)  null,
        updated_at             datetime(6)  null,
        creator                bigint       null,
        participant            bigint       null,
        constraint FK2d4av2vqc4bri12b84h7vij4 foreign key (creator) references users (id),
        constraint FKqay30a8e04mykddr0asal1g1i foreign key (participant) references users (id)
);

