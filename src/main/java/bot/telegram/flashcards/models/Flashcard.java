package bot.telegram.flashcards.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class Flashcard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @ManyToOne
    @JoinColumn(name = "packageId", referencedColumnName = "id")
    private FlashcardPackage flashcardPackage;

    @Column
    private String question;

    @Column
    private String answer;
}
