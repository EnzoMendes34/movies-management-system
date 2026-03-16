CREATE TABLE reservation_seats (
                                   id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                   reservation_id BIGINT NOT NULL,
                                   seat_id BIGINT NOT NULL,
                                   session_id BIGINT NOT NULL,
                                   CONSTRAINT fk_rs_reservation FOREIGN KEY (reservation_id) REFERENCES reservations(id),
                                   CONSTRAINT fk_rs_seat FOREIGN KEY (seat_id) REFERENCES seats(id),
                                   CONSTRAINT fk_rs_session FOREIGN KEY (session_id) REFERENCES sessions(id),
                                   CONSTRAINT uq_seat_session UNIQUE (seat_id, session_id)
);