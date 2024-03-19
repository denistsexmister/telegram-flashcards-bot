package bot.telegram.flashcards.service;

import bot.telegram.flashcards.repository.*;
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
    }


    @Test
    @DisplayName("It should get flashcard package")
    void should_get_flashcard_package() {
        Assertions.assertThat(education.getFlashcardPackage(1L)).isNotNull();
    }

    @Test
    @DisplayName("It should get flashcard package list by user")
    void should_get_flashcard_package_list_by_user() {
        Assertions.assertThat(education.getFlashcardPackageListByUser(1L)).isNotNull();
    }

    @Test
    @DisplayName("It should generate flashcard list")
    void should_generate_flashcard_list() {
        Assertions.assertThat(education.generateFlashcardList(1L, 1L, 1)).isNotNull();
    }

    @Test
    @DisplayName("It should change msg to msg with shown answer")
    void should_change_msg_to_msg_with_shown_answer(){
        Assertions.assertThat(education.changeMsgToMsgWithShownAnswer(1L, 1)).isNotNull();
    }

    @Test
    @DisplayName("It should create congratulation message")
    void should_create_congratulation_message(){
        Assertions.assertThat(education.createCongratulationMessage(1L, 1)).isNotNull();
    }




}