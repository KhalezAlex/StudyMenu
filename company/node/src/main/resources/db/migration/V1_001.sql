-- убрать констрейнты для удаления таблиц
alter table if exists admin_t drop constraint if exists FK7dvp2xao1ajqcthvulxrjo7ce;
alter table if exists app_user_t drop constraint if exists FKi9lowk2137j2cv493n3j59res;
alter table if exists app_user_t drop constraint if exists FK6y56y067lqa6uul3l4yhile0p;
alter table if exists app_user_t drop constraint if exists FK4ec45sbeeokvvncjhs3gaiiw4;
alter table if exists app_user_t drop constraint if exists FKmlola79fgwn6tfv4n86sfutq5;
alter table if exists category_t drop constraint if exists FK87qjm4etpb5lqcmefeku1h5s1;
alter table if exists department_t drop constraint if exists FKqyguv0hoa1epk0x1kl2t907dv;
alter table if exists department_t drop constraint if exists FKc9o3ace2u8nc54i0w8f1k2u6x;
alter table if exists employee_t drop constraint if exists FKf7c8qtlyr3m64d5wgy7f5k1cv;
alter table if exists employee_t drop constraint if exists FK9pkilhik6q7nxbl7nqwsilaut;
alter table if exists ingredient_t drop constraint if exists FKbt1tcqqpwxtoekx394tuet6do;
alter table if exists item_t drop constraint if exists FK7e375auelwueew9w2mmkit5jn;