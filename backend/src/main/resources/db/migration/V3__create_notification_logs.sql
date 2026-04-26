CREATE TABLE notification_logs (
    id UUID PRIMARY KEY,

    message_id UUID NOT NULL,
    channel_id BIGINT NOT NULL,

    user_id VARCHAR(50) NOT NULL,
    user_name VARCHAR(150) NOT NULL,
    user_email VARCHAR(255),
    user_phone VARCHAR(30),

    status VARCHAR(30) NOT NULL,
    attempts INTEGER NOT NULL,
    error_message VARCHAR(1000),

    idempotency_key VARCHAR(255) NOT NULL UNIQUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_logs_message
        FOREIGN KEY (message_id)
        REFERENCES messages(id),

    CONSTRAINT fk_logs_channel
        FOREIGN KEY (channel_id)
        REFERENCES notification_channels(id)
);
