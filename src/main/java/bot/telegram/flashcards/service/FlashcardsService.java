package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.Flashcard;
import bot.telegram.flashcards.models.Flashcards;
import bot.telegram.flashcards.repository.FlashcardsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FlashcardsService {
    private final FlashcardsRepository flashcardsRepository;
//    make method getFlashcardIdsByFlashcardsId for find from FlashcardsRepository Flashcard's ID

    public List<Long> getFlashcardIdsByFlashcardsId(Long flashcardsId) {
        Flashcards flashcards = flashcardsRepository.findById(flashcardsId).orElse(null).getFlashcards();
        List<Long> flashcardIds = new ArrayList<>();

        if(flashcards != null){
            List<Flashcard> flashcardList = flashcards.getFlashcardList();
            for(Flashcard flashcard : flashcardList)
                flashcardIds.add(flashcard.getId());
        }
        return flashcardIds;
    }
}
