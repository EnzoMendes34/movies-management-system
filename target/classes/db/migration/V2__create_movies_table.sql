CREATE TABLE movies (
                        id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        title VARCHAR(200) NOT NULL,
                        synopsis TEXT NOT NULL,
                        duration_in_minutes INT NOT NULL,
                        genre VARCHAR(100) NOT NULL,
                        rating VARCHAR(10) NOT NULL,
                        poster_url VARCHAR(500) NOT NULL,
                        release_date DATE NOT NULL,
                        enabled BOOLEAN NOT NULL DEFAULT TRUE
);
