set names 'utf8';
DROP DATABASE IF EXISTS vsol;
CREATE DATABASE vsol DEFAULT CHARACTER SET 'utf8';
USE vsol;
CREATE TABLE IF NOT EXISTS teams (
	id serial,
	name tinytext,
	cash int not null default 0,
	primary key (id)
) engine=InnoDB;
CREATE TABLE IF NOT EXISTS events (
	id serial,
	season tinyint unsigned not null,
	day smallint unsigned not null,
	isDuringGeneration bit(1) not null,
	description tinytext,
	primary key (season, day, isDuringGeneration)
) engine=InnoDB;
CREATE TABLE IF NOT EXISTS actions (
	id serial,
	profit int not null default 0,
	description tinytext,
	eventId bigint unsigned not null,
	primary key (id),
	foreign key (eventId) references events(id)
) engine=InnoDB;