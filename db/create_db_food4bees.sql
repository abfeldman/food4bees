-- psql -U postgres -f create_db_food4bees.sql

-- Delete old database and user
DROP DATABASE IF EXISTS food4bees;
DROP USER IF EXISTS food4bees;

-- Create database and user
CREATE USER food4bees PASSWORD 'password';
CREATE DATABASE food4bees OWNER=food4bees;

-- Connect as database owner
\connect food4bees food4bees

CREATE TABLE "user"
(
  id SERIAL PRIMARY KEY,

  name VARCHAR(256) NOT NULL,
  email VARCHAR(256) NOT NULL,
  password VARCHAR(256) NOT NULL
);

-- color is an rgb value from 000000 (black) to FFFFFF (white)
CREATE TABLE plant
(
  id SERIAL PRIMARY KEY,

  user_id INTEGER REFERENCES "user",

  common_name VARCHAR(256),
  latin_name VARCHAR(256) NOT NULL,
  description VARCHAR(4096),
  wikipedia_url VARCHAR(1024),
  color NUMERIC,
  height NUMERIC,
  nectar_index NUMERIC,
  pollen_index NUMERIC,
  start_flowering TIMESTAMP WITHOUT TIME ZONE,
  end_flowering TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE plant_image
(
  id SERIAL PRIMARY KEY,

  plant_id INTEGER REFERENCES "plant",

  caption VARCHAR(256) NOT NULL,
  image BYTEA NOT NULL
);

-- polygon can be a single point
-- amount is the fraction (0.0 to 1.0) of the plant in the area
CREATE TABLE vegetation
(
  id SERIAL PRIMARY KEY,

  user_id INTEGER REFERENCES "user",
  plant_id INTEGER REFERENCES "plant",

  area POLYGON,
  amount NUMERIC,
  "time" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()
);

-- version 0.4
CREATE TABLE email_verification_token
(
  id SERIAL PRIMARY KEY,

  user_id INTEGER REFERENCES "user",

  token VARCHAR(256) NOT NULL,
  "time" TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()
);
CREATE INDEX token_idx ON email_verification_token(token);

ALTER TABLE "user" ADD verified BOOLEAN NOT NULL DEFAULT FALSE;
-- change to TRUE for all existing users
UPDATE "user" SET verified = TRUE;

CREATE TABLE "group"
(
  id SERIAL PRIMARY KEY,

  name VARCHAR(256) NOT NULL
);

INSERT INTO "group" ("name") VALUES ('Administrator');
INSERT INTO "group" ("name") VALUES ('Editor');
INSERT INTO "group" ("name") VALUES ('User');

-- password is 'password'
INSERT INTO "user" (name, email, password) VALUES ('Food4Bees Administrator', 'webmaster@food4bees.org', 'LJLfrWZTAo0=$NDMlg8DXk8d9eimVLogXsfbG8lM=');

ALTER TABLE "user" ADD group_id INTEGER REFERENCES "group";
UPDATE "user" SET group_id = (SELECT id FROM "group" WHERE name = 'User');
UPDATE "user" SET group_id = (SELECT id FROM "group" WHERE name = 'Administrator') WHERE email = 'webmaster@food4bees.org';
ALTER TABLE "user" ALTER COLUMN group_id SET NOT NULL;

ALTER TABLE "plant" ADD version INTEGER NOT NULL DEFAULT 0;
