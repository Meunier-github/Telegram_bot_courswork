package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.exception.CommandNotFoundException;
import pro.sky.telegrambot.exception.DateTimeBeforeException;
import pro.sky.telegrambot.exception.MessageParseException;


@Service
public class ErrorHandlerService {
    private final TelegramBot bot;

    public ErrorHandlerService(TelegramBot bot) {
        this.bot = bot;
    }

    public void handleDateTimeBeforeException(DateTimeBeforeException ex, long chatId) {
        String userErrorMessage = ex.getMessage();
        bot.execute(new SendMessage(chatId, userErrorMessage));
    }

    public void handlerMessageParseException(MessageParseException ex, long chatId) {
        String userErrorMessage = ex.getMessage();
        bot.execute(new SendMessage(chatId, userErrorMessage));
    }

    public void handlerCommandNotFoundException(CommandNotFoundException ex, long chatId) {
        String userErrorMessage = ex.getMessage();
        bot.execute(new SendMessage(chatId, userErrorMessage));
    }

    public void handlerThrowableException(Throwable ex, long chatId) {
        String userErrorMessage = "Что то пошло не так";
        bot.execute(new SendMessage(chatId, userErrorMessage));
    }
}