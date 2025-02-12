
/**
 * Simply a sound of someone pooping...
 *
 * @author Majed Alali and Vinushan Nagentherarajah
 * @version 3.1
 */
public class Pooping extends Sounds
{
    // The name of the file containing the pooping sound.
    private static final String POOP_SOUND_FILE = "turd.mp3";
    // Used to count the number of times the sound was asked to play.
    private static int COUNT = 0;
    // The number of times the method should be called to execute.
    private static final int PLAY_STEP = 50;

    /**
     * Constructor for objects of class Pooping
     */
    public Pooping()
    {
        
    }
    
    /**
     * Plays a sound that resembles pooping every set number 
     * of times it's called... Counter gets incremented 
     * every time this method is called.
     */
    public void playSound() 
    {
        COUNT++;
        if(COUNT % PLAY_STEP == 0) {
            playSoundEffects(POOP_SOUND_FILE);
        }
    }
}
