package bot.telegram.flashcards.service;

import bot.telegram.flashcards.repository.FlashcardsStatisticsRepository;
import bot.telegram.flashcards.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @Mock
    private FlashcardsStatisticsRepository statisticsRepository;
    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private StatisticsService statisticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        statisticsService = new StatisticsService(statisticsRepository, userRepository);
        System.out.println("Setup statistics service was called");
    }

    @AfterEach
    void tearDown() {
        statisticsService = null;
        System.out.println("Teardown statistics service was called");
    }

    @Test
    void should_create_month_statistics_by_all_decks_message() {
    }
}
