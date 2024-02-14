package bot.telegram.flashcards.controller;

import bot.telegram.flashcards.service.StartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
public class StartController {
    private final StartService startService;

    @Autowired
    public StartController(StartService startService) {
        this.startService = startService;
    }

    public List<SendMessage> startCommandReceived(Update update) {
        long chatId = update.getMessage().getChatId();
        String userFirstName = update.getMessage().getChat().getFirstName();

        if (!startService.addUserIfNotInRepo(update.getMessage().getChatId())) {
            return startService.createWelcomeAndGuideMessages(chatId, userFirstName);
        } else {
            return startService.createWelcomeMessageWithGetGuideButton(chatId, userFirstName);
        }
    }

    public List<SendMessage> getGuideButtonClicked(CallbackQuery callbackQuery) {
        return List.of(startService.createGuideMessage(((Message) callbackQuery.getMessage()).getChatId()));
    }
}
