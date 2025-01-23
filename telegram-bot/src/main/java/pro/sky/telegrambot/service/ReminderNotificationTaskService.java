package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class ReminderNotificationTaskService {
    final private TelegramBot bot;
    final private NotificationTaskService service;

    public ReminderNotificationTaskService(TelegramBot bot,
                                           NotificationTaskService service) {
        this.bot = bot;
        this.service = service;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void getNotificationTasksWhereIsTime() {
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> tasks = service.getByDateAndTimeNow(localDateTime);
        if (!tasks.isEmpty()) {
            sendNotificationTask(tasks);
        }
    }

    public void sendNotificationTask(List<NotificationTask> tasks) {
        tasks.stream()
                .parallel()
                .forEach(t -> {
                    SendMessage sendMessage = new SendMessage(t.getChatId(), "Напоминаю:\n" + t.getTask());
                    bot.execute(sendMessage);
                });
    }
}