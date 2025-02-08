import java.util.*;

/**
 * Represent a rectangular grid of field positions.
 * Each position is able to store a single character/object.
 * 
 * @author David J. Barnes and Michael Kölling and Majed Alali and Vinushan Nagentherarajah
 * @version 9.1
 */
public class Field
{
    // A random number generator for providing random locations.
    private static final Random rand = Randomizer.getRandom();
    
    // The dimensions of the field.
    private final int depth, width;
    // Animals mapped by location.
    private final Map<Location, Characters> field = new HashMap<>();
    // The characters.
    private final List<Characters> characters = new ArrayList<>();

    /**
     * Represent a field of the given dimensions.
     * @param depth The depth of the field.
     * @param width The width of the field.
     */
    public Field(int depth, int width)
    {
        this.depth = depth;
        this.width = width;
    }

    /**
     * Place a Character at the given location.
     * If there is already a character at the location it will
     * be lost.
     * @param character The character to be placed.
     * @param location Where to place the character.
     */
    public void placeCharacter(Characters character, Location location)
    {
        assert location != null;
        boolean change = true;
        Object other = field.get(location);
        // Nothing overtakes Josh's place (He is immortal)
        if(other != null) {
            if(other instanceof Josh) {
                change = false;
            }
            else {
                characters.remove(other);
            }
        }
        if(change) {
            field.put(location, character);
            characters.add(character);
        }
        else {
            character.setDead();
        }
    }
    
    /**
     * Return the character at the given location, if any.
     * @param location Where in the field.
     * @return The character at the given location, or null if there is none.
     */
    public Characters getCharacterAt(Location location)
    {
        return field.get(location);
    }

    /**
     * Get a shuffled list of the free adjacent locations.
     * @param location Get locations adjacent to this.
     * @return A list of free adjacent locations.
     */
    public List<Location> getFreeAdjacentLocations(Location location)
    {
        List<Location> free = new LinkedList<>();
        List<Location> adjacent = getAdjacentLocations(location);
        for(Location next : adjacent) {
            Characters character = field.get(next);
            if(character == null) {
                free.add(next);
            }
            else if(!character.isAlive()) {
                free.add(next);
            }
        }
        return free;
    }

    /**
     * Return a shuffled list of locations adjacent to the given one.
     * The list will not include the location itself.
     * All locations will lie within the grid.
     * @param location The location from which to get List of adjacent ones.
     * @return A list of locations adjacent to that given.
     */
    public List<Location> getAdjacentLocations(Location location)
    {
        // The list of locations to be returned.
        List<Location> locations = new ArrayList<>();
        if(location != null) {
            int row = location.row();
            int col = location.col();
            for(int roffset = -1; roffset <= 1; roffset++) {
                int nextRow = row + roffset;
                if(nextRow >= 0 && nextRow < depth) {
                    for(int coffset = -1; coffset <= 1; coffset++) {
                        int nextCol = col + coffset;
                        // Exclude invalid locations and the original location.
                        if(nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                            locations.add(new Location(nextRow, nextCol));
                        }
                    }
                }
            }
            
            // Shuffle the list. Several other methods rely on the list
            // being in a random order.
            Collections.shuffle(locations, rand);
        }
        return locations;
    }

    /**
     * Print out the number of predators, preys and plants in the field.
     */
    public void fieldStats()
    {
        int numPredators = 0, numPreys = 0, numPlants = 0, numPoops = 0;
        for(Characters chara : field.values()) {
            if(chara != null) {
                if(chara.isAlive()) {
                    if(chara instanceof Plant) {
                        numPlants++;
                    }
                    else if(chara instanceof Prey) {
                        numPreys++;
                    }
                    else if(chara instanceof Predator) {
                        numPredators++;
                    }
                    else if(chara instanceof Poop) {
                        numPoops++;
                    }
                }
            }
        }
        System.out.println("Preys: " + numPreys +
                           " Predators: " + numPredators +
                           " Plants: " + numPlants +
                           " Poops: " + numPoops);
    }

    /**
     * Empty the field.
     */
    public void clear()
    {
        Iterator<Characters> characters = field.values().iterator();
        characters.forEachRemaining(Characters::setDead);
        field.clear();
    }

    /**
     * Return whether there is at least one rabbit and one fox in the field.
     * @return true if there is at least one rabbit and one fox in the field.
     */
    public boolean isViable()
    {
        boolean preyFound = false;
        boolean predatorFound = false;
        Iterator<Characters> it = characters.iterator();
        while(it.hasNext() && ! (preyFound && predatorFound)) {
            Characters character = it.next();
            if(character instanceof Prey prey) {
                if(prey.isAlive()) {
                    preyFound = true;
                }
            }
            else if(character instanceof Predator predator) {
                if(predator.isAlive()) {
                    predatorFound = true;
                }
            }
        }
        return preyFound && predatorFound;
    }
    
    /**
     * Get the list of characters.
     */
    public List<Characters> getCharacters()
    {
        return characters;
    }

    /**
     * Return the depth of the field.
     * @return The depth of the field.
     */
    public int getDepth()
    {
        return depth;
    }
    
    /**
     * Return the width of the field.
     * @return The width of the field.
     */
    public int getWidth()
    {
        return width;
    }
}
