package bot.telegram.flashcards.service;

import bot.telegram.flashcards.models.Flashcard;
import bot.telegram.flashcards.models.FlashcardPackage;
import bot.telegram.flashcards.models.User;
import bot.telegram.flashcards.models.temporary.FlashcardEducationList;
import bot.telegram.flashcards.models.temporary.FlashcardRepetitionList;
import bot.telegram.flashcards.models.temporary.FlashcardStatus;
import bot.telegram.flashcards.repository.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;

@Service
@AllArgsConstructor
public class EducationService {
    private final FlashcardRepository flashcardRepository;
    private final FlashcardPackageRepository flashcardPackageRepository;
    private final UserRepository userRepository;
    private final FlashcardEducationListRepository flashcardEducationListRepository;
    private final FlashcardRepetitionListRepository flashcardRepetitionListRepository;
    private final FlashcardStatusRepository flashcardStatusRepository;

    private static final Logger log = LoggerFactory.getLogger(EducationService.class);

    public FlashcardPackage getFlashcardPackage(long packageId) throws NoSuchElementException {
        return flashcardPackageRepository.findById(packageId).orElseThrow();
    }

    public List<FlashcardPackage> getFlashcardPackageListByUser(long chatId) throws NoSuchElementException {
        try {
            User user = userRepository.findById(chatId).orElseThrow();

            return user.getFlashcardPackageList();
        }catch (Exception e){
            log.error("Cannot get flashcard package list", e);
            return List.of();
        }
    }

    public EditMessageText generateFlashcardList(long flashcardPackageId, long chatId, int messageId) {
        try {
            FlashcardPackage flashcardPackage = flashcardPackageRepository.findById(flashcardPackageId).orElseThrow();
            List<Flashcard> flashcardList = flashcardPackage.getFlashcardList();
            User user = userRepository.findById(chatId).orElseThrow();

            Collections.shuffle(flashcardList);

            List<FlashcardEducationList> flashcardEducationList = new ArrayList<>();
            for (int i = 0; i < flashcardList.size(); i++) {
                flashcardEducationList.add(
                        new FlashcardEducationList(
                                new FlashcardEducationList.FlashcardEducationListPK(i + 1, user),
                                flashcardList.get(i)
                        )
                );
            }

            flashcardEducationListRepository.saveAll(flashcardEducationList);
            user.setCurrentFlashcard(1L);
            userRepository.save(user);

            EditMessageText editMessage = EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text("Flashcard 1/" + flashcardList.size() + "\n\nQuestion:\n" + flashcardList.get(0).getQuestion())
                    .replyMarkup(new InlineKeyboardMarkup(List.of(List.of(
                            InlineKeyboardButton.builder()
                                    .callbackData("SHOW_ANSWER_CLICKED").text("Show answer").build()))))
                    .build();
            return editMessage;
        }catch (Exception e){
            log.error("Cannot generate flashcard list", e);
            return null;
        }
    }

    public EditMessageText changeMsgToMsgWithShownAnswer(long chatId, int messageId) {
        User user = userRepository.findById(chatId).orElseThrow();
        long numberOfFlashcards = flashcardEducationListRepository.
                countFlashcardEducationListByFlashcardEducationListPK_User(user);
        FlashcardEducationList flashcardEducationList = flashcardEducationListRepository.findById(
                        new FlashcardEducationList.FlashcardEducationListPK(user.getCurrentFlashcard(), user)).orElseThrow();
        Flashcard currentFlashcard = flashcardEducationList.getFlashcard();

        EditMessageText messageWithShownAnswer = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(List.of(InlineKeyboardButton.builder().text("Idk").callbackData("0%_BUTTON_CLICKED").build(),
                                             InlineKeyboardButton.builder().text("25%").callbackData("25%_BUTTON_CLICKED").build(),
                                             InlineKeyboardButton.builder().text("50%").callbackData("50%_BUTTON_CLICKED").build(),
                                             InlineKeyboardButton.builder().text("75%").callbackData("75%_BUTTON_CLICKED").build(),
                                             InlineKeyboardButton.builder().text("Easy").callbackData("100%_BUTTON_CLICKED").build()))
                        .build())
                .text("Flashcard " + flashcardEducationList.getFlashcardEducationListPK().getId()
                        + "/" + numberOfFlashcards +
                        "\n\nQuestion:\n" + currentFlashcard.getQuestion() +
                        "\n\nAnswer:\n" + currentFlashcard.getAnswer())
                .build();

