package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.Flashcard;
import bot.telegram.flashcards.repository.FlashcardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EducationService {
    private final FlashcardsRepository flashcardsRepository;

    @Autowired
    public EducationService(FlashcardsRepository flashcardsRepository) {
        this.flashcardsRepository = flashcardsRepository;
    }
}
