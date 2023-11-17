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
    owner to test;

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
    owner to test;

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
    owner to test;

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
    owner to test;
--
-- insert into clients(id, firstname, lastname, phone_number)
-- values (1, 'Маргарита', 'Мастерова', '86666666666'),
--        (2, 'Анастасия', 'Романова', '89376124591'),
--        (3, 'Станислав', 'Асафьев', '83560213495');
--
-- insert into coaches(id, firstname, lastname, phone_number)
-- values (1, 'Светлана', 'Коваленко', '80239450137'),
--        (2, 'Руслан', 'Греческий', '89217450134'),
--        (3, 'Антон', 'Белоусов', '89120451734');
--
-- insert into groups(id, name, id_coach)
-- values (1, 'Тайский бокс', 3),
--        (2, 'Кроссфит', 1),
--        (3, 'Растяжка', 2),
--        (4, 'ММА', 1),
--        (5, 'Бразильское джиу-джитсу', 3);
--
-- insert into clients_groups(id_client, id_group)
-- values (1, 1),
--        (1, 2),
--        (2, 1),
--        (2, 4),
--        (2, 5),
--        (3, 5);