create table if not exists followers (
    id           bigint not null primary key,
    from_user_fk bigint null,
    to_user_fk   bigint null,
    constraint FK39wew2cbfl6a3vkr5owqp1lm1 foreign key (to_user_fk) references users (id),
    constraint FKi7ig92yvk4p8mkb19ycosmqyb foreign key (from_user_fk) references users (id)
);

