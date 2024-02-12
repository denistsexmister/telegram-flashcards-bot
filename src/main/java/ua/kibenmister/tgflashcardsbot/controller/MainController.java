package ua.kibenmister.tgflashcardsbot.controller;


import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ua.kibenmister.tgflashcardsbot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Slf4j
@Component
public class MainController extends TelegramLongPollingBot {
    final BotConfig config;

    @Autowired
    public MainController(BotConfig config) {
        super(EmojiParser.parseToUnicode(config.getToken()));
        this.config = config;

        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get a welcome message"));
        listOfCommands.add(new BotCommand("/starteducation", "start learning cards"));
        listOfCommands.add(new BotCommand("/showallcards", "show all cards to learn"));
        listOfCommands.add(new BotCommand("/statistics", "show learning statistics"));
        listOfCommands.add(new BotCommand("/help", "show commands info and usages"));


        try {
            execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message msg = update.getMessage();

            SendMessage sm = SendMessage.builder()
                    .chatId(msg.getChatId())
                    .text(msg.getText())
                    .build();
            try {
                execute(sm);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
