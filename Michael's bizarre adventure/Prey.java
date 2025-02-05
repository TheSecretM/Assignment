import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A simple model of a prey, based on the rabbit's class.
 * Preys age, move, breed, and die. They might also eat waste and get sick, hindering their 
 * movements. And might spread the disease to nearby Animals
 * 
 * @author David J. Barnes and Michael Kölling and Majed Alali and Vinushan Nagentherarajah
 * @version 9.1
 */
public abstract class Prey extends Characters
{
    // Characteristics shared by all preys (class variables).
    // The age at which a prey can start to breed.
    protected static final int BREEDING_AGE = 5;
    // The age to which a prey can live.
    private static final int MAX_AGE = 70;
    // The likelihood of a prey breeding.
    private static final double BREEDING_PROBABILITY = 0.10;
    // The maximum number of births.
    protected static final int MAX_CHILD_SIZE = 5;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    // The food value of a single plant. In effect, this is the
    // number of steps a prey can go before it has to eat again.
    private static final int PLANT_FOOD_VALUE = 30;
    // Determines the value above which prey won't eat until it
    // goes below that again.
    private static final int HUNGER_VALUE = 200;

    // Individual characteristics (instance fields).

    // The prey's age.
    protected int age;
    // The prey's food level, which is increased by eating plants.
    private int foodLevel;
    // Movement attempts counter (sick preys can't move well enough)
    private int move;

    /**
     * Create a new prey. A prey may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the prey will have a random age.
     * @param location The location within the field.
     */
    public Prey(boolean randomAge, Location location)
    {
        super(location);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
        foodLevel = rand.nextInt(PLANT_FOOD_VALUE);
    }

    /**
     * This is what the prey does most of the time: it moves around 
     * and looks for plants to eat. In the process, it might breed, 
     * die of hunger, or die of old age.
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
                if(! freeLocations.isEmpty()) {
                    giveBirth(nextFieldState, freeLocations);
                }
                // Move towards a source of food if found.
                Location nextLocation = findFood(currentField);
                if(nextLocation == null && ! freeLocations.isEmpty()) {
                    // No food found - try to move to a free location.
                    nextLocation = freeLocations.remove(0);
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
     * This is what the prey does at night: sleep it off... 
     * Nothing changes at all while they sleep, except if they 
     * get eaten, in which case they die, poor animals...
     * @param currentField The field currently occupied.
     * @param nextFieldState The updated field.
     */
    public void nightAct(Field currentField, Field nextFieldState)
    {
        if(isAlive()) {
            nextFieldState.placeCharacter(this, this.getLocation());
        }
    }

    /**
     * Look for plants adjacent to the current location.
     * Only the first live plant is eaten.
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
            if(character instanceof Plant plant) {
                if(plant.isAlive() && foodLevel < HUNGER_VALUE) {
                    plant.setDead();
                    foodLevel = PLANT_FOOD_VALUE;
                    foodLocation = loc;
                }
            }
        }
        return foodLocation;
    }

    /**
     * Look for poops adjacent to the current location.
     * Only the first live poop is eaten.
     * @param field The field currently occupied.
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findPoop(Field field)
    {
        List<Location> adjacent = field.getAdjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        Location foodLocation = null;
        while(foodLocation == null && it.hasNext()) {
            Location loc = it.next();
            Characters character = field.getCharacterAt(loc);
            if(character instanceof Poop poop) {
                if(poop.isAlive()) {
                    poop.setDead();
                    setSick();
                    foodLocation = loc;
                }
            }
        }
        return foodLocation;
    }

    /**
     * Make this prey more hungry. This could result in the prey's death.
     */
    protected void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    @Override
    public String toString() {
        return "Prey{" +
        "age=" + age +
        ", alive=" + isAlive() +
        ", location=" + getLocation() +
        '}';
    }

    /**
     * Increase the age.
     * This could result in the prey's death.
     */
    protected void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }

    /**
     * Check whether or not this prey is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param freeLocations The locations that are free in the current field.
     */
    protected abstract void giveBirth(Field nextFieldState, List<Location> freeLocations);

    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    protected int breed()
    {
        int births;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_CHILD_SIZE) + 1;
        }
        else {
            births = 0;
        }
        return births;
    }

    /**
     * A prey can breed if it has reached the breeding age.
     * @return true if the rabbit can breed, false otherwise.
     */
    protected boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
