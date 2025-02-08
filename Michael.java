import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Michael is the LEGENDARY PROFESSO.... I mean the subclass of Predators.
 * Michael eats Prey and can be eaten by Predator subclass Josh. They sleep 
 * at night and can poop.
 *
 * @author Majed Alali and Vinushan Nagentherarajah
 * @version 3.1
 */
public class Michael extends Predator
{
    // Used to play poop sounds in certain conditions...
    Pooping sound;

    /**
     * Constructor for objects of class Michael
     */
    public Michael(boolean randomAge, Location location)
    {
        super(randomAge, location);
        sound = new Pooping();
    }

    /**
     * Check whether this Michael predator is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param freeLocations The locations that are free in the current field.
     */
    protected void giveBirth(Field nextFieldState, List<Location> freeLocations)
    {
        // New Michael are born into adjacent locations.
        // Get a list of adjacent free locations.
        int births = breed();
        if(births > 0) {
            for (int b = 0; b < births && ! freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.removeFirst();
                Michael young = new Michael(false, loc);
                nextFieldState.placeCharacter(young, loc);
            }
        }
    }

    /**
     * This is what the Michael predator does most of the time: it hunts for
     * preys. In the process, it might breed, poop, die of hunger,
     * or die of old age. Does not age or do anything at night.
     * @param currentField The field currently occupied.
     * @param nextFieldState The updated field.
     */
    public void act(Field currentField, Field nextFieldState) 
    {
        // Only the list of occupied locations is used to check 
        // for any disease transfers, since only characters can 
        // transfer disease
        List<Location> occupied = 
            currentField.getAdjacentLocations(getLocation());
        List<Location> freeLocations =
            nextFieldState.getFreeAdjacentLocations(getLocation());
        occupied.removeAll(freeLocations);
        diseaseTransfer(occupied, currentField);
        if(!canMove()) {
            if(isAlive()) {
                nextFieldState.placeCharacter(this, getLocation());
            }
        }
        else {
            if(isAlive()) {
                incrementAge();
                incrementHunger();
                incrementSickness();
                Random rand = new Random();
                if(! freeLocations.isEmpty()) {
                    if(rand.nextInt(0, 8) == 0) {
                        poop(nextFieldState, freeLocations);
                    }
                    else {
                        giveBirth(nextFieldState, freeLocations);
                    }
                }
                // Move towards a source of food if found.
                Location nextLocation = findFood(currentField);
                if(nextLocation == null && ! freeLocations.isEmpty()) {
                    // No food found - try to move to a free location.
                    nextLocation = freeLocations.removeFirst();
                }
                // Searches for poop to eat as a last resort, yikes!
                if(nextLocation == null) {
                    nextLocation = findPoop(currentField);
                }
                // See if it was possible to move.
                if(nextLocation != null) {
                    setLocation(nextLocation);
                    nextFieldState.placeCharacter(this, nextLocation);
                }
                else {
                    // Overcrowding.
                    setDead();
                }
            }
        }
    }
    
    /**
     * The lazy old man... I mean the great Michael sleeps at night, 
     * doing nothing and miraculously not even aging, which 
     * signifies his Greatness.
     */
    public void nightAct(Field currentField, Field nextFieldState) {
        if(isAlive()) {
            nextFieldState.placeCharacter(this, this.location);
        }
    }

    /**
     * Look for preys adjacent to the current location.
     * Only the first live prey is eaten.
     * @param field The field currently occupied.
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findFood(Field field)
    {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        Location foodLocation = null;
        while(foodLocation == null && it.hasNext()) {
            Location loc = it.next();
            Characters character = field.getCharacterAt(loc);
            if(character instanceof Prey prey) {
                if(prey.isAlive()) {
                    prey.setDead();
                    foodLevel = PREY_FOOD_VALUE;
                    foodLocation = loc;
                }
            }
        }
        return foodLocation;
    }

    /**
     * Poops... Yikes!
     * Michael shamelessly poops on one of the adjacent locations.
     */
    private void poop(Field nextFieldState, List<Location> freeLocations) {
        Location loc = freeLocations.removeFirst();
        Poop fresh = new Poop(loc);
        sound.playSound();
        nextFieldState.placeCharacter(fresh, loc);
    }
}
