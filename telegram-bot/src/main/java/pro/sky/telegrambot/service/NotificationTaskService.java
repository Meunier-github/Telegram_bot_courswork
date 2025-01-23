package pro.sky.telegrambot.service;

import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class NotificationTaskService {
    private final NotificationTaskRepository repository;

    public NotificationTaskService(NotificationTaskRepository repository) {
        this.repository = repository;
    }

    public void saveTask(NotificationTask task) {
        repository.save(task);
    }

    public List<NotificationTask> getByDateAndTimeNow(LocalDateTime localDateTime) {
        return repository.getByDateAndTimeNow(localDateTime);
    }
}