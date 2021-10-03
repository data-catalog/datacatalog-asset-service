package edu.bbte.projectbluebook.datacatalog.assets.exception;

import edu.bbte.projectbluebook.datacatalog.assets.model.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@RestControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public final ErrorResponse handleWebExchangeBindException(WebExchangeBindException e, ServerWebExchange exchange) {
        ConcurrentHashMap<String, String> messageObject = new ConcurrentHashMap<>();

        e.getFieldErrors()
                .forEach(violation -> messageObject
                .put(violation.getField(), Objects.requireNonNull(violation.getDefaultMessage())));

        return new ErrorResponse()
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .error(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase())
                .path(exchange.getRequest().getURI().getPath())
                .message(messageObject);
    }
}
