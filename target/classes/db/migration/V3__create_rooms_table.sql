CREATE TABLE rooms (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       room_name VARCHAR(100) NOT NULL,
                       type VARCHAR(20) NOT NULL,
                       capacity INT NOT NULL,
                       enabled BOOLEAN NOT NULL DEFAULT TRUE
);