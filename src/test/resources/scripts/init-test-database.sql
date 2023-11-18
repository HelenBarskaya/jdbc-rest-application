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
-- insert into clients(firstname, lastname, phone_number)
-- values ('Маргарита', 'Мастерова', '86666666666'),
--        ('Анастасия', 'Романова', '89376124591'),
--        ('Станислав', 'Асафьев', '83560213495');
--
-- insert into coaches(firstname, lastname, phone_number)
-- values ('Светлана', 'Коваленко', '80239450137'),
--        ('Руслан', 'Греческий', '89217450134'),
--        ('Антон', 'Белоусов', '89120451734');
--
-- insert into groups(name, id_coach)
-- values ('Тайский бокс', 3),
--        ('Кроссфит', 1),
--        ('Растяжка', 2),
--        ('ММА', 1),
--        ('Бразильское джиу-джитсу', 3);
--
-- insert into clients_groups(id_client, id_group)
-- values (1, 1),
--        (1, 2),
--        (2, 1),
--        (2, 4),
--        (2, 5),
--        (3, 5);