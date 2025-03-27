create table if not exists chat(
    id bigint primary key
);
create table if not exists link(
   id serial primary key ,
   url varchar(255) not null unique ,
   tags varchar(255) not null ,
   filters varchar(255) not null ,
   update varchar,
   chat_id bigint,
   foreign key (chat_id) references chat(id)
);
