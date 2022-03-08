create table if not exists verification_tokens (
    id           bigint auto_increment primary key,
    confirmed_at datetime(6)  null,
    created_at   datetime(6)  null,
    expires_at   datetime(6)  null,
    token        varchar(255) null,
    user_id      bigint       null,
    constraint FK54y8mqsnq1rtyf581sfmrbp4f foreign key (user_id) references users (id)
)



