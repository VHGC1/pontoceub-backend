DROP TABLE IF EXISTS roles create table roles (
       id   int         not null,
       name varchar(20) not null,
       constraint PK_ROLE primary key (id),
       constraint ROLES_NAME_UNIQUE unique (name)
    )

DROP TABLE IF EXISTS users create table users (
    id       int          identity,
    name     varchar(100) not null,
    email    varchar(100) not null,
    password varchar(100) not null,
    constraint PK_USER  primary key (id),
    constraint USER_EMAIL_UNIQUE unique (email)
)

DROP TABLE IF EXISTS users_roles create table users_roles (
    id      int identity,
    user_id int not null,
    role_id int not null,
    primary key (id)
)

alter table users_roles
   add constraint FK_USERS_ROLES_ROLE foreign key (role_id)
    references roles

alter table users_roles
   add constraint FK_USERS_ROLES_USER foreign key (user_id)
    references users