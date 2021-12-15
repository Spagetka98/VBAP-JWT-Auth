package cz.osu.theatre.errors.exceptions;

public class UserInformationTakenException extends RuntimeException {
    public UserInformationTakenException(String message) {
        super(message);
    }
}
