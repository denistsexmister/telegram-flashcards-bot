package bot.telegram.flashcards.repository;

import bot.telegram.flashcards.models.temporary.FlashcardEducationList;
import org.springframework.data.repository.CrudRepository;

public interface FlashcardEducationListRepository
        extends CrudRepository<FlashcardEducationList, FlashcardEducationList.FlashcardEducationListPK> {
}
