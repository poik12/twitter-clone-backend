create table if not exists tweets_hashtags (
        tweet_id   bigint not null,
        hashtag_id bigint not null,
        constraint FK4g1ua0nf2xtyi45f0lmxvfcgg foreign key (hashtag_id) references hashtags (id),
        constraint FKst72iv9um9094kdo7xvtb9fb9 foreign key (tweet_id) references tweets (id)
);