package bot.telegram.flashcards.controller;

import bot.telegram.flashcards.service.StartService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StartControllerTest {

    @Mock
    private StartService startService;

    @InjectMocks
    private StartController startController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startController = new StartController(startService);
    }

    @Test
    void startCommandReceived() {
        Assertions.assertThat(startController.startCommandReceived(null)).isNull();
    }

    @Test
    void getGuideButtonClicked() {
        Assertions.assertThat(startController.getGuideButtonClicked(null)).isNull();
    }
}