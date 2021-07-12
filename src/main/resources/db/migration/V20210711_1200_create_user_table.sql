CREATE TABLE IF NO EXISTS chat_address(
    id          BIGSERIAL       NOT NULL PRIMARY KEY,
    country     VARCHAR(512)    NOT NULL,
    city        VARCHAR(512)    NOT NULL,
    street      VARCHAR(512)    NOT NULL,
    post_code   VARCHAR(512)    NOT NULL
);

CREATE TABLE IF NO EXISTS chat_users(
    id          BIGSERIAL       NOT NULL PRIMARY KEY,
    chat_name   VARCHAR(64),
    first_name  VARCHAR(128)    NOT NULL,
    last_name   VARCHAR(128)    NOT NULL,
    birth_date  DATE            NOT NULL,
    gender      VARCHAR(64)     NOT NULL,
    status      VARCHAR(64)     NOT NULL,
    locked      BOOLEAN         NOT NULL DEFAULT false,
    closed      BOOLEAN         NOT NULL DEFAULT false,
    username    VARCHAR(256)    NOT NULL UNIQUE,
    password    TEXT            NOT NULL,
    user_role   VARCHAR(64)     NOT NULL,
    address_id  BIGINT          REFERENCES chat_address(id)
);