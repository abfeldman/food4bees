-- first close all connections from to food4bees database
-- both from running applications and database admin client

CREATE DATABASE food4bees2test WITH TEMPLATE food4bees OWNER postgres;

DELETE FROM plant_image;
DELETE FROM vegetation;
DELETE FROM plant;
DELETE FROM "user";

