CREATE TABLE IF NOT EXISTS chat_user_addresses(
    id          BIGSERIAL       NOT NULL PRIMARY KEY,
    country     VARCHAR(512)    NOT NULL,
    city        VARCHAR(512)    NOT NULL,
    street      VARCHAR(512)    NOT NULL,
    post_code   VARCHAR(512)    NOT NULL
);

CREATE TABLE IF NOT EXISTS chat_users(
    id              BIGSERIAL                   NOT NULL PRIMARY KEY,
    chat_name       VARCHAR(64),
    first_name      VARCHAR(128)                NOT NULL,
    last_name       VARCHAR(128)                NOT NULL,
    birth_date      DATE                        NOT NULL,
    gender          VARCHAR(64)                 NOT NULL,
    status          VARCHAR(64)                 NOT NULL,
    picture         TEXT,
    locked          BOOLEAN                     NOT NULL DEFAULT false,
    closed          BOOLEAN                     NOT NULL DEFAULT false,
    username        VARCHAR(256)                NOT NULL UNIQUE,
    password        TEXT                        NOT NULL,
    user_role       VARCHAR(64)                 NOT NULL,
    registration_ts TIMESTAMP WITH TIME ZONE    NOT NULL,
    address_id      BIGINT                      REFERENCES chat_user_addresses(id)
);

INSERT INTO chat_users(username, password, user_role, first_name, last_name, birth_date, gender, status, locked, closed, registration_ts) VALUES
-- pass: r00t_CHAT@63
('rt.admn@chatapat.com', '$2a$10$0.JW6ea4MbLq.cDdI1X0o.sP5iuIp7BPtAprAm50cN5Ebi8Nr79w.', 'ROOT', 'admin', 'root', NOW()::date, 'UNKNOWN', 'ACTIVE', false, false, NOW()),
-- pass: maInADm_CHAT@66
('main.admn@chatapat.com', '$2a$10$sF.ffutdMdIR9cQqW202M.dVD7K1qY6.Kv4OCopTwaGTzfRTqVuR.', 'ADMIN', 'admin', 'main', NOW()::date, 'UNKNOWN', 'ACTIVE', false, false, NOW()),
-- pass: secondaryADm_CHAT@69
('secondary.admn@chatapat.com', '$2a$10$kcZZ6yrYzVh9jbjwJY7VO.QPfMVPTdNLpjozPhRwgK2gqoWvopFUG', 'ADMIN', 'admin', 'secondary', NOW()::date, 'UNKNOWN', 'ACTIVE', false, false, NOW()),
-- pass: fakeOne
('fakeOne', '$2a$10$eCDu/23sEcP1siu0IHJYRu2BQ84Lh73P.7xCGxM9w1JjHMdhqYx4e', 'STANDARD_USER', 'fake', 'One', NOW()::date, 'MALE', 'ACTIVE', false, false, NOW()),
-- pass: fakeTwo
('fakeTwo', '$2a$10$SqYDnzoecpQJrB1TItx2ce9GY8iI2XHiT5tB8lynjHABediGq5Dya', 'STANDARD_USER', 'fake', 'Two', NOW()::date, 'FEMALE', 'ACTIVE', false, false, NOW()),
-- pass: fakeThree
('fakeThree', '$2a$10$8ZSjWGZuHMJK5SZqVOwobOHtCeEFmWLYQlOAC0AZkIY5YckqXTM5K', 'STANDARD_USER', 'fake', 'Three', NOW()::date, 'MALE', 'ACTIVE', false, false, NOW());