        return messageWithShownAnswer;
    }

    public EditMessageText nextRepetitionFlashcard(long chatId, int messageId) {
        User user = userRepository.findById(chatId).orElseThrow();
        return nextRepetitionFlashcard(chatId, messageId, user.getCurrentFlashcard());
    }

    public EditMessageText nextRepetitionFlashcard(long chatId, int messageId, long currentFlashcardId) {
        User user = userRepository.findById(chatId).orElseThrow();

        user.setCurrentFlashcard(currentFlashcardId + 1);
        userRepository.save(user);

        long numberOfFlashcards = flashcardRepetitionListRepository.
                countFlashcardRepetitionListByFlashcardRepetitionListPK_User(user);

        if (user.getCurrentFlashcard() > numberOfFlashcards) {
            clearTemporaryResourcesAfterEducation(chatId);
            return createCongratulationMessage(chatId, messageId);
        }

        FlashcardRepetitionList flashcardRepetitionList = flashcardRepetitionListRepository.findById(
                new FlashcardRepetitionList.FlashcardRepetitionListPK(user.getCurrentFlashcard(), user)).orElseThrow();
        Flashcard currentFlashcard = flashcardRepetitionList.getFlashcard();

        EditMessageText editMessage = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("Flashcard (repetition) " +
                        flashcardRepetitionList.getFlashcardRepetitionListPK().getId() +
                        "/" + numberOfFlashcards + "\n\nQuestion:\n" + currentFlashcard.getQuestion())
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .callbackData("SHOW_ANSWER_REPETITION_CLICKED").text("Show answer").build())).build())
                .build();

        return editMessage;
    }

    public void clearTemporaryResourcesAfterEducation(long chatId) {
        User user = userRepository.findById(chatId).orElseThrow();
        flashcardEducationListRepository.deleteAllByFlashcardEducationListPK_User(user);
        flashcardRepetitionListRepository.deleteAllByFlashcardRepetitionListPK_User(user);
        flashcardStatusRepository.deleteAllByFlashcardStatusPK_User(user);

        user.setCurrentFlashcard(null);
        userRepository.save(user);
    }

    public EditMessageText createCongratulationMessage(long chatId, int messageId) {
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("CONGRATULATION_MESSAGE")
                //.replyMarkup() //TODO: Implement button which will restart pack learning class
                .build();
    }

    public EditMessageText nextFlashcard(long chatId, int messageId) {
        User user = userRepository.findById(chatId).orElseThrow();

        user.setCurrentFlashcard(user.getCurrentFlashcard() + 1);
        userRepository.save(user);

        long numberOfFlashcards = flashcardEducationListRepository.
                countFlashcardEducationListByFlashcardEducationListPK_User(user);

        if (user.getCurrentFlashcard() > numberOfFlashcards) {
            return nextRepetitionFlashcard(chatId, messageId, 0);
        }

        FlashcardEducationList flashcardEducationList = flashcardEducationListRepository.findById(
                new FlashcardEducationList.FlashcardEducationListPK(user.getCurrentFlashcard(), user)).orElseThrow();
        Flashcard currentFlashcard = flashcardEducationList.getFlashcard();

        EditMessageText editMessage = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text("Flashcard " + flashcardEducationList.getFlashcardEducationListPK().getId()
                        + "/" + numberOfFlashcards +
                        "\n\nQuestion:\n" + currentFlashcard.getQuestion())
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(List.of(InlineKeyboardButton.builder()
                                .callbackData("SHOW_ANSWER_CLICKED").text("Show answer").build())).build())
                .build();

        return editMessage;
    }

    public void moveFlashcardToRepetitionList(long chatId) {
        User user = userRepository.findById(chatId).orElseThrow();
        Flashcard currentFlashcard = flashcardEducationListRepository.findById(
                new FlashcardEducationList.FlashcardEducationListPK(user.getCurrentFlashcard(), user))
                .orElseThrow().getFlashcard();
        if (flashcardRepetitionListRepository.findAllByFlashcard(currentFlashcard).isEmpty()) {
            FlashcardRepetitionList flashcardRepetitionList =
                    new FlashcardRepetitionList(new FlashcardRepetitionList.FlashcardRepetitionListPK(
                            getAvailableIdForRepetitionList(chatId), user), currentFlashcard);
            flashcardRepetitionListRepository.save(flashcardRepetitionList);
        }
    }

    private long getAvailableIdForRepetitionList(long chatId) {
        List<Long> idsList = flashcardRepetitionListRepository.findIds(chatId);

        if (idsList.isEmpty()) {
            return 1L;
        } else {
            return idsList.get(0) + 1;
        }
    }

    public EditMessageText changeMsgToMsgWithShownAnswerRepetition(long chatId, int messageId) {
        User user = userRepository.findById(chatId).orElseThrow();
        long numberOfFlashcards = flashcardRepetitionListRepository.
                countFlashcardRepetitionListByFlashcardRepetitionListPK_User(user);
        FlashcardRepetitionList flashcardRepetitionList = flashcardRepetitionListRepository.findById(
                new FlashcardRepetitionList.FlashcardRepetitionListPK(user.getCurrentFlashcard(), user)).orElseThrow();
        Flashcard currentFlashcard = flashcardRepetitionList.getFlashcard();

        EditMessageText messageWithShownAnswer = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .replyMarkup(InlineKeyboardMarkup.builder()
                        .keyboardRow(List.of(InlineKeyboardButton.builder().text("Next Question").callbackData("NEXT_QUESTION_REPETITION_CLICKED").build()))
                        .build())
                .text("Flashcard " + flashcardRepetitionList.getFlashcardRepetitionListPK().getId()
                        + "/" + numberOfFlashcards +
                        "\n\nQuestion:\n" + currentFlashcard.getQuestion() +
                        "\n\nAnswer:\n" + currentFlashcard.getAnswer())
                .build();

        return messageWithShownAnswer;
    }

    public void duplicateFlashcard(long chatId, int numberOfDuplicates) {
        User user = userRepository.findById(chatId).orElseThrow();
        FlashcardEducationList flashcardEducationList = flashcardEducationListRepository.findById(
                        new FlashcardEducationList.FlashcardEducationListPK(user.getCurrentFlashcard(), user))
                .orElseThrow();
        Flashcard currentFlashcard = flashcardEducationList.getFlashcard();

        Optional<FlashcardStatus> flashcardStatusOptional = flashcardStatusRepository
                .findById(new FlashcardStatus.FlashcardStatusPK(user, currentFlashcard)); // TODO: repair problem https://stackoverflow.com/questions/76887311/listresultsconsumer-duplicate-row-was-found-and-assert-was-specified
        if (flashcardStatusOptional.isEmpty()){
            flashcardStatusRepository.save(new FlashcardStatus(
                    new FlashcardStatus.FlashcardStatusPK(user, currentFlashcard),
                    numberOfDuplicates,
                    null)); // TODO: set to appropriate difficulty
        } else {
            FlashcardStatus flashcardStatus = flashcardStatusOptional.get();
            flashcardStatus.setNumberOfDuplicatedCards(flashcardStatus.getNumberOfDuplicatedCards() - 1);

            if (numberOfDuplicates >= flashcardStatus.getNumberOfDuplicatedCards()) {
                int difference = numberOfDuplicates - flashcardStatus.getNumberOfDuplicatedCards();

                numberOfDuplicates -= flashcardStatus.getNumberOfDuplicatedCards();

                flashcardStatus.setNumberOfDuplicatedCards(flashcardStatus.getNumberOfDuplicatedCards() + difference);
                flashcardStatusRepository.save(flashcardStatus);
            }
        }

        if (numberOfDuplicates <= 0) {
            return;
        }

        long numberOfAllFlashcardsInDeck =
                flashcardEducationListRepository.countFlashcardEducationListByFlashcardEducationListPK_User(
                        user);
        long currentFlashcardId = flashcardEducationList.getFlashcardEducationListPK().getId();

        double divider = 1.0 / (numberOfDuplicates + 1);


        for (int i = 0; i < numberOfDuplicates; i++) {
            long numberOfFlashcardsAhead = numberOfAllFlashcardsInDeck - currentFlashcardId;
            long flashcardStep = (long) (numberOfFlashcardsAhead * divider) + 1; // because of "+ 1" newCoord can be out of range
            long newCoord = currentFlashcardId + flashcardStep * (i + 1);

            if (newCoord >= numberOfAllFlashcardsInDeck) {
                flashcardEducationListRepository.save(new FlashcardEducationList(
                        new FlashcardEducationList.FlashcardEducationListPK(
                                numberOfAllFlashcardsInDeck + 1, user),
                        currentFlashcard));
            } else {
                FlashcardEducationList savedFlashcardEducationList = flashcardEducationListRepository
                        .findById(new FlashcardEducationList.FlashcardEducationListPK(
                                newCoord, user)).orElseThrow();

                flashcardEducationListRepository.save(
                        new FlashcardEducationList(new FlashcardEducationList.FlashcardEducationListPK(
                                newCoord, user), currentFlashcard));
                newCoord++;
                FlashcardEducationList nextFlashcard;
                while (newCoord <= numberOfAllFlashcardsInDeck) {
                    nextFlashcard = flashcardEducationListRepository.findById(new FlashcardEducationList.FlashcardEducationListPK(newCoord, user)).orElseThrow();
                    savedFlashcardEducationList.setFlashcardEducationListPK(
                            new FlashcardEducationList.FlashcardEducationListPK(
                                    nextFlashcard.getFlashcardEducationListPK().getId(), user));
                    flashcardEducationListRepository.save(savedFlashcardEducationList);
                    savedFlashcardEducationList = nextFlashcard;
                    newCoord++;
                }

                savedFlashcardEducationList.setFlashcardEducationListPK(
                        new FlashcardEducationList.FlashcardEducationListPK(
                                newCoord, user));
                flashcardEducationListRepository.save(savedFlashcardEducationList);
            }

            numberOfAllFlashcardsInDeck++;
        }
    }

    public void decreaseNumberOfDuplicatesIfExists(long chatId) {
        User user = userRepository.findById(chatId).orElseThrow();
        FlashcardEducationList flashcardEducationList = flashcardEducationListRepository.findById(
                        new FlashcardEducationList.FlashcardEducationListPK(user.getCurrentFlashcard(), user))
                .orElseThrow();
        Flashcard currentFlashcard = flashcardEducationList.getFlashcard();
        Optional<FlashcardStatus> flashcardStatusOptional =
                flashcardStatusRepository.findById(new FlashcardStatus.FlashcardStatusPK(user, currentFlashcard));

        if (flashcardStatusOptional.isPresent()) {
            FlashcardStatus flashcardStatus = flashcardStatusOptional.get();
            flashcardStatus.setNumberOfDuplicatedCards(flashcardStatus.getNumberOfDuplicatedCards() - 1);
            flashcardStatusRepository.save(flashcardStatus);
        }
    }
}

