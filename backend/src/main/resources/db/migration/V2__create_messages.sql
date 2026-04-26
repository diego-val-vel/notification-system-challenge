CREATE TABLE messages (
    id UUID PRIMARY KEY,
    category_id BIGINT NOT NULL,
    body VARCHAR(1000) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_messages_category
        FOREIGN KEY (category_id)
        REFERENCES categories(id)
);
