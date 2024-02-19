package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.Flashcard;
import bot.telegram.flashcards.models.Flashcards;
import bot.telegram.flashcards.repository.FlashcardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EducationService {
    private final FlashcardRepository flashcardRepository;

    public List<Flashcard> getFlashcard(Long id) {
        return flashcardRepository.findFlashcardById(id);

    }
}

