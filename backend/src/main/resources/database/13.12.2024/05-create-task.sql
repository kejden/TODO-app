CREATE TABLE tasks (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   title VARCHAR(255) NOT NULL,
   description TEXT NOT NULL,
   category_id BIGINT NOT NULL,
   user_id BIGINT NOT NULL,
   status VARCHAR(50) NOT NULL,
   FOREIGN KEY (category_id) REFERENCES categories(id),
   FOREIGN KEY (user_id) REFERENCES users(id)
);

