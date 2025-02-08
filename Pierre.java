import java.util.List;

/**
 * Pierre is a subclass of Prey. They can eat Plants and reproduce by 
 * themselves. They sleep at night, which stops them from doing 
 * any actions at that time.
 *
 * @author Majed Alali and Vinushan Nagentherarajah
 * @version 2.1
 */
public class Pierre extends Prey
{
    /**
     * Constructor for objects of class Pierre
     */
    public Pierre(boolean randomAge, Location location)
    {
        super(randomAge, location);
    }
    
    /**
     * Check whether this prey is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param freeLocations The locations that are free in the current field.
     */
    protected void giveBirth(Field nextFieldState, List<Location> freeLocations)
    {
        // New preys are born into adjacent locations.
        // Get a list of adjacent free locations.
        int births = breed();
        if(births > 0) {
            for (int b = 0; b < births && !freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.removeFirst();
                Prey young = new Pierre(false, loc);
                nextFieldState.placeCharacter(young, loc);
            }
        }
    }
}
