package de.hsh.inform.dbs2.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "UE08_PERSON")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;


    @OneToMany(mappedBy = "person")
    private List<MovieCharacter> playedCharacters = new ArrayList<>();

    public Person() {}

    public Person(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<MovieCharacter> getPlayedCharacters() { return playedCharacters; }
}