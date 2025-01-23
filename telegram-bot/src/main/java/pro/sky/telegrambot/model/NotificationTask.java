package pro.sky.telegrambot.model;

import pro.sky.telegrambot.exception.DateTimeBeforeException;
import pro.sky.telegrambot.exception.ErrorMessage;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private long id;
    private long chatId;
    @NotNull
    private boolean isActive;
    @NotNull
    private LocalDateTime notificationDateTime;
    @NotNull
    private LocalDateTime createdAt;
    @NotNull
    private String task;

    public NotificationTask() {
    }

    public NotificationTask(long chatId,
                            String task,
                            LocalDateTime notificationDateTime) {

        this.chatId = chatId;
        this.task = task;
        this.isActive = true;
        this.createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        setDateAndTime(notificationDateTime);
    }

    private void setDateAndTime(LocalDateTime notificationDateTime) {
        Objects.requireNonNull(notificationDateTime, "Дата и время не может быть null");
        validateNotificationDateTime(notificationDateTime);
        this.notificationDateTime = notificationDateTime;
    }

    private void validateNotificationDateTime(LocalDateTime notificationDateTime) {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        if (notificationDateTime.isBefore(now)) {
            String errorMessage = String.format(
                    ErrorMessage.BEFORE_DATA_TIME_EXCEPTION_MESSAGE.getMessage(),
                    now,
                    notificationDateTime
            );
            throw new DateTimeBeforeException(errorMessage);
        }
    }

    public long getId() {
        return id;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getNotificationDateTime() {
        return notificationDateTime;
    }

    public void setNotificationDateTime(LocalDateTime notificationDateTime) {
        this.notificationDateTime = notificationDateTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return id == that.id && chatId == that.chatId && isActive == that.isActive && Objects.equals(notificationDateTime, that.notificationDateTime) && Objects.equals(createdAt, that.createdAt) && Objects.equals(task, that.task);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, isActive, notificationDateTime, createdAt, task);
    }
}