package bot.telegram.flashcards.service;

import bot.telegram.flashcards.repository.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EducationServiceTest {
    @Mock
    private  FlashcardRepository flashcardRepository;
    @Mock
    private  FlashcardPackageRepository flashcardPackageRepository;
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  FlashcardEducationListRepository flashcardEducationListRepository;
    @Mock
    private  FlashcardRepetitionListRepository flashcardRepetitionListRepository;

    @InjectMocks
    private EducationService education;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        education = new EducationService(flashcardRepository, flashcardPackageRepository, userRepository, flashcardEducationListRepository, flashcardRepetitionListRepository);

        System.out.println("Setup education service was called");
    }

    @AfterEach
    void tearDown() {
        education = null;
        System.out.println("Teardown education service was called");
    }


}