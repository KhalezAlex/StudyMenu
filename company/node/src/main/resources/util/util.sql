DELETE FROM app_user_t WHERE id =
(
    SELECT id FROM app_user_t LIMIT 1
);

DELETE FROM company_t WHERE id =
(
    SELECT id FROM company_t LIMIT 1
);