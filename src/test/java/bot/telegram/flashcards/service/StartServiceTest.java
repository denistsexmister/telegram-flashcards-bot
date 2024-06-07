package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.User;
import bot.telegram.flashcards.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
class StartServiceTest {

    @InjectMocks
    private StartService startService;

    @Test
    void addUserIfNotInRepo() {
        boolean result = startService.addUserIfNotInRepo(1L);
        Assertions.assertThat(result).isFalse();
    }

    @Test
    void createWelcomeAndGuideMessages() {
        String sendMessage = startService.createWelcomeAndGuideMessages(1L, "test").toString();
        Assertions.assertThat(sendMessage).isNotNull();
    }

    @Test
    void createWelcomeMessageWithGetGuideButton() {
        String sendMessage = startService.createWelcomeMessageWithGetGuideButton(1L, "test").toString();
        Assertions.assertThat(sendMessage).isNotNull();
    }

    @Test
    void createGuideMessage() {
        String sendMessage = startService.createGuideMessage(1L).toString();
        Assertions.assertThat(sendMessage).isNotNull();
    }
}