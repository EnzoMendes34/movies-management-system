CREATE TABLE payments (
                          id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          reservation_id BIGINT NOT NULL UNIQUE,
                          stripe_payment_intent_id VARCHAR(255) NOT NULL UNIQUE,
                          amount_in_cents INT NOT NULL,
                          currency VARCHAR(10) NOT NULL DEFAULT 'BRL',
                          status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                          created_at TIMESTAMP NOT NULL,
                          paid_at TIMESTAMP,
                          CONSTRAINT fk_payment_reservation FOREIGN KEY (reservation_id) REFERENCES reservations(id)
);