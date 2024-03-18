package bot.telegram.flashcards.controller;

import bot.telegram.flashcards.config.BotConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class MainControllerTest {

    private static BotConfig config;


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