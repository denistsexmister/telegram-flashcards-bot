package bot.telegram.flashcards.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "flashcardPackage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlashcardPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @OneToMany(mappedBy = "flashcardPackage")
    private List<Flashcard> flashcardList;

    @OneToMany(mappedBy = "flashcardPackage")
    private List<FlashcardsStatistics> flashcardsStatisticsList;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
