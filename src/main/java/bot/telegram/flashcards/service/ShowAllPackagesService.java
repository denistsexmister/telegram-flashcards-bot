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

//create the rows with createRowWithOneButton
        List<InlineKeyboardButton> firstRow = createRowWithOneButton(flashcardPackageId,
                "FLASHCARD_PACKAGE_%d_SELECTED",
                "Start education");

        List<InlineKeyboardButton> secondRow = createRowWithOneButton(flashcardPackageId,
                "SHOW_ALL_CARDS_OF_PACKAGE_%d_SELECTED",
                "Show all cards");

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
    }

    public List<InlineKeyboardButton> createRowWithOneButton(long flashcardPackageId,String callbackData, String text){
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(InlineKeyboardButton.builder()
                .callbackData(callbackData.formatted(flashcardPackageId))
                .text(text)
                .build());

        return row;
    }
}
