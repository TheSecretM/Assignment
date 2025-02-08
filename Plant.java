import java.util.Random;
import java.util.List;

/**
 * Plants represents the plants that can be found around the field. 
 * Plants do not move, do not eat and can be eaten by all sorts of 
 * Animals. They can reproduce.
 *
 * @author Majed Alali and Vinushan Nagentherarajah
 * @version 3.0
 */
public class Plant extends Characters
{
    // The minimum age for plants to produce offspring
    private static final int BREEDING_AGE = 5;
    // The maximum number of plant offsprings a plant can produce at one time.
    private static final int MAX_BIRTH_RATE = 2;
    // The age to which a plant can live.
    private static final int MAX_AGE = 200;
    // The likelihood of a plant producing offspring.
    private static final double BREEDING_PROBABILITY = 0.16;
    // The plant's age.
    private int age;
    // A shared random number generator to control offspring produce.
    private static final Random rand = Randomizer.getRandom();
    
    /**
     * Constructor for objects of class Plant that initialises 
     * the age to 0.
     */
    public Plant(Location location)
    {
        super(location);
        age = 0;
    }
    
    /**
     * Check whether this plant is to produce offspring at this step.
     * New births will be made into free adjacent locations.
     * @param freeLocations The locations that are free in the current field.
     */
    private void giveBirth(Field nextFieldState, List<Location> freeLocations)
    {
        // New predators are born into adjacent locations.
        // Get a list of adjacent free locations.
        int births = breed();
        if(births > 0) {
            for (int b = 0; b < births && ! freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.removeFirst();
                Plant young = new Plant(loc);
                nextFieldState.placeCharacter(young, loc);
            }
        }
    }
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_BIRTH_RATE) + 1;
        }
        else {
            births = 0;
        }
        return births;
    }
    
    /**
     * A plant can breed if it has reached the breeding age.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
    
    /**
     * Increase the age. This could result in the plant's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * This is what the prey does most of the time - it runs 
     * around. Sometimes it will breed or die of old age. It 
     * can eat wastes and get sick.
     * @param currentField The field occupied.
     * @param nextFieldState The updated field.
     */
    public void act(Field currentField, Field nextFieldState)
    {
        if(isAlive()) {
            incrementAge();
            // Makes sure the object remains in place while not getting 
            // removed from the field.
            nextFieldState.placeCharacter(this, this.location);
            // Checks Adjacent Locations for a free space for giving birth.
            List<Location> freeLocations = 
            nextFieldState.getFreeAdjacentLocations(getLocation());
            if(!freeLocations.isEmpty()) {
                giveBirth(nextFieldState, freeLocations);
            }
        }
    }
    
    /**
     * Has less chance of giving birth at night. Otherwise,
     * it's actions are the same.
     */
    public void nightAct(Field currentField, Field nextFieldState) {
        if(isAlive()) {
            incrementAge();
            // Makes sure the object remains in place while not getting 
            // removed from the field.
            nextFieldState.placeCharacter(this, this.location);
            // Checks Adjacent Locations for a free space for giving birth.
            List<Location> freeLocations = 
            nextFieldState.getFreeAdjacentLocations(getLocation());
            if(!freeLocations.isEmpty()) {
                Random rand = new Random();
                if(rand.nextBoolean()) {
                    giveBirth(nextFieldState, freeLocations);
                }
            }
        }
    }
    
    @Override
    public String toString() {
        return "Plant{" +
                "age=" + age +
                ", alive=" + isAlive() +
                ", location=" + getLocation() +
                '}';
    }
}
