package bot.telegram.flashcards.controller;

import bot.telegram.flashcards.service.ShowAllPackagesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Controller
@AllArgsConstructor
public class ShowAllPackagesController {
    private ShowAllPackagesService showAllPackagesService;

    public SendMessage showAllPackagesCommandReceived(Update update) {
        String flashcardPackages = showAllPackagesService.showAllPackages(update.getMessage().getChatId()).toString();

        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId().toString());

        return message;
    }

}
