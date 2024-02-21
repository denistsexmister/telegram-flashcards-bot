package bot.telegram.flashcards.controller;

import bot.telegram.flashcards.models.Flashcard;
import bot.telegram.flashcards.service.EducationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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

//            TODO: add if() for flashcard.getAnswer()

            message.setChatId(update.getMessage().getChatId().toString());
            message.setText(messageText);
            messages.add(message);
        }
        return messages;
    }

}

