package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.Flashcard;
import bot.telegram.flashcards.models.Flashcards;
import bot.telegram.flashcards.repository.FlashcardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Service
@AllArgsConstructor
public class EducationService {
    private final FlashcardRepository flashcardRepository;

    public List<Flashcard> getFlashcard(Long id) {
        return flashcardRepository.findFlashcardById(id);
    }

    public List<SendMessage> educationMessage(long chatId, String userFirstName) {
        SendMessage educMessage = SendMessage.builder()
                .chatId(chatId)
                .text("Hello, " + userFirstName + ". If you know answer on question what you see on screen, " +
                        "choose button \"Yes\" under message. Otherwise, choose \"No\".")
                .build();

        SendMessage yesAndNoButton = createYesAndNoButton(chatId);

        return List.of(educMessage, yesAndNoButton);
    }

    private SendMessage createYesAndNoButton(long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .replyMarkup(new InlineKeyboardMarkup(List.of
                        (List.of(InlineKeyboardButton.builder()
                                .text("Yes")
                                .callbackData("yes")
                                .text("No")
                                .callbackData("no")
                                .build()))))
                .build();
    }
}

