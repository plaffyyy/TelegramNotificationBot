create table if not exists chat(
    id bigint primary key
);
create table if not exists link(
   id bigserial primary key ,
   url varchar(255) not null unique ,
   tags varchar(255) not null ,
   filters varchar(255) not null ,
   update text,
   chat_id bigint,
   foreign key (chat_id) references chat(id)
);
