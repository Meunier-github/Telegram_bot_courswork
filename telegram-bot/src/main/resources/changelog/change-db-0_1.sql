-- liquibase formatted sql

-- changeset user1:1
create TABLE notification_task (
    id SERIAL PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    notification_date_time TIMESTAMP NOT NULL,
    created_at DATE NOT NULL,
    task VARCHAR(255) NOT NULL
)