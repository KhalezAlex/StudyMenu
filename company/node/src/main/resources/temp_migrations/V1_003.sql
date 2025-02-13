-- создание таблиц

create table admin_t
(
    id int8 generated by default as identity,
    app_user_id int8 not null ,
    primary key (id)
);

create table app_user_t
(
    id int8 generated by default as identity,
    first_login_date timestamp not null default current_timestamp,
    telegram_user_id int8 not null unique,
    username varchar(255),
    admin_id int8,
    company_id int8,
    department_id int8,
    employee_id int8,
    primary key (id)
);

create table category_t
(
    id int8 generated by default as identity,
    name varchar(100) not null,
    department_id int8 not null,
    primary key (id)
);

create table company_t (
    id int8 generated by default as identity,
    email varchar(50),
    name varchar(50),
    current_view varchar(100),
--     app_user_id int8,
    state varchar(50),
    primary key (id)
);

create table department_t
(
    id int8 generated by default as identity,
    state varchar(50),
--     app_user_id int8,
--     company_id int8,
    primary key (id)
);

create table employee_t
(
    id int8 generated by default as identity,
    state varchar(50),
    app_user_id int8 not null,
    department_id int8 not null,
    primary key (id)
);

create table ingredient_t
(
    id int8 generated by default as identity,
    name varchar(50),
    units varchar(20),
    weight float8,
    item_id int8 not null,
    primary key (id)
);

create table item_t
(
    id int8 generated by default as identity,
    name varchar(100),
    units varchar(20),
    weight float8,
    category_id int8 not null,
    primary key (id)
);