CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE devices (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50),
    serial_number VARCHAR(100) UNIQUE,
    owner_id BIGINT REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE accountability_partners (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100)
);

CREATE TABLE user_accountability_partners (
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    partner_id BIGINT REFERENCES accountability_partners(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, partner_id)
);
