package bot.telegram.flashcards.controller;

import bot.telegram.flashcards.service.EducationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class EducationControllerTest {

    @Mock
    private EducationService educationService;

    @InjectMocks
    private EducationController educationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        educationController = new EducationController(educationService);
    }

    @Test
    void startEducationCommandReceived() {

        assertNull(educationController.startEducationCommandReceived(null));
    }

    @Test
    void startEducation() {

        assertNull(educationController.startEducation(null));
    }

    @Test
    void showAnswer() {

        assertNull(educationController.showAnswer(null));
    }

    @Test
    void answerButtonClicked() {

        assertNull(educationController.answerButtonClicked(null, null));
    }

    @Test
    void nextQuestionRepetition() {

        assertNull(educationController.nextQuestionRepetition(null));
    }

    @Test
    void showAnswerRepetition() {

        assertNull(educationController.showAnswerRepetition(null));
    }
}