package bot.telegram.flashcards.models;

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

    @Builder
    private static User createUserWithId(long id) {
        User user = new User();
        user.setId(id);
        return user;
    }
}
