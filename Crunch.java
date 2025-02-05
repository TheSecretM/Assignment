import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.embed.swing.JFXPanel;

/**
 * Simply a sound of crunching on something...
 *
 * @author Majed Alali and Vinushan Nagentherarajah
 * @version 2.3
 */
public class Crunch
{
    // The name of the file containing the crunching sound.
    private static final String CRUNCH_SOUND_FILE = "crunch.mp3";
    // Used to count the number of times the sound was asked to play.
    private static int COUNT;
    // The file where the sounds are stored.
    private File file;

    /**
     * Constructor for objects of class Crunch
     */
    public Crunch()
    {
        COUNT = 0;
        final JFXPanel fxPanel = new JFXPanel();
    }
    
    /**
     * Plays a sound that resembles crunching something every 
     * set number of times it's called. Counter gets incremented 
     * every time this method is called.
     */
    public void playSound() {
        COUNT++;
        if(COUNT % 10 == 0) {
            Media crunchSound = new Media(new File(CRUNCH_SOUND_FILE).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(crunchSound);
            mediaPlayer.play();
        }
    }
}
