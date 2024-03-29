package bot.telegram.flashcards.models;

import bot.telegram.flashcards.models.temporary.FlashcardEducationList;
import bot.telegram.flashcards.models.temporary.FlashcardRepetitionList;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "account")
@Data
public class User {
    @Id
    @Column
    private long id;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<FlashcardPackage> flashcardPackageList;

    @OneToMany(mappedBy = "flashcardEducationListPK.user")
    private List<FlashcardEducationList> flashcardEducationList;

    @OneToMany(mappedBy = "flashcardRepetitionListPK.user")
    private List<FlashcardRepetitionList> flashcardRepetitionList;

    @Column
    private Long currentFlashcard;

    @Builder
    private static User createUserWithId(long id) {
        User user = new User();
        user.setId(id);
        return user;
    }
}
