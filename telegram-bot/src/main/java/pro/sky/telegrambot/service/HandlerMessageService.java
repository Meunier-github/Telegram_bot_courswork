package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.component.MessageParserComponent;
import pro.sky.telegrambot.exception.CommandNotFoundException;
import pro.sky.telegrambot.model.NotificationTask;


@Service
public class HandlerMessageService {
    private final NotificationTaskService service;
    private final MessageParserComponent messageParserComponent;

    public HandlerMessageService(NotificationTaskService service,
                                 MessageParserComponent messageParserComponent) {
        this.service = service;
        this.messageParserComponent = messageParserComponent;
    }

    public SendMessage handleUpdateMessage(long chatId, String message) {
        NotificationTask task = messageParserComponent.parseToNotificationTask(chatId, message);
        service.saveTask(task);
        return new SendMessage(chatId, "Ваше напоминание сохранено: " + message);
    }

    public SendMessage handleUpdateCommand(long chatId, String message) {
        if (message.equalsIgnoreCase("/start")) {
            return new SendMessage(chatId, "Приветствую");
        } else {
            throw new CommandNotFoundException("Неизвестная команда");
        }
    }
}