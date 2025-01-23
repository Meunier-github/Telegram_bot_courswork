package pro.sky.telegrambot.component;

import org.springframework.stereotype.Component;
import pro.sky.telegrambot.exception.ErrorMessage;
import pro.sky.telegrambot.exception.MessageParseException;
import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class MessageParserComponent {

    public NotificationTask parseToNotificationTask(long chatId, String message) throws IllegalArgumentException {

        String regex = "(\\d{2}\\.\\d{2}\\.\\d{4})\\s+(\\d{2}:\\d{2})\\s+(.+)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);

        if (!matcher.matches()) {
            String errorMessage = String.format(
                    ErrorMessage.PARSE_EXCEPTION_MESSAGE.getMessage(),
                    message
            );
            throw new MessageParseException(errorMessage);
        }

        String dateString = matcher.group(1);
        String timeString = matcher.group(2);
        String task = matcher.group(3);

        try {
            LocalDateTime localDateTime = convertStringToLocalDateTime(dateString, timeString);
            return new NotificationTask(chatId, task, localDateTime);
        } catch (DateTimeParseException ex) {
            throw new MessageParseException(ErrorMessage.INVALID_DATA_TIME_PARSE_EXCEPTION_MESSAGE.getMessage());
        }
    }

    private LocalDateTime convertStringToLocalDateTime(String dateString, String timeString) throws DateTimeParseException {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = LocalDate.parse(dateString, dateFormatter);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime localTime = LocalTime.parse(timeString, timeFormatter);

        return LocalDateTime.of(localDate, localTime);
    }
}