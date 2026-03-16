CREATE TABLE seats (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       room_id BIGINT NOT NULL,
                       row_letter CHAR(1) NOT NULL,
                       seat_number INT NOT NULL,
                       type VARCHAR(20) NOT NULL,
                       CONSTRAINT fk_seat_room FOREIGN KEY (room_id) REFERENCES rooms(id)
);