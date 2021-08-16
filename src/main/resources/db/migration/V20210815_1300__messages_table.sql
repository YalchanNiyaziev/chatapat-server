CREATE TABLE IF NOT EXISTS chat_messages(
    id                  BIGSERIAL                                   NOT NULL PRIMARY KEY,
    message_type        VARCHAR(32)                                 NOT NULL,
    content             TEXT                                        NOT NULL,
    message_ts          TIMESTAMP WITH TIME ZONE                    NOT NULL,
    message_status      VARCHAR(64)                                 NOT NULL,
    sender_id           BIGINT REFERENCES chat_users(id)            NOT NULL,
    conversation_id     BIGINT REFERENCES chat_conversations(id)    NOT NULL
);