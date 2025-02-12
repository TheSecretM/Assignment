import java.util.List;
import java.util.Random;

/**
 * Leonardo is a subclass of Prey. They sleep at night (do not do 
 * anything) and might breed when another Leonardo of the same 
 * gender is adjacent to it, gay or lesbian breeding is possible....
 *
 * @author Majed Alali and Vinushan Nagentherarajah
 * @version 2.2
 */
public class Leonardo extends Prey
{
    // The probability of Leonardo breeding.
    private static final double BREEDING_PROBABILITY = 0.97;
    // Determines if a Leonardo object is male or not a male(female).
    private final boolean male;
    /**
     * Constructor for objects of class Agi
     */
    public Leonardo(boolean randomAge, Location location)
    {
        super(randomAge, location);
        Random rand = new Random();
        male = rand.nextBoolean();
    }

    /**
     * This is what Leonardo does most of the time: it moves around 
     * and looks for plants to eat. In the process, it might breed 
     * when close to another Leonardo of a different gender,
     * it might die of hunger, or die of old age.
     * @param currentField The field currently occupied.
     * @param nextFieldState The updated field.
     */
    @Override
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
        if(isAlive()) {
            if(!canMove()) {
                nextFieldState.placeCharacter(this, getLocation());
            }
            else {
                incrementAge();
                incrementHunger();
                incrementSickness();
                if(! freeLocations.isEmpty()) {
                    // Lists close by characters and stores them in a List
                    List<Location> occupiedInNext =
                        nextFieldState.getAdjacentLocations(getLocation());
                    occupiedInNext.removeAll(freeLocations);
                    Leonardo second = closeByLeonardo(occupiedInNext, currentField);
                    // Only gives birth when 2 Luis objects are close 
                    // to each other and are of different genders.
                    if(second != null) {
                        if(sameGender(this, second)) {
                            giveBirth(nextFieldState, freeLocations);
                        }
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
                Prey young = new Leonardo(false, loc);
                nextFieldState.placeCharacter(young, loc);
            }
        }
    }

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    @Override
    protected int breed()
    {
        int births;
        Random rand = new Random();
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_CHILD_SIZE) + 1;
        }
        else {
            births = 0;
        }
        return births;
    }

    /**
     * Checks if there is another Leonardo character close to this 
     * Leonardo character are returns it if so, prioritising one with a 
     * different gender if found, or null if none are close.
     * @return The nearby Leonardo giving priority to one
     * with different gender than this Leonardo, or null if none are found
     * @param closeByCharacters The list of locations of characters
     * close to this Leonardo.
     * @param currentField The current field.
     */
    private Leonardo closeByLeonardo(List<Location> closeByCharacters, Field currentField) {
        Leonardo lastLeonardo = null;
        for(Location location : closeByCharacters) {
            if(currentField.getCharacterAt(location) instanceof Leonardo leonardo) {
                if(this.sameGender(this, leonardo)) {
                    return leonardo;
                }
                else {
                    lastLeonardo = leonardo;
                }
            }
        }
        return lastLeonardo;
    }

    /**
     * Checks if the other Leonardo object has a different gender 
     * compared to this Leonardo object.
     * @return True if gender is same. False if both have different gender.
     * @param secondLeonardo The second Leonardo whose gender is to be checked
     * alongside the current Leonardo object.
     * @param leonardo This Leonardo object.
     */
    private boolean sameGender(Leonardo leonardo, Leonardo secondLeonardo) {
        return (leonardo.isMale() == secondLeonardo.isMale());
    }

    /**
     * Checks if this object has true for male, indicating 
     * that it is a male.
     * @return True if male, or return False if not male.
     */
    private boolean isMale() {
        return male;
    }
}
