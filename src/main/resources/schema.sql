DROP ALL OBJECTS;

CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    email varchar(60) UNIQUE NOT NULL,
    login varchar(40) NOT NULL,
    name varchar(40) NOT NULL,
    birthday DATE NOT NULL
    );

CREATE TABLE IF NOT EXISTS friendship (
    user_id INTEGER,
    friend_id INTEGER,
    status BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES users (user_id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS mpa(
    mpa_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name varchar(100) UNIQUE NOT NULL
    );

CREATE TABLE IF NOT EXISTS film (
    film_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name varchar(100) NOT NULL,
    description varchar(200),
    release_date DATE,
    duration INTEGER,
    mpa_id INTEGER NOT NULL,
    FOREIGN KEY (mpa_id) REFERENCES mpa (mpa_id) ON DELETE RESTRICT
    );

CREATE TABLE IF NOT EXISTS genre (
    genre_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name varchar(40) UNIQUE NOT NULL
    );

CREATE TABLE IF NOT EXISTS film_genre (
    film_id INTEGER NOT NULL,
    genre_id INTEGER NOT NULL,
    FOREIGN KEY (film_id) REFERENCES film (film_id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genre (genre_id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS likes (
    film_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    FOREIGN KEY (film_id) REFERENCES film (film_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
    );