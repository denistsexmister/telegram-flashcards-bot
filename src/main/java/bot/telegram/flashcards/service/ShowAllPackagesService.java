package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.FlashcardPackage;
import bot.telegram.flashcards.repository.FlashcardPackageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ShowAllPackagesService {

    private FlashcardPackageRepository flashcardPackageRepository;

    public List<FlashcardPackage> getListOfPackages(Long chatId) throws NoSuchElementException {
        return (List<FlashcardPackage>) flashcardPackageRepository.findAll();
    }

    public SendMessage getAllPackages(Long chatId) {
        List<FlashcardPackage> flashcardPackageList = getListOfPackages(chatId);

        return SendMessage.builder()
                .chatId(chatId)
                .text("Choose package:")
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboard(flashcardPackageList
                        .stream()
                        .map(flashcardPackage -> List.of(InlineKeyboardButton.builder()
//                                TODO: make like startEducationCommandReceived(EducationController)
                                .callbackData("FLASHCARD_PACKAGE_%d_EDITED".formatted(flashcardPackage.getId()))
                                .text(flashcardPackage.getTitle()).build())).collect(Collectors.toList())).build())
                                .build();
    }


    public EditMessageText getPackageDescription(long flashcardPackageId,int messageId, long chatId) {
        FlashcardPackage flashcardPackage = flashcardPackageRepository.findById(flashcardPackageId).orElseThrow();
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(flashcardPackage.getTitle())
                .replyMarkup(new InlineKeyboardMarkup(List.of(List.of(
                        InlineKeyboardButton.builder()
                                .callbackData("SHOW_ALL_PACKAGES")
                                .text("Show all packages")
                                .build()))))
                .build();


    }
}
