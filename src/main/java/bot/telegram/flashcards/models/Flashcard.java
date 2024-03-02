package bot.telegram.flashcards.models;

import bot.telegram.flashcards.models.temporary.FlashcardEducationList;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flashcard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @ManyToOne
    @JoinColumn(name = "packageId", referencedColumnName = "id")
    private FlashcardPackage flashcardPackage;

    @OneToOne(mappedBy = "flashcard")
    private FlashcardEducationList flashcardEducationList;

    @Column
    private String question;

    @Column
    private String answer;
}
