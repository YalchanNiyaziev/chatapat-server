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
    picture         TEXT                        NOT NULL,
    locked          BOOLEAN                     NOT NULL DEFAULT false,
    closed          BOOLEAN                     NOT NULL DEFAULT false,
    username        VARCHAR(256)                NOT NULL UNIQUE,
    password        TEXT                        NOT NULL,
    user_role       VARCHAR(64)                 NOT NULL,
    registration_ts TIMESTAMP WITH TIME ZONE    NOT NULL,
    address_id      BIGINT                      REFERENCES chat_user_addresses(id)
);

INSERT INTO chat_users(username, password, user_role, first_name, last_name, birth_date, gender, status, picture, locked, closed, registration_ts) VALUES
-- pass: r00t_CHAT@63
('rt.admn@chatapat.com', '$2a$10$0.JW6ea4MbLq.cDdI1X0o.sP5iuIp7BPtAprAm50cN5Ebi8Nr79w.', 'ROOT', 'admin', 'root', NOW()::date, 'UNKNOWN', 'ACTIVE', 'https://cdn.motor1.com/images/mgl/J7EjQ/s1/electric-ford-mustang-by-charge-cars.jpg', false, false, NOW()),
-- pass: maInADm_CHAT@66
('main.admn@chatapat.com', '$2a$10$sF.ffutdMdIR9cQqW202M.dVD7K1qY6.Kv4OCopTwaGTzfRTqVuR.', 'ADMIN', 'admin', 'main', NOW()::date, 'UNKNOWN', 'ACTIVE', 'https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/gettyimages-518361844-1551386275.jpg', false, false, NOW()),
-- pass: secondaryADm_CHAT@69
('secondary.admn@chatapat.com', '$2a$10$kcZZ6yrYzVh9jbjwJY7VO.QPfMVPTdNLpjozPhRwgK2gqoWvopFUG', 'ADMIN', 'admin', 'secondary', NOW()::date, 'UNKNOWN', 'ACTIVE', 'https://static.wikia.nocookie.net/naruto/images/d/dd/Naruto_Uzumaki%21%21.png', false, false, NOW()),
-- pass: fakeOne
('fakeOne', '$2a$10$eCDu/23sEcP1siu0IHJYRu2BQ84Lh73P.7xCGxM9w1JjHMdhqYx4e', 'STANDARD_USER', 'fake', 'One', NOW()::date, 'MALE', 'ACTIVE', 'https://static.posters.cz/image/750/posters/african-lion-i8112.jpg', false, false, NOW()),
-- pass: fakeTwo
('fakeTwo', '$2a$10$SqYDnzoecpQJrB1TItx2ce9GY8iI2XHiT5tB8lynjHABediGq5Dya', 'STANDARD_USER', 'fake', 'Two', NOW()::date, 'FEMALE', 'ACTIVE', 'https://cdn.motor1.com/images/mgl/J7EjQ/s1/electric-ford-mustang-by-charge-cars.jpg', false, false, NOW()),
-- pass: fakeThree
('fakeThree', '$2a$10$8ZSjWGZuHMJK5SZqVOwobOHtCeEFmWLYQlOAC0AZkIY5YckqXTM5K', 'STANDARD_USER', 'fake', 'Three', NOW()::date, 'MALE', 'ACTIVE', 'https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/gettyimages-518361844-1551386275.jpg', false, false, NOW()),
-- pass: fakeOne
('fakeOne1', '$2a$10$eCDu/23sEcP1siu0IHJYRu2BQ84Lh73P.7xCGxM9w1JjHMdhqYx4e', 'STANDARD_USER', 'fake', 'One', NOW()::date, 'MALE', 'ACTIVE', 'https://static.wikia.nocookie.net/naruto/images/d/dd/Naruto_Uzumaki%21%21.png', false, false, NOW()),
-- pass: fakeOne
('fakeOne2', '$2a$10$eCDu/23sEcP1siu0IHJYRu2BQ84Lh73P.7xCGxM9w1JjHMdhqYx4e', 'STANDARD_USER', 'fake', 'One', NOW()::date, 'MALE', 'ACTIVE', 'https://www.audi.com/content/dam/gbp2/career/2021/diversity/1920x1440-mobile-diversity-audi-inclusion.jpg', false, false, NOW()),
-- pass: fakeOne
('fakeOne3', '$2a$10$eCDu/23sEcP1siu0IHJYRu2BQ84Lh73P.7xCGxM9w1JjHMdhqYx4e', 'STANDARD_USER', 'fake', 'One', NOW()::date, 'MALE', 'ACTIVE', 'https://www.audi.com/content/dam/gbp2/career/2021/diversity/1920x1440-mobile-diversity-audi-inclusion.jpg', false, false, NOW()),
-- pass: fakeOne
('fakeOne4', '$2a$10$eCDu/23sEcP1siu0IHJYRu2BQ84Lh73P.7xCGxM9w1JjHMdhqYx4e', 'STANDARD_USER', 'fake', 'One', NOW()::date, 'MALE', 'ACTIVE', 'https://www.audi.com/content/dam/gbp2/career/2021/diversity/1920x1440-mobile-diversity-audi-inclusion.jpg', false, false, NOW()),
-- pass: fakeOne
('fakeOne5', '$2a$10$eCDu/23sEcP1siu0IHJYRu2BQ84Lh73P.7xCGxM9w1JjHMdhqYx4e', 'STANDARD_USER', 'fake', 'One', NOW()::date, 'MALE', 'ACTIVE', 'https://www.audi.com/content/dam/gbp2/career/2021/diversity/1920x1440-mobile-diversity-audi-inclusion.jpg', false, false, NOW()),
-- pass: fakeOne
('fakeOne6', '$2a$10$eCDu/23sEcP1siu0IHJYRu2BQ84Lh73P.7xCGxM9w1JjHMdhqYx4e', 'STANDARD_USER', 'fake', 'One', NOW()::date, 'MALE', 'ACTIVE', 'https://www.audi.com/content/dam/gbp2/career/2021/diversity/1920x1440-mobile-diversity-audi-inclusion.jpg', false, false, NOW()),
-- pass: fakeOne
('fakeOne7', '$2a$10$eCDu/23sEcP1siu0IHJYRu2BQ84Lh73P.7xCGxM9w1JjHMdhqYx4e', 'STANDARD_USER', 'fake', 'One', NOW()::date, 'MALE', 'ACTIVE', 'https://www.audi.com/content/dam/gbp2/career/2021/diversity/1920x1440-mobile-diversity-audi-inclusion.jpg', false, false, NOW()),
-- pass: fakeOne
('fakeOne8', '$2a$10$eCDu/23sEcP1siu0IHJYRu2BQ84Lh73P.7xCGxM9w1JjHMdhqYx4e', 'STANDARD_USER', 'fake', 'One', NOW()::date, 'MALE', 'ACTIVE', 'https://www.audi.com/content/dam/gbp2/career/2021/diversity/1920x1440-mobile-diversity-audi-inclusion.jpg', false, false, NOW()),
-- pass: fakeOne
('fakeOne9', '$2a$10$eCDu/23sEcP1siu0IHJYRu2BQ84Lh73P.7xCGxM9w1JjHMdhqYx4e', 'STANDARD_USER', 'fake', 'One', NOW()::date, 'MALE', 'ACTIVE', 'https://www.audi.com/content/dam/gbp2/career/2021/diversity/1920x1440-mobile-diversity-audi-inclusion.jpg', false, false, NOW()),
-- pass: fakeOne
('fakeOne10', '$2a$10$eCDu/23sEcP1siu0IHJYRu2BQ84Lh73P.7xCGxM9w1JjHMdhqYx4e', 'STANDARD_USER', 'fake', 'One', NOW()::date, 'MALE', 'ACTIVE', 'https://www.audi.com/content/dam/gbp2/career/2021/diversity/1920x1440-mobile-diversity-audi-inclusion.jpg', false, false, NOW()),
-- pass: fakeOne
('fakeOne11', '$2a$10$eCDu/23sEcP1siu0IHJYRu2BQ84Lh73P.7xCGxM9w1JjHMdhqYx4e', 'STANDARD_USER', 'fake', 'One', NOW()::date, 'MALE', 'ACTIVE', 'https://www.audi.com/content/dam/gbp2/career/2021/diversity/1920x1440-mobile-diversity-audi-inclusion.jpg', false, false, NOW()),
-- pass: fakeOne
('fakeOne12', '$2a$10$eCDu/23sEcP1siu0IHJYRu2BQ84Lh73P.7xCGxM9w1JjHMdhqYx4e', 'STANDARD_USER', 'fake', 'One', NOW()::date, 'MALE', 'ACTIVE', 'https://www.audi.com/content/dam/gbp2/career/2021/diversity/1920x1440-mobile-diversity-audi-inclusion.jpg', false, false, NOW()),
-- pass: fakeOne
('fakeOne13', '$2a$10$eCDu/23sEcP1siu0IHJYRu2BQ84Lh73P.7xCGxM9w1JjHMdhqYx4e', 'STANDARD_USER', 'fake', 'One', NOW()::date, 'MALE', 'ACTIVE', 'https://www.audi.com/content/dam/gbp2/career/2021/diversity/1920x1440-mobile-diversity-audi-inclusion.jpg', false, false, NOW()),
-- pass: fakeOne
('fakeOne14', '$2a$10$eCDu/23sEcP1siu0IHJYRu2BQ84Lh73P.7xCGxM9w1JjHMdhqYx4e', 'STANDARD_USER', 'fake', 'One', NOW()::date, 'MALE', 'ACTIVE', 'https://www.audi.com/content/dam/gbp2/career/2021/diversity/1920x1440-mobile-diversity-audi-inclusion.jpg', false, false, NOW()),
-- pass: fakeOne
('fakeOne15', '$2a$10$eCDu/23sEcP1siu0IHJYRu2BQ84Lh73P.7xCGxM9w1JjHMdhqYx4e', 'STANDARD_USER', 'fake', 'One', NOW()::date, 'MALE', 'ACTIVE', 'https://www.audi.com/content/dam/gbp2/career/2021/diversity/1920x1440-mobile-diversity-audi-inclusion.jpg', false, false, NOW());