package bot.telegram.flashcards.controller;


import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import bot.telegram.flashcards.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.*;

@Slf4j
@Component
public class MainController extends TelegramLongPollingBot {
    final BotConfig config;
    private final StartController startController;

    @Autowired
    public MainController(BotConfig config, StartController startController) {
        super(config.getToken());
        this.config = config;
        this.startController = startController;

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
            String msgText = msg.getText();

            switch (msgText) {
                case "/start" -> startController.startCommandReceived(update)
                        .forEach(this::executeMessage);
                default -> defaultMessage(msg.getChatId());
            }
        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String callbackQueryData = callbackQuery.getData();

            switch (callbackQueryData) {
                case "GET_GUIDE_BUTTON_CLICKED" -> startController.getGuideButtonClicked(callbackQuery)
                        .forEach(this::executeMessage);
            }

        }
    }

    private <T extends Serializable, Method extends BotApiMethod<T>> void executeMessage(Method message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void defaultMessage(long chatId) {
        SendMessage commandNotFoundMessage = SendMessage.builder()
                .chatId(chatId)
                .text("Command was not recognized, please use commands from menu list!")
                .build();

        executeMessage(commandNotFoundMessage);
    }
}
