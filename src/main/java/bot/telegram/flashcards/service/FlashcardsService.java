package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.Flashcard;
import bot.telegram.flashcards.models.FlashcardPackage;
import bot.telegram.flashcards.repository.FlashcardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FlashcardsService {
    private final FlashcardRepository flashcardsRepository;
//    make method getFlashcardIdsByFlashcardsId for find from FlashcardsRepository Flashcard's ID

    public List<Long> getFlashcardIdsByFlashcardsId(Long flashcardsId) {
        //TODO: this method show NullPointerException if flashcardsId == null, even if we have flashcardsId in test
        FlashcardPackage flashcards = flashcardsRepository.findById(flashcardsId).orElse(null).getFlashcardPackage();
        List<Long> flashcardIds = new ArrayList<>();

        if(flashcards != null){
            List<Flashcard> flashcardList = flashcards.getFlashcardList();
            for(Flashcard flashcard : flashcardList)
                flashcardIds.add(flashcard.getId());
        }
        return flashcardIds;
    }
}
