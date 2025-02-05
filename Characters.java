import java.util.List;

/**
 * Characters is an interface of all the actors in the field. 
 *
 * @author Majed Alali and Vinushan Nagentherarajah
 * @version 3.0
 */
public abstract class Characters
{
    // The length of sickness
    private static final int SICKNESS_LENGTH = 10;
    // Variable to limit movement.
    private static final int MOVEMENT_LIMITER = 3;
    // Whether the character is alive or not.
    protected boolean alive;
    // The character's position.
    protected Location location;
    // The character's wellness.
    private boolean sick;
    // Counts the number of steps character was sick for, resets on 
    // eating another poop.
    private int sickCounter;
    // Counter for the number of movements for use in preventing movement 
    // at set intervals
    private int moveCounter;
    /**
     * Constructor of the class Characters.
     */
    public Characters (Location location) {
        this.alive = true;
        this.location = location;
        sick = false;
        sickCounter = 0;
        moveCounter = 0;
    }
    
    /**
     * Character does specific actions in the field.
     * @param currentField The current state of the field.
     * @param nextFieldState The new state being built.
     */
    abstract public void act(Field currentField, Field nextFieldState);
    
    /**
     * Character does specific actions in the field during nighttime.
     * @param currentField The current state of the field.
     * @param nextFieldState The new state being built.
     */
    abstract public void nightAct(Field currentField, Field nextFieldState);
    
    /**
     * Check whether a certain character is alive or not.
     * @return true if the character is alive, or return false if the character is dead.
     */
    public boolean isAlive()
    {
        return alive;
    }
    
    /**
     * Indicate that the character is no longer alive.
     */
    protected void setDead()
    {
        alive = false;
        location = null;
    }
    
    /**
     * Return the character's location.
     * @return The character's location.
     */
    public Location getLocation()
    {
        return location;
    }
    
    /**
     * Set the character's location.
     * @param location The new location.
     */
    protected void setLocation(Location location)
    {
        this.location = location;
    }
    
    /**
     * Sets the prey to be sick and resets the sickCounter, which 
     * determines the number of steps it was sick for
     */
    protected void setSick() {
        sick = true;
        sickCounter = 0;
    }
    
    /**
     * @return True if the Character is sick, False if not sick.
     */
    protected boolean isSick() {
        return sick;
    }
    
    /**
     * Increments the sick counter and if it exceeds the sickness 
     * length, the character becomes no longer sick by setting 
     * sick to false.
     */
    protected void incrementSickness() {
        sickCounter++;
        if(sickCounter > SICKNESS_LENGTH) {
            sickCounter = 0;
            sick = false;
        }
    }
    
    /**
     * Decides if a character can move or not at this step, sick 
     * characters only get to move on set intervals while those 
     * that are not sick move every step.
     */
    protected boolean canMove() {
        if(isSick()) {
            moveCounter++;
            if(moveCounter < MOVEMENT_LIMITER) {
                return false;
            }
            else {
                moveCounter = 0;
                return true;
            }
        }
        else {
            return true;
        }
    }
    
    /**
     * Disease transfers from nearby sick characters to this character 
     * if any sick characters are nearby.
     * @param adjacentLocation The locations adjacent to the one this
     * character is in.
     * @param currentField The current field object being used.
     */
    protected void diseaseTransfer(List<Location> adjacentLocation, Field currentField) {
        for(Location location : adjacentLocation) {
            Characters chara = currentField.getCharacterAt(location);
            if(chara != null) {
                if(chara.isSick()) {
                    setSick();
                    return;
                }
            }
        }
    }
}
