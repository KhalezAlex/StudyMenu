create table admin_t
(
    id int8 generated by default as identity,
    primary key (id)
);

create table app_user_t
(
    id int8 generated by default as identity,
    first_login_date timestamp,
    telegram_user_id int8 not null,
    username varchar(30),
    admin_id int8,
    company_id int8,
    department_id int8,
    employee_id int8,
    primary key (id)
);

create table category_t
(
    id int8 generated by default as identity,
    name varchar(100),
    department_id int8,
    excel_doc_id int8,
    primary key (id)
);

create table company_t
(
    id int8 generated by default as identity,
    current_view varchar(100),
    email varchar(100),
    name varchar(100),
    state varchar(100) not null,
    primary key (id)
);

create table department_t
(
    id int8 generated by default as identity,
    state varchar(100),
    current_view varchar(100),
    company_id int8,
    primary key (id)
);

create table employee_t
(
    id int8 generated by default as identity,
    state varchar(100),
    department_id int8,
    primary key (id)
);

create table excel_document_t
(
    id int8 generated by default as identity,
    as_byte_array bytea,
    file_size int8,
    mime_type varchar(50),
    name varchar(100),
    telegram_file_id varchar(100),
    primary key (id)
);

create table ingredient_t
(
    id int8 generated by default as identity,
    name varchar(50),
    units varchar(20),
    weight float8,
    item_id int8,
    primary key (id)
);

create table item_t
(
    id int8 generated by default as identity,
    name varchar(100),
    units varchar(20),
    weight float8,
    category_id int8,
    primary key (id)
);