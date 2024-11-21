-- liquibase formatted sql

-- changeset leandro:1
CREATE TABLE roles (
    id UUID DEFAULT RANDOM_UUID() NOT NULL,
    name varchar(255),
    PRIMARY KEY (id)
);

CREATE TABLE membership (
    id UUID DEFAULT RANDOM_UUID() NOT NULL,
    member_id UUID,
    role_id UUID,
    team_id UUID,
    PRIMARY KEY (id),
    CONSTRAINT fk_membership_role
        FOREIGN KEY (role_id)
        REFERENCES roles(id)
);

INSERT INTO roles (id, name) VALUES ('1770f8f4-e117-4d87-85a3-60173ddd352f', 'DEVELOPER');
INSERT INTO roles (id, name) VALUES ('4405b3dc-a535-41c2-8b6b-38ace1a6a8ee', 'PRODUCT_OWNER');
INSERT INTO roles (id, name) VALUES ('a8cf155d-8b7e-4c34-b559-5e6235296710', 'TESTER');