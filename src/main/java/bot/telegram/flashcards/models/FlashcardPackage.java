package bot.telegram.flashcards.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
public class FlashcardPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @OneToMany(mappedBy = "flashcardPackage")
    private List<Flashcard> flashcardList;

    @OneToMany(mappedBy = "flashcardPackage")
    private List<FlashcardsStatistics> flashcardsStatisticsList;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
