package bot.telegram.flashcards.service;

import bot.telegram.flashcards.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StartServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private StartService start;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        start = new StartService(userRepository);
        System.out.println("Setup start service was called");
    }

    @AfterEach
    void tearDown() {
        start = null;
        System.out.println("Teardown start service was called");
    }

    @Test
    @DisplayName("It should add user if is in repo")
    void should_add_user_if_is_in_repo() {
        long chatId = 1L;

        when(userRepository.existsById(chatId)).thenReturn(false);

        // Act
        boolean result = start.addUserIfNotInRepo(chatId);

        // Assert
        assertThat(result).isFalse();
        verify(userRepository, times(1)).save(any());
    }


    @Test
    @DisplayName("It should create welcome and guide messages")
    void should_create_welcome_and_guide_messages() {
        long chatId = 1L;
        String userFirst = "Test";
        assertThat(start.createWelcomeAndGuideMessages(chatId, userFirst)).isNotNull();
    }

    @Test
    @DisplayName("It should create welcome message with get guide button")
    void should_create_welcome_message_with_get_guide_button() {
        long chatId = 1L;
        String userFirst = "Test";
        assertThat(start.createWelcomeMessageWithGetGuideButton(chatId, userFirst)).isNotNull();
    }

    @Test
    @DisplayName("It should create guide message")
    void should_create_guide_message() {
        long chatId = 1L;

        SendMessage result = start.createGuideMessage(chatId);
        assertThat(result).isNotNull();
    }
}