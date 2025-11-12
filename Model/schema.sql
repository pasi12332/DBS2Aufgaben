DROP TABLE IF EXISTS MovieCharacter; 
DROP TABLE IF EXISTS MovieGenre;
DROP TABLE IF EXISTS Movie;
DROP TABLE IF EXISTS Person;
DROP TABLE IF EXISTS Genre;

CREATE TABLE Genre (
    genreID SERIAL PRIMARY KEY,
    genre VARCHAR(255) NOT NULL
);

CREATE TABLE Person (
    personID SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE Movie (
    movieID SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    year INT NOT NULL,
    type VARCHAR(255) NOT NULL
);

CREATE TABLE MovieCharacter (
    movcharID SERIAL PRIMARY KEY,
    character VARCHAR(255) NOT NULL,
    alias VARCHAR(255),
    position INT,
    movieID INT NOT NULL,
    personID INT NOT NULL, 
    CONSTRAINT fk_movie_person_movie FOREIGN KEY (movieID) REFERENCES movie(movieID),
    CONSTRAINT fk_person_person_movie FOREIGN KEY (personID) REFERENCES person(personID)
);

-- Join-Tabelle für die N:M-Beziehung (Movie, Genre)
CREATE TABLE MovieGenre (
    movieID INT NOT NULL,
    genreID INT NOT NULL,
    PRIMARY KEY(movieID, genreID),
    CONSTRAINT fk_movie_genre_movie FOREIGN KEY (movieID) REFERENCES movie(movieID),
    CONSTRAINT fk_genre_genre_movie FOREIGN KEY (genreID) REFERENCES genre(genreID)
)