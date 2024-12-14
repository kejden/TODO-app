CREATE TABLE task (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   title VARCHAR(255) NOT NULL,
   description TEXT NOT NULL,
   category_id BIGINT,
   user_id BIGINT NOT NULL,
   status VARCHAR(50) NOT NULL,
   FOREIGN KEY (category_id) REFERENCES category(id),
   FOREIGN KEY (user_id) REFERENCES `user`(user_id)
);

