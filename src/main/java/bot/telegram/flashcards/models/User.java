package bot.telegram.flashcards.models;

import bot.telegram.flashcards.models.temporary.FlashcardEducationList;
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

    @Builder
    private static User createUserWithId(long id) {
        User user = new User();
        user.setId(id);
        return user;
    }
}
