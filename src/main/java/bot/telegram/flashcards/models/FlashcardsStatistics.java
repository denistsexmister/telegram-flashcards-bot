package bot.telegram.flashcards.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
public class FlashcardsStatistics {
    @EmbeddedId
    private FlashcardsStatisticsPK id;

    @Column
    @ManyToOne
    @JoinColumn(name = "flashcards_id")
    private Flashcards flashcards;

    @Column
    private long hardCards;

    @Column
    private long failedCards;

    @Column
    private long studiedCards;

    @Embeddable
    public static class FlashcardsStatisticsPK implements Serializable {
        @Column
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @Column
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private LocalDate date;
    }

}
