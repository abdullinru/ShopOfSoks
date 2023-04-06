-- liquebase formatted sql
-- changeset abdullinru:1

create table socks
(
    id          Serial      primary key,
    color       varchar     not null,
    cotton_part integer     not null
)