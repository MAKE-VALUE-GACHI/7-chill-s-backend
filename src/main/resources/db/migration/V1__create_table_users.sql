CREATE TABLE users
(
    id         CHAR(32) PRIMARY KEY,
    name       VARCHAR(30) NOT NULL,
    email      VARCHAR(50) NOT NULL,
    created_at DATETIME    NOT NULL,
    updated_at DATETIME    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
