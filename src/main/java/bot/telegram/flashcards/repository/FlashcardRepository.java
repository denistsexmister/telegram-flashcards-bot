package bot.telegram.flashcards.repository;

import bot.telegram.flashcards.models.Flashcard;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlashcardRepository extends CrudRepository<Flashcard, Long> {
    List<Flashcard> findFlashcardById(Long id);

}
