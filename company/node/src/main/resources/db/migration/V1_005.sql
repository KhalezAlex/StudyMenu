INSERT INTO app_user_t (id, telegram_user_id, username) VALUES
                           (1,  292005725, 'Proxodimiec');

INSERT INTO company_t (id, email, current_view, app_user_id, state) VALUES
    (1, 'klozevitz@yandex.ru', 'REGISTERED_WELCOME_VIEW', 1, 'BASIC_STATE');

UPDATE app_user_t SET company_id = 1 WHERE id = 1;