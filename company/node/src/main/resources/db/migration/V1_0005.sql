--

INSERT INTO company_t (email, current_view, state)
VALUES
    ('klozevitz@yandex.ru', 'REGISTERED_WELCOME_VIEW', 'BASIC_STATE');

INSERT INTO app_user_t (telegram_user_id, username, company_id)
VALUES
    (315944589, 'Svyatkireev', 1);

INSERT INTO department_t (state, company_id)
VALUES
    ('BASIC_STATE', 1);

INSERT INTO app_user_t (telegram_user_id, username, department_id)
VALUES
    (292005725, 'Proxodimiec', 1);