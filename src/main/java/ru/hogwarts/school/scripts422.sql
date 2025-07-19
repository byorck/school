CREATE TABLE people
(
    id                  SERIAL PRIMARY KEY,
    name                TEXT    NOT NULL,
    age                 INTEGER NOT NULL,
    age_constraint      INTEGER CHECK (age >= 18),
    has_drivers_license BOOLEAN NOT NULL
);

CREATE TABLE cars
(
    id    SERIAL PRIMARY KEY,
    brand TEXT  NOT NULL,
    model TEXT  NOT NULL,
    price MONEY NOT NULL
);

CREATE TABLE people_cars
(
    people_id INTEGER,
    car_id    INTEGER,
    PRIMARY KEY (people_id, car_id),
    FOREIGN KEY (people_id) REFERENCES people (id) ON DELETE CASCADE,
    FOREIGN KEY (car_id) REFERENCES cars (id) ON DELETE CASCADE
)
