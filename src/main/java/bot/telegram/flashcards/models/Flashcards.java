package bot.telegram.flashcards.models;

import jakarta.persistence.*;

@Entity
@Table(name = "flashcards")
public class Flashcards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "question")
    private String question;

    @Column(name = "answer")
    private String answer;
}
