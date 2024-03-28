package bot.telegram.flashcards.service;

import bot.telegram.flashcards.controller.EducationController;
import bot.telegram.flashcards.models.FlashcardPackage;
import bot.telegram.flashcards.repository.FlashcardPackageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ShowAllPackagesService {

    private FlashcardPackageRepository flashcardPackageRepository;
    private EducationController educationController;

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
                                .callbackData("SHOW_ALL_PACKAGES_%d_SELECTED".formatted(flashcardPackage.getId()))
                                .text(flashcardPackage.getTitle()).build())).collect(Collectors.toList())).build())
                                .build();
    }


    public EditMessageText getPackage(long flashcardPackageId,int messageId, long chatId) {
        FlashcardPackage flashcardPackage = flashcardPackageRepository.findById(flashcardPackageId).orElseThrow();

//         Create the first row of buttons
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        firstRow.add(InlineKeyboardButton.builder()
                .callbackData("FLASHCARD_PACKAGE_%d_SELECTED".formatted(flashcardPackageId))
                .text("Start education")
                .build());

//
// Create the second row of buttons
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        secondRow.add(InlineKeyboardButton.builder()
                .callbackData("SHOW_ALL_CARDS_OF_PACKAGE_%d_SELECTED".formatted(flashcardPackageId))
                .text("Show all cards")
                .build());
//
// Create the InlineKeyboardMarkup with the rows of buttons
        InlineKeyboardMarkup replyMarkup = InlineKeyboardMarkup.builder()
                .keyboard(List.of(firstRow, secondRow))
                .build();
//
// Return the EditMessageText with the replyMarkup
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(String.format("%s                         Number of cards: %d\n\n%s",
                        flashcardPackage.getTitle(),
                        flashcardPackage.getFlashcardList().size(),
                        flashcardPackage.getDescription()))
                .replyMarkup(replyMarkup)
                .build();

//        return EditMessageText.builder()
//                .chatId(chatId)
//                .messageId(messageId)
//                .text(String.format("%s              Number of cards: %d\n\n%s",
//                        flashcardPackage.getTitle(),
//                        flashcardPackage.getFlashcardList().size(),
//                        flashcardPackage.getDescription()))
//                .replyMarkup(new InlineKeyboardMarkup(List.of(List.of(
//                        InlineKeyboardButton.builder()
//                                .callbackData("SHOW_ALL_PACKAGES")
//                                .text("Show all packages")
//                                .build()))))
//                .build();
    }
}
