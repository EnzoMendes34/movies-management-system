CREATE TABLE sessions (
                          id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          movie_id BIGINT NOT NULL,
                          room_id BIGINT NOT NULL,
                          start_time TIMESTAMP NOT NULL,
                          end_time TIMESTAMP NOT NULL,
                          language VARCHAR(20) NOT NULL,
                          price_in_cents INT NOT NULL,
                          status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
                          CONSTRAINT fk_session_movie FOREIGN KEY (movie_id) REFERENCES movies(id),
                          CONSTRAINT fk_session_room FOREIGN KEY (room_id) REFERENCES rooms(id)
);