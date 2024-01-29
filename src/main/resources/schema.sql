DROP TABLE if exists films CASCADE;
DROP TABLE if exists users CASCADE;
DROP TABLE if exists rating CASCADE;
DROP TABLE if exists genre CASCADE;
DROP TABLE if exists film_likes CASCADE;
DROP TABLE if exists user_friends CASCADE;
DROP TABLE if exists film_genre CASCADE;

CREATE TABLE if not exists rating (
    id integer generated  by default as identity PRIMARY KEY,
    name varchar(255)
);

CREATE TABLE if not exists genre (
    id integer generated  by default as identity PRIMARY KEY,
    name varchar(255)
);

CREATE TABLE if not exists films (
    id integer generated  by default as identity PRIMARY KEY,
    name varchar(255),
    description varchar(1024),
    release_date date,
    duration integer,
    rating_id integer references rating(id) ON DELETE CASCADE
);

CREATE TABLE if not exists film_genre (
    film_id integer references films(id) on delete cascade,
    genre_id integer references genre(id) on delete cascade
);

CREATE TABLE if not exists users (
    id integer generated  by default as identity PRIMARY KEY,
    name varchar(100),
    email varchar(100),
    login varchar(100),
    birthday date
);

CREATE TABLE if not exists film_likes (
    film_id integer references films(id) on delete cascade,
    user_id integer references users(id) on delete cascade
);

CREATE TABLE if not exists user_friends (
    user_id integer references users(id) on delete cascade ,
    friends_id integer references users(id) on delete cascade
);

