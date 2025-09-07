package game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

//SoundPlayer is a utility class for playing audio files.
public class SoundPlayer {


    public static void playSound(String soundFilePath) throws NoSoundFileException {
        try {
            File soundFile = new File(soundFilePath);
            if (!soundFile.exists()) {
                // Requirement: Throw a custom exception.
                throw new NoSoundFileException("Sound file not found: " + soundFilePath);
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile.toURI().toURL());
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new NoSoundFileException("Could not play sound: " + e.getMessage());
        }
    }
}
