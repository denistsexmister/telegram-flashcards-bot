package bot.telegram.flashcards.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "flashcards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Flashcards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @OneToMany(mappedBy = "flashcards")
    private List<Flashcard> flashcardList;

    @OneToMany(mappedBy = "flashcards")
    private List<FlashcardsStatistics> flashcardsStatisticsList;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
