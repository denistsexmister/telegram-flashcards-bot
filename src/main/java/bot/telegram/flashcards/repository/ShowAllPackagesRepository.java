package bot.telegram.flashcards.repository;

import bot.telegram.flashcards.models.FlashcardPackage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ShowAllPackagesRepository
        extends CrudRepository<FlashcardPackage, Long> {
}
