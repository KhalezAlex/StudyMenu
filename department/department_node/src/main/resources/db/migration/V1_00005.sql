CREATE OR REPLACE FUNCTION delete_category_by_name_and_department_id_cascade(categoryName VARCHAR, departmentId BIGINT)
RETURNS BOOLEAN AS
$$
BEGIN

-- Удаление вопросов, связанных с элементами категории
DELETE FROM test_question_t
WHERE item_id IN (
    SELECT id FROM item_t WHERE category_id IN (
        SELECT id FROM category_t WHERE name = categoryName AND department_id = departmentId
    )
);

-- Удаление ингредиентов, связанных с элементами категории
DELETE FROM ingredient_t
WHERE item_id IN (
    SELECT id FROM item_t WHERE category_id IN (
        SELECT id FROM category_t WHERE name = categoryName AND department_id = departmentId
    )
);

-- Удаление элементов, связанных с категорией
DELETE FROM item_t
WHERE category_id IN (
    SELECT id FROM category_t WHERE name = categoryName AND department_id = departmentId
);

-- Удаление самой категории
DELETE FROM category_t
WHERE name = categoryName AND department_id = departmentId;

-- Возвращаем TRUE, тк с void еще не разобрался
RETURN TRUE;
END;
$$
LANGUAGE plpgsql;