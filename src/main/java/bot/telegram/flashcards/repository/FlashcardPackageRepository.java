package bot.telegram.flashcards.repository;

import bot.telegram.flashcards.models.FlashcardPackage;
import org.springframework.data.repository.CrudRepository;

public interface FlashcardPackageRepository extends CrudRepository<FlashcardPackage, Long> {
}
