package bot.telegram.flashcards.service;

import bot.telegram.flashcards.repository.FlashcardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FlashcardServiceTest {

    @Mock
    private FlashcardRepository flashcardRepository;
    @InjectMocks
    private FlashcardService flashcardService;


    @AfterEach
    void tearDown() {
        flashcardService = null;
        System.out.println("Teardown flashcards service was called");
    }

    @Test
    @DisplayName("It should get flashcard ids by flashcards id")
    void should_get_flashcard_ids_by_flashcards_id() {
        Assertions.assertThat(flashcardService.getFlashcardIdsByFlashcardsId(1L)).isNotNull();
    }
}