package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.User;
import bot.telegram.flashcards.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Service
@AllArgsConstructor
public class StartService {
    private final UserRepository userRepository;

    public boolean addUserIfNotInRepo(long chatId) {
        boolean didUserExistInRepo = userRepository.existsById(chatId);
        if (!didUserExistInRepo) {
            userRepository.save(User.builder().id(chatId).build());
        }

        return didUserExistInRepo;
    }

    public List<SendMessage> createWelcomeAndGuideMessages(long chatId, String userFirstName) {
        SendMessage welcomeMessage = SendMessage.builder()
                .chatId(chatId)
                .text("Hi, " + userFirstName + "! This bot allows you to create and learn flashcards.")
                .build();

        SendMessage guideMessage = createGuideMessage(chatId);
        return List.of(welcomeMessage, guideMessage);
    }

    public List<SendMessage> createWelcomeMessageWithGetGuideButton(long chatId, String userFirstName) {
        SendMessage welcomeMessage = SendMessage.builder()
                .chatId(chatId)
                .text("Hi, " + userFirstName + "! This bot allows you to create and learn flashcards. You can learn basics by clicking \"get guide\" button below.")
                .replyMarkup(new InlineKeyboardMarkup(List.of
                        (List.of(InlineKeyboardButton.builder()
                                .text("Get Guide")
                                .callbackData("GET_GUIDE_BUTTON_CLICKED")
                                .build()))))
                .build();

        return List.of(welcomeMessage);
    }

    public SendMessage createGuideMessage(long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("GUIDE_MESSAGE")
                .build();
    }
}
