import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.embed.swing.JFXPanel;

/**
 * Simply a sound of someone releasing a turd
 *
 * @author Majed Alali and Vinushan Nagentherarajah
 * @version 2.1
 */
public class Pooping
{
    // The name of the file containing the pooping sound.
    private static final String POOP_SOUND_FILE = "turd.mp3";
    // Used to count the number of times the sound was asked to play.
    private static int COUNT;
    // The file where the sounds are stored.
    private File file;

    /**
     * Constructor for objects of class Pooping
     */
    public Pooping()
    {
        COUNT = 0;
        final JFXPanel fxPanel = new JFXPanel();
    }
    
    /**
     * Plays a sound that resembles pooping every set number 
     * of times it's called... Counter gets incremented 
     * every time this method is called.
     */
    public void playSound() 
    {
        COUNT++;
        if(COUNT % 10 == 0) {
            Media poopSound = new Media(new File(POOP_SOUND_FILE).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(poopSound);
            mediaPlayer.play();
        }
    }
}
