CREATE TABLE likes
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    CHAR(32) NOT NULL,
    post_id    BIGINT   NOT NULL,
    created_at datetime NOT NULL,
    updated_at datetime NOT NULL,

    CONSTRAINT fk_likes_user_id FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
