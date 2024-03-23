package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.FlashcardPackage;
import bot.telegram.flashcards.repository.ShowAllPackagesRepository;
import bot.telegram.flashcards.repository.FlashcardPackageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ShowAllPackagesService {

    private ShowAllPackagesRepository showAllPackagesRepository;

    public List<FlashcardPackage> getListOfPackages(Long chatId) throws NoSuchElementException {
        return (List<FlashcardPackage>) showAllPackagesRepository.findAll();
    }

    public EditMessageText getAllPackages(Long chatId, int messageId) {
        List<FlashcardPackage> flashcardPackageList = getListOfPackages(chatId);

        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("Choose package:")
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboard(flashcardPackageList
                        .stream()
                        .map(flashcardPackage -> List.of(InlineKeyboardButton.builder()
                                .callbackData(String.valueOf(flashcardPackage.getId()))
                                .text(flashcardPackage.getTitle()).build())).collect(Collectors.toList())).build())
                                .build();
    }

}
