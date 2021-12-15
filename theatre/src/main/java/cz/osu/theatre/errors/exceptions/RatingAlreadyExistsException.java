package cz.osu.theatre.errors.exceptions;

public class RatingAlreadyExistsException extends RuntimeException{
    public RatingAlreadyExistsException(String message) {
        super(message);
    }
}
