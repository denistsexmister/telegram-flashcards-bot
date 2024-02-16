package bot.telegram.flashcards.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "flashcard")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Flashcard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "package_id", referencedColumnName = "id")
    private Flashcards flashcards;

    @Column(name = "question")
    private String question;

    @Column(name = "answer")
    private String answer;
}