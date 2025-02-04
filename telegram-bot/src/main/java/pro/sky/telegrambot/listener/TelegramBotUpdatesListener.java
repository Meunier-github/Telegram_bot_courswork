package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.HandlerMessageService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final HandlerMessageService handlerMessageService;
    public TelegramBotUpdatesListener(HandlerMessageService handlerMessageService) {
        this.handlerMessageService = handlerMessageService;
    }

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            // Process your updates here
            long chatId = update.message().chat().id();
            String msg = update.message().text();
            boolean isCommand = msg.split(" ")[0].startsWith("/");
            SendMessage sendMessage;
            if (isCommand) {
                sendMessage = handlerMessageService.handleUpdateCommand(chatId, msg);
            } else {
                sendMessage = handlerMessageService.handleUpdateMessage(chatId, msg);
            }
            if (sendMessage != null) {
                telegramBot.execute(sendMessage);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
