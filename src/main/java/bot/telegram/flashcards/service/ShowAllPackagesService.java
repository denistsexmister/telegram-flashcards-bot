package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.Flashcard;
import bot.telegram.flashcards.models.FlashcardPackage;
import bot.telegram.flashcards.repository.FlashcardPackageRepository;
import bot.telegram.flashcards.repository.FlashcardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ShowAllPackagesService {

    private FlashcardPackageRepository flashcardPackageRepository;
    private FlashcardRepository flashcardRepository;


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


    public EditMessageText showPackage(long packageId,int messageId, long chatId) {
        FlashcardPackage flashcardPackage = flashcardPackageRepository.findById(packageId).orElseThrow();

//create the rows with createRowWithOneButton
        List<InlineKeyboardButton> firstRow = createRowWithOneButton(packageId,
                "FLASHCARD_PACKAGE_%d_SELECTED",
                "Start education");
//todo: make work packageId with flashcardId
        List<InlineKeyboardButton> secondRow = createRowWithOneButton(packageId,
                "FIRST_CARD_%d_OF_PACKAGE_CLICKED",
                "Show first card of package");

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


    public List<Flashcard> getAllCardsOfPackage(long packageId) {
        FlashcardPackage flashcardPackage = flashcardPackageRepository.findById(packageId).orElseThrow();

        return new ArrayList<>(flashcardPackage.getFlashcardList());
    }




    public EditMessageText getPreviousOrNextCard(long packageId, int messageId, long chatId) {


        List<Flashcard> allCards = getAllCardsOfPackage(packageId);

        if (allCards.isEmpty()) {
            // Handle case where no cards are found
            return null; // or throw an exception, return a default message, etc.
        }

        int currentCard = 0;

        Flashcard flashcard = allCards.get(currentCard);

        int currentCardNumber = allCards.indexOf(flashcard) + 1;


        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(String.format("Card: %d \n\nQuestion:\n%s\n\nAnswer:\n%s",
                        currentCardNumber,
                        flashcard.getQuestion(),
                        flashcard.getAnswer()))
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .text("Next")
                                .callbackData(String.format("NEXT_CARD_%d_%d".formatted(packageId,++currentCard))) // Increment currentCard here
                                .build())).build())
                .build();


//        int currentCard = 0;
//
//        Flashcard flashcard = flashcardRepository.findById(getAllCardsOfPackage(packageId).get(currentCard).getId()).orElseThrow();
//
//        int currentCardNumber = getAllCardsOfPackage(packageId).indexOf(flashcard) + 1;
//
//            return EditMessageText.builder()
//                    .chatId(chatId)
//                    .messageId(messageId)
//                    .text(String.format("Card: %d \n\nQuestion:\n%s\n\nAnswer:\n%s",
//                            ++currentCardNumber,
//                            flashcard.getQuestion(),
//                            flashcard.getAnswer()))
//                    .replyMarkup(InlineKeyboardMarkup.builder()
//                            .keyboardRow(List.of(InlineKeyboardButton.builder()
//                                    .text("Next")
//                                    .callbackData(String.format("NEXT_CARD",(++currentCard)))
//                                    .build())).build())
//                    .build();
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
