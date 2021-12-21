package cz.osu.theatre.errors;

import cz.osu.theatre.errors.exceptions.*;
import cz.osu.theatre.models.responses.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserInformationTakenException.class, BadCredentialsException.class, RatingValueException.class, RatingAlreadyExistsException.class, ParameterException.class,
            AuthorAlreadyExistException.class, DivisionAlreadyExistException.class, TheatreActivityAlreadyExistException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public MessageResponse badRequestException(Exception exception, WebRequest request) {
        log.error(String.format("Invoked exception type: BAD_REQUEST, full message: %s ", exception.getMessage()));

        return new MessageResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler({IllegalStateException.class, ConstraintViolationException.class, TheatreActivitySaveException.class, DivisionIsUsedException.class
            , NullPointerException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageResponse internalServerErrorException(Exception exception, WebRequest request) {
        log.error(String.format("Invoked exception type: INTERNAL_SERVER_ERROR, full message: %s ", exception.getMessage()));

        return new MessageResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler({UserNotFoundException.class, TheatreActivityNotFoundException.class, DivisionNotFoundException.class, RatingNotExistsException.class,
            AuthorNotFoundException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public MessageResponse notFoundException(Exception exception, WebRequest request) {
        log.error(String.format("Invoked exception type: NOT_FOUND, full message: %s ", exception.getMessage()));

        return new MessageResponse(HttpStatus.NOT_FOUND, exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public MessageResponse unauthorizedException(AccessDeniedException exception, WebRequest request) {
        log.error(String.format("Invoked exception type: UNAUTHORIZED, full message: %s ", exception.getMessage()));

        return new MessageResponse(HttpStatus.UNAUTHORIZED, exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(RefreshTokenException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public MessageResponse forbiddenException(RefreshTokenException exception, WebRequest request) {
        log.error(String.format("Invoked exception type: FORBIDDEN, full message: %s ", exception.getMessage()));

        return new MessageResponse(HttpStatus.FORBIDDEN, exception.getMessage(), LocalDateTime.now());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String errorResult = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error(String.format("Invoked exception type: BAD_REQUEST, full message: %s ", errorResult));

        return new ResponseEntity<>(new MessageResponse(HttpStatus.BAD_REQUEST, errorResult, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

}
