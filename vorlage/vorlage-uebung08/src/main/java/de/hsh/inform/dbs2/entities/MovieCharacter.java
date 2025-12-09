package de.hsh.inform.dbs2.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "UE08_MOVIE_CHARACTER")
public class MovieCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "character_name")
    private String character;
    
    private String alias;
    private int position;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

    public MovieCharacter() {}

    public MovieCharacter(String character, String alias, int position, Movie movie, Person person) {
        this.character = character;
        this.alias = alias;
        this.position = position;
        this.movie = movie;
        this.person = person;
    }

    public Long getId() { return id; }
    public String getCharacter() { return character; }
    public void setCharacter(String character) { this.character = character; }
    public String getAlias() { return alias; }
    public void setAlias(String alias) { this.alias = alias; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    
    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }
    
    public Person getPerson() { return person; }
    public void setPerson(Person person) { this.person = person; }
}