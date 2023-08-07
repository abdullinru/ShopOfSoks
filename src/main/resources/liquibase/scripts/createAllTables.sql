-- liquebase formatted sql
-- changeset abdullinru:1

create table socks
(
    id          Serial      primary key,
    color       varchar     not null,
    cotton_part integer     not null
);
create table count
(
    id          Serial      primary key,
    count       integer     not null,
    sock_id     integer     REFERENCES socks (id)
)
