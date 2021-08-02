CREATE TABLE IF NOT EXISTS chat_user_connections(
    id                      BIGSERIAL                   NOT NULL PRIMARY KEY,
    requester_id            BIGINT                      REFERENCES chat_users(id),
    bearer_id               BIGINT                      REFERENCES chat_users(id),
    is_connection_request   BOOLEAN                     NOT NULL,
    connected               BOOLEAN                     NOT NULL,
    blocked                 BOOLEAN                     NOT NULL,
    last_update_ts          TIMESTAMP WITH TIME ZONE    NOT NULL,
    updated_by              VARCHAR(256)                NOT NULL
);