CREATE TABLE reservations (
                              id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                              user_id BIGINT NOT NULL,
                              session_id BIGINT NOT NULL,
                              total_price_in_cents INT NOT NULL,
                              status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                              created_at TIMESTAMP NOT NULL,
                              CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES users(id),
                              CONSTRAINT fk_reservation_session FOREIGN KEY (session_id) REFERENCES sessions(id)
);