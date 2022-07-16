CREATE TABLE IF NOT EXISTS role
(
    id        BIGINT AUTO_INCREMENT,
    role_name VARCHAR(30) UNIQUE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE permission
(
    id              BIGINT AUTO_INCREMENT,
    permission_name VARCHAR(30) UNIQUE NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE role_permission
(
    role_id       BIGINT,
    permission_id BIGINT,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES role (id),
    FOREIGN KEY (permission_id) REFERENCES permission (id)
);

INSERT INTO role (role_name)
VALUES ('USER'), ('USER_ORGANIZER'), ('ADMIN')