package pro.sky.telegrambot.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.exception.CommandNotFoundException;
import pro.sky.telegrambot.exception.DateTimeBeforeException;
import pro.sky.telegrambot.exception.MessageParseException;
import pro.sky.telegrambot.service.ErrorHandlerService;


@Aspect
@Component
public class ErrorAspect {
    private final Logger logger = LoggerFactory.getLogger(ErrorAspect.class);
    private final ErrorHandlerService errorHandlerService;

    public ErrorAspect(ErrorHandlerService errorHandlerService) {
        this.errorHandlerService = errorHandlerService;
    }

    @Pointcut("execution(* pro.sky.telegrambot.service.HandlerMessageService.handleUpdateMessage(..)) " +
            "|| execution(* pro.sky.telegrambot.service.HandlerMessageService.handleUpdateCommand(..)) ")
    public void pointCutErrorHandlerMessageService() {
    }


    @Around("pointCutErrorHandlerMessageService()")
    public Object handleError(ProceedingJoinPoint joinPoint) {
        long chatId = (long) joinPoint.getArgs()[0];
        try {
            return joinPoint.proceed();
        } catch (DateTimeBeforeException e) {
            logger.error(e.getMessage(), e);
            errorHandlerService.handleDateTimeBeforeException(e, chatId);
            return null;
        } catch (MessageParseException e) {
            logger.error(e.getMessage(), e);
            errorHandlerService.handlerMessageParseException(e, chatId);
            return null;
        } catch (CommandNotFoundException e) {
            logger.error(e.getMessage(), e);
            errorHandlerService.handlerCommandNotFoundException(e, chatId);
            return null;
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
            errorHandlerService.handlerThrowableException(e, chatId);
            return null;
        }
    }
}