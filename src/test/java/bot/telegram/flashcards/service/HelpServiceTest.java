package bot.telegram.flashcards.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class HelpServiceTest {

    @InjectMocks
    private HelpService helpService;

    @BeforeEach
    void setUp() {
        helpService = new HelpService();
        System.out.println("Setup help service was called");
    }

    @AfterEach
    void tearDown() {
        helpService = null;
        System.out.println("Teardown help service was called");
    }
    @Test
    @DisplayName("It should create help message")
    void should_create_help_message() {
        assertThat(helpService.createHelpMessage(1L)).isNotNull();
        System.out.println("Help message was created");
    }

}