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
        if(update.hasCallbackQuery() && update.getCallbackQuery().getData().equals("SHOW_ALL_PACKAGES")) {
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            return showAllPackagesService.getAllPackages(chatId);
        } else {
            long chatId = update.getMessage().getChatId();
            return showAllPackagesService.getAllPackages(chatId);
        }
    }

//    TODO: make method, which will show description of chosen package, example startEducation(EducationController)

    public EditMessageText showPackageDescription(CallbackQuery callbackQuery) {
        long flashcardPackageId = Long.parseLong(callbackQuery.getData().split("_")[2]);
        int messageId = ((Message) callbackQuery.getMessage()).getMessageId();
        long chatId = callbackQuery.getMessage().getChatId();

        return showAllPackagesService.getPackageDescription( flashcardPackageId ,messageId, chatId);
    }

}
