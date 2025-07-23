ALTER TABLE student
    ADD CONSTRAINT age_constraint CHECK (age > 16),
    ALTER COLUMN age set default 20,
    ADD CONSTRAINT name_constraint UNIQUE (name),
    ALTER COLUMN name SET NOT NULL;

