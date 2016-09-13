# User schema

# --- !Ups
ALTER TABLE user
ADD money BIGINT NOT NULL DEFAULT 0;

# --- !Downs
ALTER TABLE user
DROP COLUMN money