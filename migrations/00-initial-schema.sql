create table link(
    id serial primary key ,
    url varchar(255) not null unique ,
    tags jsonb not null ,
    filters jsonb not null ,
    update jsonb
);

create table chat(
    id serial primary key
);

create table chat_link(
    link_id bigint,
    chat_id bigint,
    primary key (link_id, chat_id),
    foreign key (link_id) references link(id) on delete cascade ,
    foreign key (chat_id) references chat(id) on delete cascade
);
