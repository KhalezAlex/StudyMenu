-- добавление связей

alter table if exists app_user_t add constraint UK_p67ono8k3fpip2cvwvs1i71p4 unique (telegram_user_id);
alter table if exists app_user_t add constraint FKi9lowk2137j2cv493n3j59res foreign key (admin_id) references admin_t;
alter table if exists app_user_t add constraint FK6y56y067lqa6uul3l4yhile0p foreign key (company_id) references company_t;
alter table if exists app_user_t add constraint FK4ec45sbeeokvvncjhs3gaiiw4 foreign key (department_id) references department_t;
alter table if exists app_user_t add constraint FKmlola79fgwn6tfv4n86sfutq5 foreign key (employee_id) references employee_t;

alter table if exists category_t add constraint FK87qjm4etpb5lqcmefeku1h5s1 foreign key (department_id) references department_t;
alter table if exists category_t add constraint FKtbscvyp0m43if6vaksuyc29lb foreign key (excel_doc_id) references excel_document_t;

alter table if exists department_t add constraint FKc9o3ace2u8nc54i0w8f1k2u6x foreign key (company_id) references company_t;

alter table if exists employee_t add constraint FK9pkilhik6q7nxbl7nqwsilaut foreign key (department_id) references department_t;

alter table if exists ingredient_t add constraint FKbt1tcqqpwxtoekx394tuet6do foreign key (item_id) references item_t;

alter table if exists item_t add constraint FK7e375auelwueew9w2mmkit5jn foreign key (category_id) references category_t;