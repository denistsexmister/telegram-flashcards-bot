package bot.telegram.flashcards.repository;

import bot.telegram.flashcards.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
