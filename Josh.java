import java.util.List;
import java.util.Iterator;

/**
 * Josh is a subclass of Predator and is the top of the food chain 
 * hierarchy. They can eat Predators and Preys and never age or die, but they 
 * never reproduce. They continue moving through the night.
 *
 * @author Majed Alali and Vinushan Nagentherarajah
 * @version 3.3
 */
public class Josh extends Predator
{
    // Used to play crunch sounds in certain conditions...
    Crunch sound;

    /**
     * Constructor for objects of class Josh.
     */
    public Josh(boolean randomAge, Location location)
    {
        super(randomAge, location);
        sound = new Crunch();
    }

    /**
     * This is what the predator does most of the time: it hunts for
     * preys. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param currentField The field currently occupied.
     * @param nextFieldState The updated field.
     */
    public void act(Field currentField, Field nextFieldState)
    {
        // Josh moves and acts regardless of time of day.
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
                incrementSickness();
                Location nextLocation = findFood(currentField);
                if(! freeLocations.isEmpty()) {
                    Michael michael = closebyMichael(occupied, currentField);
                    // Only gives birth when 2 Luis objects are close 
                    // to each other and are of different genders.
                    if(michael != null) {
                        giveBirth(nextFieldState, freeLocations);
                    }
                }
                // Move towards a source of food if found.
                if(nextLocation == null && ! freeLocations.isEmpty()) {
                    // No food found - try to move to a free location.
                    nextLocation = freeLocations.remove(0);
                }
                // Searches for poop to eat as a last resort, yikes!
                if(nextLocation == null && ! freeLocations.isEmpty()) {
                    nextLocation = findPoop(currentField);
                }
                // See if it was possible to move.
                if(nextLocation != null) {
                    setLocation(nextLocation);
                    nextFieldState.placeCharacter(this, nextLocation);
                }
                else {
                    // Overcrowding makes Josh overtake one position 
                    // and kill whoever is there, but doesn't overtake 
                    // another Josh.
                    List<Location> occupiedSpots = 
                        currentField.getAdjacentLocations(getLocation());
                    Location newLocation = 
                        overtake(occupiedSpots, currentField, nextFieldState);
                    // stays in place as a last resort.
                    if(newLocation == null) {
                        nextFieldState.placeCharacter(this, getLocation());
                    }
                }
            }
        }
    }
    
    /**
     * Nothing changes for Josh during the night, his actions 
     * are exactly the same as during the day. Hunt, breed Michaels, 
     * maybe eat poop and/or get sick. or eat anyone in his way if
     * the place is overcrowded.
     */
    public void nightAct(Field currentField, Field nextFieldState) {
        act(currentField, nextFieldState);
    }

    /**
     * Look for food adjacent to the current location, which are 
     * either Michael or Prey objects, since Josh eats any 
     * sort of meat. Only the first live Michael or prey is eaten.
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
            if(character instanceof Michael michael) {
                if(michael.isAlive()) {
                    michael.setDead();
                    sound.playSound();
                    foodLocation = loc;
                }
            }
            else if(character instanceof Prey prey) {
                if(prey.isAlive()) {
                    prey.setDead();
                    foodLocation = loc;
                }
            }
        }
        return foodLocation;
    }

    /**
     * Check whether this Josh object is to give birth at this step.
     * New births will be made into free adjacent locations. Josh 
     * can surprisingly only give birth to Michael...
     * @param freeLocations The locations that are free in the current field.
     * @param nextFieldState The next field state to be used.
     */
    protected void giveBirth(Field nextFieldState, List<Location> freeLocations)
    {
        // New Michael Predators are born into adjacent locations.
        // Get a list of adjacent free locations.
        int births = breed();
        if(births > 0) {
            for (int b = 0; b < births && ! freeLocations.isEmpty(); b++) {
                Location loc = freeLocations.remove(0);
                Predator young = new Michael(false, loc);
                nextFieldState.placeCharacter(young, loc);
            }
        }
    }
    
    /**
     * Checks for any closeby Michael Predators in the next field 
     * state and returns the first one found, or null if none are 
     * found.
     * @return Michael if a Michael object was found in one of the 
     * adjacent spots, or none if no Michael object is adjacent
     * to this Josh object.
     * @param closebyCharacters The list of locations occupied 
     * by Characters adjacent to this Josh object.
     * @param field The field to check.
     */
    private Michael closebyMichael(List<Location> closebyCharacters, Field field) {
        for(Location location : closebyCharacters) {
            if(field.getCharacterAt(location) instanceof Michael michael) {
                return michael;
            }
        }
        return null;
    }

    /**
     * With the almighty power, Josh overtakes a position of an adjacent 
     * character in case all other adjacent locations are occupied, 
     * ensuring his survival while also eating whatever character 
     * was in the overtaken spot. The last non-Josh character spotted 
     * will be overtaken.
     * @param occupiedSpots The adjacent spots containing a character.
     * @param currentField The current field object.
     * @param nextFieldState The next field object to be used after this 
     * current field object.
     * @return The new location to be placed in, or null if no 
     * overtaking happened.
     */
    private Location overtake(List<Location> occupiedSpots, Field currentField, Field nextFieldState) {
        int index = 0;
        Characters chara = null;
        Location newLocation = null;
        // Makes sure Josh doesn't overtake another Josh while 
        // getting another character to kill and overtake.
        while(index < occupiedSpots.size() && chara == null) {
            Characters temp = currentField.getCharacterAt(occupiedSpots.get(index));
            if(!(temp instanceof Josh) && temp != null && temp.isAlive()) {
                chara = temp;
            }
            index++;
        }
        if(chara != null) {
            newLocation = chara.getLocation();
            if(newLocation != null) {
                setLocation(newLocation);
                chara.setDead();
                nextFieldState.placeCharacter(this, newLocation);
            }
        }
        return newLocation;
    }
}
