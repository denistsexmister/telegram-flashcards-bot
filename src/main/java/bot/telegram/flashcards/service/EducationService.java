package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.Flashcard;
import bot.telegram.flashcards.models.FlashcardPackage;
import bot.telegram.flashcards.models.User;
import bot.telegram.flashcards.models.temporary.FlashcardEducationList;
import bot.telegram.flashcards.repository.FlashcardEducationListRepository;
import bot.telegram.flashcards.repository.FlashcardPackageRepository;
import bot.telegram.flashcards.repository.FlashcardRepository;
import bot.telegram.flashcards.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class EducationService {
    private final FlashcardRepository flashcardRepository;
    private final FlashcardPackageRepository flashcardPackageRepository;
    private final UserRepository userRepository;
    private final FlashcardEducationListRepository flashcardEducationListRepository;

    public Flashcard getFlashcard(Long id) {
        return flashcardRepository.findFlashcardById(id);
    }

    public List<SendMessage> educationMessage(long chatId, String userFirstName) {
        SendMessage educMessage = SendMessage.builder()
                .chatId(chatId)
                .text("Hello, " + userFirstName + ". If you know answer on question what you see on screen, " +
                        "choose button \"Yes\" under message. Otherwise, choose \"No\".")
                .build();

        SendMessage yesButton = createYesButton(chatId);
        SendMessage noButton = createNoButton(chatId);

        return List.of(educMessage, yesButton, noButton);
    }

    public SendMessage createNoButton(long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(new InlineKeyboardMarkup(List.of
                        (List.of(InlineKeyboardButton.builder()
                                .text("No")
                                .callbackData("NO_BUTTON")
                                .build()))))
                .build();
    }

    public SendMessage createYesButton(long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(new InlineKeyboardMarkup(List.of
                        (List.of(InlineKeyboardButton.builder()
                                .text("Yes")
                                .callbackData("YES_BUTTON")
                                .text("No")
                                .callbackData("NO_BUTTON")
                                .build()))))
                .build();
    }


    public FlashcardPackage getFlashcardPackage(long packageId) throws NoSuchElementException {
        return flashcardPackageRepository.findById(packageId).orElseThrow();
    }

    public List<FlashcardPackage> getFlashcardPackageListByUser(long chatId) throws NoSuchElementException {
        User user = userRepository.findById(chatId).orElseThrow();

        return user.getFlashcardPackageList();
    }

    public EditMessageText generateFlashcardList(long flashcardPackageId, long chatId, int messageId) {
        FlashcardPackage flashcardPackage = flashcardPackageRepository.findById(flashcardPackageId).orElseThrow();
        List<Flashcard> flashcardList = flashcardPackage.getFlashcardList();
        User user = userRepository.findById(chatId).orElseThrow();

        Collections.shuffle(flashcardList);

        List<FlashcardEducationList> flashcardEducationList = new ArrayList<>();
        for (int i = 0; i < flashcardList.size(); i++) {
            flashcardEducationList.add(
                    new FlashcardEducationList(
                            new FlashcardEducationList.FlashcardEducationListPK(i + 1, user),
                            flashcardList.get(i)
                    )
            );
        }

        flashcardEducationListRepository.saveAll(flashcardEducationList);
        user.setCurrentFlashcard(1L);
        userRepository.save(user);

        EditMessageText editMessage = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("Flashcard 1/" + flashcardList.size() + "\n\nQuestion:\n" + flashcardList.get(0).getQuestion())
                .replyMarkup(new InlineKeyboardMarkup(List.of(List.of(
                        InlineKeyboardButton.builder()
                                .callbackData("SHOW_ANSWER_CLICKED").text("Show answer").build()))))
                .build();
        return editMessage;
    }

    public EditMessageText changeMsgToMsgWithShownAnswer(long chatId, int messageId) {
        User user = userRepository.findById(chatId).orElseThrow();
        long numberOfFlashcards = flashcardEducationListRepository.
                countFlashcardEducationListByFlashcardEducationListPK_User(user);
        Flashcard currentFlashcard = flashcardEducationListRepository.findById(
                new FlashcardEducationList.FlashcardEducationListPK(user.getCurrentFlashcard(), user)).orElseThrow()
                .getFlashcard();

        EditMessageText messageWithShownAnswer = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(List.of(InlineKeyboardButton.builder().text("Idk").callbackData("0%_BUTTON_CLICKED").build(),
                                             InlineKeyboardButton.builder().text("25%").callbackData("25%_BUTTON_CLICKED").build(),
                                             InlineKeyboardButton.builder().text("50%").callbackData("50%_BUTTON_CLICKED").build(),
                                             InlineKeyboardButton.builder().text("75%").callbackData("75%_BUTTON_CLICKED").build(),
                                             InlineKeyboardButton.builder().text("Easy").callbackData("100%_BUTTON_CLICKED").build()))
                        .build())
                .text("Flashcard " + currentFlashcard.getId() + "/" + numberOfFlashcards +
                        "\n\nQuestion:\n" + currentFlashcard.getQuestion() +
                        "\n\nAnswer:\n" + currentFlashcard.getAnswer())
                .build();

        return messageWithShownAnswer;
    }

    public EditMessageText nextFlashcard(long chatId, int messageId) {
        User user = userRepository.findById(chatId).orElseThrow();

        user.setCurrentFlashcard(user.getCurrentFlashcard() + 1);
        userRepository.save(user);

        long numberOfFlashcards = flashcardEducationListRepository.
                countFlashcardEducationListByFlashcardEducationListPK_User(user);
        Flashcard currentFlashcard = flashcardEducationListRepository.findById(
                        new FlashcardEducationList.FlashcardEducationListPK(user.getCurrentFlashcard(), user)).orElseThrow()
                .getFlashcard();

        EditMessageText editMessage = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("Flashcard " +currentFlashcard.getId() + "/" + numberOfFlashcards +
                        "\n\nQuestion:\n" + currentFlashcard.getQuestion())
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .callbackData("SHOW_ANSWER_CLICKED").text("Show answer").build())).build())
                .build();

        return editMessage;
    }

    public void moveFlashcardToRepetitionList() {

    }
}

