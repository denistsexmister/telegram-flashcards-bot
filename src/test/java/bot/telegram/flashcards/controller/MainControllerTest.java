package bot.telegram.flashcards.controller;

import bot.telegram.flashcards.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class MainControllerTest {

    private static BotConfig config;

    private static SendMessage message;

    @BeforeAll
    static void setUp() {
        config = new BotConfig();
        config.setName("Flashcards");
        config.setToken("12345");

        System.out.println("Setup config was called");
    }

    @AfterAll
    static void tearDown() {
        config = null;
        System.out.println("Teardown config was called");
    }

    @Test
    @DisplayName("It should return bot name")
    void it_should_return_bot_name() {
        assertThat(config.getName()).isEqualTo("Flashcards");
    }

}