CREATE TABLE user_topics
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    CHAR(32) NOT NULL,
    topic_id   BIGINT   NOT NULL,
    created_at datetime NOT NULL,
    updated_at datetime NOT NULL,

    CONSTRAINT fk_user_topics_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_topics_topic_id FOREIGN KEY (topic_id) REFERENCES topics (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
