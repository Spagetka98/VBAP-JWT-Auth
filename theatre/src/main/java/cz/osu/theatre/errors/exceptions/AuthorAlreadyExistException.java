package cz.osu.theatre.errors.exceptions;

public class AuthorAlreadyExistException extends RuntimeException {
    public AuthorAlreadyExistException(String message) {
        super(message);
    }
}
