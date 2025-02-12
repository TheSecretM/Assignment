
/**
 * Moan is a sound of moaning...
 *
 * @author Majed Alali and Vinushan Nagentherarajah
 * @version 1.0
 */
public class Moan extends Sounds
{
    // The name of the file containing the crunching sound.
    private static final String MOAN_SOUND_FILE = "moan.mp3";

    /**
     * Constructor for objects of class Moan
     */
    public Moan()
    {
        
    }
    
    /**
     * Plays a sound that resembles moaning something 
     * time it's called.
     */
    public void playSound() {
        playSoundEffects(MOAN_SOUND_FILE);
    }
}
