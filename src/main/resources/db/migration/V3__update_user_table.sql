ALTER TABLE user ADD COLUMN role_id BIGINT NOT NULL;
ALTER TABLE user ADD CONSTRAINT FOREIGN KEY (role_id) REFERENCES role(id);