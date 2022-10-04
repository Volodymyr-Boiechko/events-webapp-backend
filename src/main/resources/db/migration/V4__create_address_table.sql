CREATE TABLE IF NOT EXISTS address
(
    id          BIGINT AUTO_INCREMENT,
    country     VARCHAR(100) NOT NULL,
    city        VARCHAR(100) NOT NULL,
    street      VARCHAR(100),
    postal_code VARCHAR(50),
    PRIMARY KEY (id)
);