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

    public SendMessage showAllPackagesCommandReceived(Update update){
        long chatId = update.getCallbackQuery().getMessage().getChatId();

        return showAllPackagesService.getAllPackages(chatId);
    }

//    TODO: make method, which will show description of chosen package, example startEducation(EducationController)
// Check git commit --amend


}