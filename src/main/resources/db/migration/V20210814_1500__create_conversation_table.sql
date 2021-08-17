CREATE TABLE IF NOT EXISTS chat_conversations(
    id                  BIGSERIAL                   NOT NULL PRIMARY KEY,
    started_ts          TIMESTAMP WITH TIME ZONE    NOT NULL,
    end_ts              TIMESTAMP WITH TIME ZONE    NOT NULL,
    conversation_type   VARCHAR(32)                 NOT NULL,
    vanishable          BOOLEAN                     NOT NULL
);

CREATE TABLE IF NOT EXISTS chat_user_x_conversation(
    user_id             BIGINT REFERENCES chat_users(id),
    conversation_id     BIGINT REFERENCES chat_conversations(id),
                        PRIMARY KEY (user_id, conversation_id)
);