CREATE TABLE post_topics
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id  BIGINT NOT NULL,
    topic_id BIGINT NOT NULL,

    CONSTRAINT fk_post_topics_post_id FOREIGN KEY (post_id) REFERENCES posts (id),
    CONSTRAINT fk_post_topics_topic_id FOREIGN KEY (topic_id) REFERENCES topics (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
