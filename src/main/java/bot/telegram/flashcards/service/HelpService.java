package bot.telegram.flashcards.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class HelpService {
    public SendMessage createHelpMessage(long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text("HELP_MESSAGE")
                .build();
    }
}
