INSERT INTO chat_user_connections(requester_id, bearer_id, is_connection_request, connected, blocked, last_update_ts, updated_by) VALUES
((SELECT id from chat_users where username = 'fakeTwo'), (SELECT id from chat_users where username = 'fakeOne'), false, true, false, '2021-08-20T18:00:11.712942Z', 'fakeOne'),
((SELECT id from chat_users where username = 'fakeThree'), (SELECT id from chat_users where username = 'fakeOne'), false, true, false, '2021-08-20T18:00:12.712942Z', 'fakeOne'),
((SELECT id from chat_users where username = 'fakeTwo'), (SELECT id from chat_users where username = 'fakeThree'), false, true, false, '2021-08-20T18:00:13.712942Z', 'fakeThree');

WITH inserment1 AS (
INSERT INTO chat_conversations(started_ts, end_ts, conversation_type, vanishable )
VALUES (NOW(), NOW(), 'CHAT', false)
    RETURNING id
    ),
    inserment2 AS (
INSERT INTO chat_user_x_conversation(user_id, conversation_id) VALUES
    ((SELECT id from chat_users where username = 'fakeOne'), (SELECT * FROM inserment1)),
    ((SELECT id from chat_users where username = 'fakeTwo'), (SELECT * FROM inserment1))
    )
INSERT INTO chat_conversation_messages(message_type, content, message_ts, message_status, sender_id, conversation_id) VALUES
    ('TEXT', 'Hello friend', '2021-08-20T18:01:11.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeOne'), (SELECT * FROM inserment1)),
    ('TEXT', 'Hi fake One, Nice to meet you. I was searching you.', '2021-08-20T18:01:21.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeTwo'), (SELECT * FROM inserment1)),
    ('TEXT', 't is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout', '2021-08-20T18:02:11.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeTwo'), (SELECT * FROM inserment1)),
    ('TEXT', 'pposed to using Content here, content here, making it look like readable English. Many desktop publishing packages and web page editors now use ', '2021-08-20T18:02:21.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeTwo'), (SELECT * FROM inserment1)),
    ('TEXT', ' m Ipsum passages, and', '2021-08-20T18:03:11.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeOne'), (SELECT * FROM inserment1)),
    ('TEXT', ' m Ipsum passages, andsdasdas asfasf re ', '2021-08-20T18:04:11.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeOne'), (SELECT * FROM inserment1)),
    ('TEXT', ' It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and', '2021-08-20T18:04:41.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeTwo'), (SELECT * FROM inserment1)),
    ('TEXT', ' m Ipsum passages, assassasndsdasdas asfasf re ', '2021-08-20T18:05:11.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeOne'), (SELECT * FROM inserment1)),
    ('TEXT', ' d. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and', '2021-08-20T18:06:11.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeTwo'), (SELECT * FROM inserment1)),
    ('TEXT', ' It has survived not only five centuries, but also tLetraset sheets containing Lorem Ipsum passages, and', '2021-08-20T18:06:51.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeTwo'), (SELECT * FROM inserment1)),
    ('TEXT', ' m Ipsum passages, andsasdasdasdfasfasf as fas fa fasfdasdas asfasf re ', '2021-08-20T18:07:11.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeOne'), (SELECT * FROM inserment1)),
    ('TEXT', ' m Ipsum passages, andafsasf ase fasf asdasdas asfasf re ', '2021-08-20T18:08:11.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeOne'), (SELECT * FROM inserment1)),
    ('TEXT', ' m Ipsum passandsdasdas asfasf re ', '2021-08-20T18:09:11.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeOne'), (SELECT * FROM inserment1)),
    ('TEXT', 'Hi fake One, Nice to meet you. I was searching yousdasdasdasda asdf asfsdg s as dfaf.', '2021-08-20T18:10:11.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeTwo'), (SELECT * FROM inserment1)),
    ('TEXT', ' m Ipsum passages, andsdasdas asfasf re ', '2021-08-20T18:11:11.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeOne'), (SELECT * FROM inserment1)),
    ('TEXT', ' m Ipsum passages, asdasd asfasf re ', '2021-08-20T18:00:12.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeOne'), (SELECT * FROM inserment1)),
    ('TEXT', '. I was searching you.', '2021-08-20T18:00:13.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeTwo'), (SELECT * FROM inserment1)),
    ('TEXT', 'Where are youI was searching you.', '2021-08-20T18:14:11.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeTwo'), (SELECT * FROM inserment1)),
    ('TEXT', 'Alalala m Ipsum passages, asdasd asfasf re ', '2021-08-20T18:15:11.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeOne'), (SELECT * FROM inserment1));




WITH inserment1 AS (
INSERT INTO chat_conversations(started_ts, end_ts, conversation_type, vanishable )
VALUES (NOW(), NOW(), 'CHAT', false)
    RETURNING id
    ),
    insertment2 AS (
INSERT INTO chat_user_x_conversation(user_id, conversation_id) VALUES
    ((SELECT id from chat_users where username = 'fakeOne'), (SELECT * FROM inserment1)),
    ((SELECT id from chat_users where username = 'fakeThree'), (SELECT * FROM inserment1))
    )
INSERT INTO chat_conversation_messages(message_type, content, message_ts, message_status, sender_id, conversation_id) VALUES
    ('TEXT', 'Hello friend', '2021-08-20T18:10:11.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeOne'), (SELECT * FROM inserment1)),
    ('TEXT', 'Hi fake One, Nice to meet you. I was searching you.', '2021-08-20T18:11:11.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeThree'), (SELECT * FROM inserment1));


WITH inserment1 AS (
INSERT INTO chat_conversations(started_ts, end_ts, conversation_type, vanishable )
VALUES (NOW(), NOW(), 'CHAT', false)
    RETURNING id
    ),
    insertment2 AS (
INSERT INTO chat_user_x_conversation(user_id, conversation_id) VALUES
    ((SELECT id from chat_users where username = 'fakeTwo'), (SELECT * FROM inserment1)),
    ((SELECT id from chat_users where username = 'fakeThree'), (SELECT * FROM inserment1))
    )
INSERT INTO chat_conversation_messages(message_type, content, message_ts, message_status, sender_id, conversation_id) VALUES
    ('TEXT', 'Hello friend', '2021-08-20T18:08:11.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeOne'), (SELECT * FROM inserment1)),
    ('IMAGE', 'https://miro.medium.com/max/1033/1*MAsNORFL89roPfIFMBnA4A.jpeg', '2021-08-20T18:09:11.712942Z', 'DELIVERED', (SELECT id from chat_users where username = 'fakeThree'), (SELECT * FROM inserment1));

