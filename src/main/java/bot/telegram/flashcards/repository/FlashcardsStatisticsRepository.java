package bot.telegram.flashcards.repository;

import bot.telegram.flashcards.models.Flashcards;
import bot.telegram.flashcards.models.FlashcardsStatistics;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FlashcardsStatisticsRepository extends CrudRepository<FlashcardsStatistics, FlashcardsStatistics.FlashcardsStatisticsPK> {
    List<FlashcardsStatistics> findFlashcardsStatisticsByFlashcards(Flashcards flashcards);
}
