package bot.telegram.flashcards.controller;

import bot.telegram.flashcards.service.HelpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HelpControllerTest {

    @Mock
    private HelpService helpService;
    @InjectMocks
    private HelpController helpController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        helpController = new HelpController(helpService);
    }

    @Test
    void helpCommandReceived() {
        assertThat(helpController.helpCommandReceived(null)).isNull();
    }
}