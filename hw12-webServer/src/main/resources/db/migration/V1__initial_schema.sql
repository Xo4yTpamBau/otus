create sequence hibernate_sequence start with 1 increment by 1;

create table client
(
    id int8 not null,
    name varchar(255),
    address_id int8,
    primary key (id));
create table address
(
    id int8 not null,
    street varchar(255),
    primary key (id)
);
create table phone
(
    id int8 not null,
    number varchar(255),
    client_id int8,
    primary key (id)
);
create table users
(
    id int8 not null,
    login varchar(255),
    name varchar(255),
    password varchar(255),
    primary key (id)
);
