import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.embed.swing.JFXPanel;

/**
 * Sounds is the class that stores all the files of the directory which contains
 * the sound effects. It is responsible for fetching individual sound files and
 * playing them.
 *
 * @author Majed Alali and Vinushan Nagentherarajah
 * @version 1.0
 */
public class Sounds {
    // List of all files in the sound-effects directory.
    File[] files;

    /**
     * Constructor of the class Sounds.
     */
    public Sounds() {
        files = new File("sound-effects").listFiles();
        final JFXPanel fxPanel = new JFXPanel();
    }

    /**
     * Gets a single file based on the name in string.
     * Or returns null if no file was found with such name.
     */
    private File getSoundEffects(String fileName) {
        for(File file : files) {
            if(file.getName().equals(fileName)) {
                return file;
            }
        }
        return null;
    }

    /**
     * Plays the file with the given name, which is most
     * likely a sound effect as this is the only use of this
     * class as of the current code.
     */
    public void playSoundEffects(String fileName) {
        File file = getSoundEffects(fileName);
        if(file != null) {
            Media sound = new Media(file.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        }
        else {
            throw new RuntimeException("Sound effects not found with name " + fileName);
        }
    }
}
