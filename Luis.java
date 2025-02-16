import java.util.List;
import java.util.Random;

/**
 * Luis is a subclass of Prey that can only reproduce if a male Luis 
 * and female Luis are close to each other. Luis do not
 * sleep at night and continue acting throughout the day.
 *
 * @author Majed Alali and Vinushan Nagentherarajah
 * @version 2.4
 */
public class Luis extends Prey
{
    // The probability of Luis breeding.
    private static final double BREEDING_PROBABILITY = 0.97;
    // Determines if a Luis object is male or not a male(female).
    private final boolean male;

    /**
     * Constructor for objects of class Luis
     */
    public Luis(boolean randomAge, Location location)
    {
        super(randomAge, location);
        Random rand = new Random();
        // Male and not Male(Female) genders applied randomly.
        male = rand.nextBoolean();
    }

    /**
     * This is what Luis does most of the time: it moves around 
     * and looks for plants to eat. In the process, it might breed 
     * when close to another Luis of a different gender,
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
                    Luis second = closeByLuis(occupiedInNext, currentField);
                    // Only gives birth when 2 Luis objects are close 
                    // to each other and are of different genders.
                    if(second != null) {
                        if(differentGender(this, second)) {
                            giveBirth(nextFieldState, freeLocations);
                        }
                    }
                }
                // Move towards a source of food if found.
                Location nextLocation = findFood(currentField);
                if(nextLocation == null && !freeLocations.isEmpty()) {
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
                Prey young = new Luis(false, loc);
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
     * Checks if the other Luis object has a different gender 
     * compared to this Luis object.
     * @return True if gender is different. False if both have the same gender.
     * @param secondLuis The second Luis whose gender is to be checked
     * alongside the current Luis block.
     * @param luis This Luis object.
     */
    private boolean differentGender(Luis luis, Luis secondLuis) {
        return (luis.isMale() != secondLuis.isMale());
    }

    /**
     * Checks if this object has true for male, indicating 
     * that it is a male.
     * @return True if male, or return False if not male.
     */
    private boolean isMale() {
        return male;
    }

    /**
     * Checks if there is another Luis character close to this 
     * Luis character are returns it if so, prioritising one with a 
     * different gender if found, or null if none are close.
     * @return The nearby Luis, giving priority to one
     * with different gender than this Luis, or null if none are found
     * @param closeByCharacters The list of locations of characters
     * close to this Luis.
     * @param currentField The current field.
     */
    private Luis closeByLuis(List<Location> closeByCharacters, Field currentField) {
        Luis lastLuis = null;
        for(Location location : closeByCharacters) {
            if(currentField.getCharacterAt(location) instanceof Luis luis) {
                if(this.differentGender(this, luis)) {
                    return luis;
                }
                else {
                    lastLuis = luis;
                }
            }
        }
        return lastLuis;
    }
}
