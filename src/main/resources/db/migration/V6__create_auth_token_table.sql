CREATE TABLE IF NOT EXISTS auth_token
(
    id                       BIGINT AUTO_INCREMENT,
    user_id                  BIGINT,
    access_token             VARCHAR(250),
    refresh_token            VARCHAR(200),
    token_type               VARCHAR(20),
    refresh_token_expires_in DATETIME,
    issued                   DATETIME,
    expires                  DATETIME,
    destinationType          VARCHAR(100),
    PRIMARY KEY (id),
    CONSTRAINT FOREIGN KEY (user_id) REFERENCES user (id)
)