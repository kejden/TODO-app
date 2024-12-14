INSERT INTO task (title, description, category_id, user_id, status) VALUES
    ('Task 1', 'Description for Task 1', (SELECT id FROM category WHERE name = 'Category 1' AND user_id = 1), 1, 'NEW'),
    ('Task 2', 'Description for Task 2', (SELECT id FROM category WHERE name = 'Category 1' AND user_id = 1), 1, 'IN_PROGRESS'),
    ('Task 3', 'Description for Task 3', NULL, 1, 'COMPLETED'),
    ('Task 4', 'Description for Task 4', (SELECT id FROM category WHERE name = 'Category 2' AND user_id = 1), 1, 'NEW'),
    ('Task 5', 'Description for Task 5', (SELECT id FROM category WHERE name = 'Category 2' AND user_id = 1), 1, 'IN_PROGRESS'),
    ('Task 6', 'Description for Task 6', NULL, 1, 'IN_PROGRESS'),
    ('Task 7', 'Description for Task 7', (SELECT id FROM category WHERE name = 'Category 3' AND user_id = 1), 1, 'COMPLETED'),
    ('Task 8', 'Description for Task 8', (SELECT id FROM category WHERE name = 'Category 1' AND user_id = 2), 2, 'NEW'),
    ('Task 9', 'Description for Task 9', (SELECT id FROM category WHERE name = 'Category 1' AND user_id = 2), 2, 'NEW'),
    ('Task 10', 'Description for Task 10', (SELECT id FROM category WHERE name = 'Category 2' AND user_id = 2), 2, 'NEW'),
    ('Task 11', 'Description for Task 11', (SELECT id FROM category WHERE name = 'Category 2' AND user_id = 2), 2, 'COMPLETED'),
    ('Task 12', 'Description for Task 12', NULL, 2, 'COMPLETED'),
    ('Task 13', 'Description for Task 13', NULL, 2, 'COMPLETED');
