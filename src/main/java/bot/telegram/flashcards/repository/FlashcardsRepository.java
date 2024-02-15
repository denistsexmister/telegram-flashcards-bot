package bot.telegram.flashcards.repository;

import bot.telegram.flashcards.models.Flashcard;
import bot.telegram.flashcards.models.Flashcards;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface FlashcardsRepository extends CrudRepository<Flashcard, Long> {
    List<Flashcard> findByPackageId(Flashcards flashcards);// <>
}
