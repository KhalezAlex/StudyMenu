INSERT INTO company_t (email, current_view, state)
VALUES ('klozevitz@yandex.ru', 'WELCOME_VIEW', 'BASIC_STATE');

INSERT INTO department_t (company_id, current_view, state)
VALUES (1, 'WELCOME_VIEW', 'BASIC_STATE');

INSERT INTO employee_t (current_view, state, department_id)
VALUES ('WELCOME_VIEW', 'BASIC_STATE', 1);

INSERT INTO app_user_t (telegram_user_id, username, company_id, department_id, employee_id)
VALUES (292005725, 'Proxodimiec', 1, 1, 1);