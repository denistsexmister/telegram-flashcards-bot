package bot.telegram.flashcards.controller;

import bot.telegram.flashcards.service.ShowAllPackagesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Controller
@AllArgsConstructor
public class ShowAllPackagesController {
    private ShowAllPackagesService showAllPackagesService;

    public SendMessage showAllPackagesCommandReceived(Update update){
        long chatId = update.getMessage().getChatId();
        return showAllPackagesService.getAllPackages(chatId);
    }
    public EditMessageText showPackageDescription(CallbackQuery callbackQuery) {
        long flashcardPackageId = Long.parseLong(callbackQuery.getData().split("_")[3]);
        int messageId = ((Message) callbackQuery.getMessage()).getMessageId();
        long chatId = callbackQuery.getMessage().getChatId();

        return showAllPackagesService.getPackage(flashcardPackageId ,messageId, chatId);
    }
}
