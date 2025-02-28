INSERT INTO company_t (email, current_view, state)
-- VALUES ('svytkireev@icloud.com', 'WELCOME_VIEW', 'BASIC_STATE');
VALUES ('klozevitz@yandex.ru', 'WELCOME_VIEW', 'BASIC_STATE');
--     ('ksenpts@mail.ru', 'WELCOME_VIEW', 'BASIC_STATE');

INSERT INTO department_t (company_id, current_view, state)
VALUES (1, 'WELCOME_VIEW', 'BASIC_STATE');
--     (2, 'WELCOME_VIEW', 'BASIC_STATE'),
--     (3, 'WELCOME_VIEW', 'BASIC_STATE');

INSERT INTO employee_t (current_view, state, department_id)
VALUES ('WELCOME_VIEW', 'BASIC_STATE', 1);

INSERT INTO app_user_t (telegram_user_id, username, company_id, department_id, employee_id)
    -- VALUES (315944589, 'Svyatkireev', 1, null, null),
--        (1871866801, 'Kseneks', null, 1, null),
VALUES (292005725, 'Proxodimiec', 1, 1, 1);

-- UPDATE company_t
-- SET app_user_id = 1
-- WHERE id = 1;
-- UPDATE department_t
-- SET app_user_id = 1
-- WHERE id = 1;
-- UPDATE employee_t
-- SET app_user_id = 1
-- WHERE id = 1;