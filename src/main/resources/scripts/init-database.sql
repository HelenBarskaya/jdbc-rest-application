create table clients
(
    id           serial
        constraint clients_pk
            primary key,
    firstname    varchar not null,
    lastname     varchar not null,
    phone_number varchar
);

alter table clients
    owner to postgres;

create table coaches
(
    id           serial
        constraint coaches_pk
            primary key,
    firstname    varchar not null,
    lastname     varchar not null,
    phone_number varchar
);

alter table coaches
    owner to postgres;

create table groups
(
    id       serial
        constraint groups_pk
            primary key,
    name     varchar not null,
    id_coach serial
        constraint groups_coaches_id_fk
            references coaches
);

alter table groups
    owner to postgres;

create table clients_groups
(
    id_client serial
        constraint clients_groups_clients_id_fk
            references clients,
    id_group  serial
        constraint clients_groups_groups_id_fk
            references groups,
    constraint clients_groups_pk
        primary key (id_client, id_group)
);

alter table clients_groups
    owner to postgres;