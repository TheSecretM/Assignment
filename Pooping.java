
/**
 * Simply a sound of someone pooping...
 *
 * @author Majed Alali and Vinushan Nagentherarajah
 * @version 3.0
 */
public class Pooping extends Sounds
{
    // The name of the file containing the pooping sound.
    private static final String POOP_SOUND_FILE = "turd.mp3";
    // Used to count the number of times the sound was asked to play.
    private static int COUNT;

    /**
     * Constructor for objects of class Pooping
     */
    public Pooping()
    {
        COUNT = 0;
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
            playSoundEffects(POOP_SOUND_FILE);
        }
    }
}
