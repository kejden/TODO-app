INSERT INTO tasks (title, description, category_id, user_id, status) VALUES
    ('Task 1', 'Description for Task 1', (SELECT id FROM categories WHERE name = 'Category 1' AND user_id = 1), 1, 'NEW'),
    ('Task 2', 'Description for Task 2', (SELECT id FROM categories WHERE name = 'Category 2' AND user_id = 1), 1, 'IN_PROGRESS'),
    ('Task 3', 'Description for Task 3', (SELECT id FROM categories WHERE name = 'Category 3' AND user_id = 1), 1, 'COMPLETED'),
    ('Task 4', 'Description for Task 4', (SELECT id FROM categories WHERE name = 'Category 1' AND user_id = 2), 2, 'NEW'),
    ('Task 5', 'Description for Task 5', (SELECT id FROM categories WHERE name = 'Category 2' AND user_id = 2), 2, 'NEW');
