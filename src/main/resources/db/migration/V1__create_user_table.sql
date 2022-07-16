CREATE TABLE IF NOT EXISTS user
(
    id           BIGINT AUTO_INCREMENT,
    public_id    VARCHAR(25) UNIQUE   NOT NULL,
    user_name    VARCHAR(25) UNIQUE   NOT NULL,
    password     VARCHAR(256),
    email        VARCHAR(100) UNIQUE  NOT NULL,
    first_name   VARCHAR(100)         NULL,
    last_name    VARCHAR(100)         NULL,
    phone_number VARCHAR(30)          NULL,
    birth_date   DATE,
    created_at   DATETIME             NOT NULL,
    is_active    TINYINT(1) DEFAULT 0 NOT NULL,
    PRIMARY KEY (id)
)