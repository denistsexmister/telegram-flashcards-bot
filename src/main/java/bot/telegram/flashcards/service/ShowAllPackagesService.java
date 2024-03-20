package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.FlashcardPackage;
import bot.telegram.flashcards.repository.ShowAllPackagesRepository;
import bot.telegram.flashcards.repository.FlashcardPackageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ShowAllPackagesService {

    private ShowAllPackagesRepository showAllPackagesRepository;

    public List<FlashcardPackage> showAllPackages(Long chatId) throws NoSuchElementException {
        return (List<FlashcardPackage>) showAllPackagesRepository.findAll();
    }

}
