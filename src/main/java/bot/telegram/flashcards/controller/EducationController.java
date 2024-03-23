package bot.telegram.flashcards.controller;

import bot.telegram.flashcards.misc.FlashcardAnswerStatus;
import bot.telegram.flashcards.models.FlashcardPackage;
import bot.telegram.flashcards.service.EducationService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class EducationController {

    private final EducationService educationService;

    private static final Logger log = LoggerFactory.getLogger(EducationController.class);

    public SendMessage startEducationCommandReceived(Update update) {
        try {
            long chatId = update.getMessage().getChatId();
            List<FlashcardPackage> flashcardPackageList =
                    educationService.getFlashcardPackageListByUser(chatId);

            SendMessage flashcardPackageChoiceMessage = new SendMessage();
            flashcardPackageChoiceMessage.setChatId(chatId);

            if (flashcardPackageList.isEmpty()) {
                flashcardPackageChoiceMessage.setText("There is no flashcard packages created yet, you can create one using /something command!");
            } else {
                List<List<InlineKeyboardButton>> buttonRowList = new ArrayList<>();
                flashcardPackageList.forEach((f) -> {
                    InlineKeyboardButton button = InlineKeyboardButton.builder()
                            .text(f.getTitle())
                            .callbackData("FLASHCARD_PACKAGE_%d_SELECTED".formatted(f.getId()))
                            .build();
                    buttonRowList.add(List.of(button));
                });
                flashcardPackageChoiceMessage.setText("Choose flashcard package you want to practise with:");
                flashcardPackageChoiceMessage.setReplyMarkup(new InlineKeyboardMarkup(buttonRowList));
            }

            return flashcardPackageChoiceMessage;
        }catch (Exception e){
            log.error("Error: cannot start education command received", e);
            return null;
        }
    }

    public EditMessageText startEducation(CallbackQuery callbackQuery) {
        long flashcardPackageId = Long.parseLong(callbackQuery.getData().split("_")[2]);
        int messageId = ((Message) callbackQuery.getMessage()).getMessageId();
        long chatId = callbackQuery.getMessage().getChatId();

        return educationService.generateFlashcardList(flashcardPackageId, chatId, messageId);
    }

    public EditMessageText showAnswer(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = ((Message) callbackQuery.getMessage()).getMessageId();

        return educationService.changeMsgToMsgWithShownAnswer(chatId, messageId);
    }

    public EditMessageText answerButtonClicked(CallbackQuery callbackQuery, FlashcardAnswerStatus answerStatus) {
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = ((Message) callbackQuery.getMessage()).getMessageId();

        switch (answerStatus) {
            case HARDEST:
                educationService.duplicateFlashcard(chatId, 2);
                break;
            case HARD:
                educationService.duplicateFlashcard(chatId, 1);
                break;
            case EASY:// can be changed to ->
                educationService.moveFlashcardToRepetitionList(chatId);
        }

        return educationService.nextFlashcard(chatId, messageId);
    }

    public EditMessageText nextQuestionRepetition(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = ((Message) callbackQuery.getMessage()).getMessageId();

        return educationService.nextRepetitionFlashcard(chatId, messageId);
    }

    public EditMessageText showAnswerRepetition(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.getMessage().getChatId();
        int messageId = ((Message) callbackQuery.getMessage()).getMessageId();

        return educationService.changeMsgToMsgWithShownAnswerRepetition(chatId, messageId);
    }
}

