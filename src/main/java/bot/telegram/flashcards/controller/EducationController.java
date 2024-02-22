package bot.telegram.flashcards.controller;

import bot.telegram.flashcards.models.Flashcard;
import bot.telegram.flashcards.service.EducationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.MemberStatus;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class EducationController {

    private final EducationService educationService;

    public List<SendMessage> startEducationCommandReceived(Update update) {
        List<Flashcard> flashcards = educationService.getFlashcard(update.getMessage().getChatId());

        List<SendMessage> messages = new ArrayList<>();

        for(Flashcard flashcard : flashcards){
            String messageText ="Question:\n " +  flashcard.getQuestion();
            SendMessage message = new SendMessage();

            if(flashcard.getAnswer() != null){
                messageText += "\nAnswer:\n " + flashcard.getAnswer();
            } else{
                messageText = "Question:\n " +  flashcard.getQuestion();
            }

            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(messageText);
            messages.add(message);
        }
        return messages;
    }

    public List<SendMessage> getYesCommandButton(CallbackQuery callbackQuery) {
        return List.of(educationService.createYesButton(callbackQuery.getMessage().getChatId()));
    }

    public List<SendMessage> getNoCommandButton(CallbackQuery callbackQuery) {
        return List.of(educationService.createNoButton(callbackQuery.getMessage().getChatId()));
    }

}

