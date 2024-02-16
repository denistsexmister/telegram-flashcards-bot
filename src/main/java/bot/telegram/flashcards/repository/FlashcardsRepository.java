package bot.telegram.flashcards.repository;

import bot.telegram.flashcards.models.Flashcard;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FlashcardsRepository extends CrudRepository<Flashcard, Long> {
    //Add List<Flashcard> into FlashcardsRepository for find by Flashcards Flashcard ID
    List<Flashcard> findByFlashcardsId(Long flashcards);
}
