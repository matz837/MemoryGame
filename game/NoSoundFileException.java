package game;

//NoSoundFileException is a custom exception for when an audio file cannot be found or played.
//This fulfills the requirement to have at least one custom exception.
public class NoSoundFileException extends Exception {
    public NoSoundFileException(String message) {
        super(message);
    }
}
