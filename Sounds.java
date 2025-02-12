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
 * @version 1.1
 */
public class Sounds {
    // List of all files in the sound-effects directory.
    private final File[] files;
    // Name of the file in which the sound files are found.
    private static final String SOUND_FILE = "sound-effects";

    /**
     * Constructor of the class Sounds.
     */
    public Sounds() {
        files = new File(SOUND_FILE).listFiles();
        final JFXPanel fxPanel = new JFXPanel();
    }

    /**
     * Gets a single file based on the name in string.
     * Or returns null if no file was found with such name.
     */
    private File getSoundEffects(String fileName) {
        try {
            for(File file : files) {
                if(file.getName().equals(fileName)) {
                    return file;
                }
            }
        }
        catch(NullPointerException e) {
            System.out.println(e + "\n" + "Make sure you pass a valid name for the sound folder");
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
        try{
            Media sound = new Media(file.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (NullPointerException e) {
            System.out.println(e + "\n" + ": Make sure you enter a valid sound file from sound-effects folder in the corresponding sound class");
        }
    }
}
