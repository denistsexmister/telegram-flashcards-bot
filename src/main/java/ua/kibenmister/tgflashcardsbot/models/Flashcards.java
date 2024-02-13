package ua.kibenmister.tgflashcardsbot.models;

import jakarta.persistence.*;

@Entity
@Table(name = "flashcards")
public class Flashcards {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;
    private String word;
}
