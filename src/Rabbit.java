import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-11
 */
public class Rabbit extends GameObject
{
    // Characteristics shared by all rabbits (static fields).

    // The age at which a rabbit can start to breed.
    private static final int BREEDING_AGE = 2;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 50;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.15;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // The food value of a single rabbit. In effect, this is the
    // number of steps a fox can go before it has to eat again.
    private static final int GRASS_FOOD_VALUE = 4;
    // A shared random number generator to control breeding.
    private static final Random rand = new Random();
    
    // Individual characteristics (instance fields).
    
    // The rabbit's age.
    private int age;
    // Whether the rabbit is alive or not.
    private boolean alive;
    // The fox's food level, which is increased by eating rabbits.
    private int foodLevel;

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     */
    public Rabbit(boolean randomAge)
    {
        age = 0;
        alive = true;
        foodLevel = GRASS_FOOD_VALUE;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }

    public void hunt(Field currentField, Field updatedField, List newRabbits)
    {
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            // Move towards the source of food if found.
            Location newLocation = findFood(currentField, location);
            if(newLocation == null) {  // no food found - move randomly
                newLocation = updatedField.freeAdjacentLocation(location);
            }
            else { //found food
                int births = breed(); //only breed if have eaten
//                if (births > 0)
//                System.out.println(births + " novos coelhos nasceram!");

                for(int b = 0; b < births; b++) {
                    Rabbit newRabbit = new Rabbit(false);
                    newRabbits.add(newRabbit);
                    Location loc = updatedField.randomAdjacentLocation(location);
                    newRabbit.setLocation(loc);
                    updatedField.place(newRabbit, loc);
//                    System.out.println("Coelho recém-nascido posicionado em " + newRabbit.getLocation());
                }
            }

            if(newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            }
            else {
                // can neither move nor stay - overcrowding - all locations taken
                alive = false;
            }
        }
    }

    private Location findFood(Field field, Location location)
    {
        Iterator adjacentLocations = field.adjacentLocations(location);
        boolean hasEatenYet = false;

        while(adjacentLocations.hasNext() && !hasEatenYet) {
            Location where = (Location) adjacentLocations.next();
            Object object = field.getObjectAt(where);
            if(object instanceof Grass) {
                hasEatenYet = true;
                Grass grass = (Grass) object;
                if(grass.exists()) {
//                    System.out.println("O coelho da posição " + getLocation() + " comeu a grama da posição " + grass.getLocation());
                    grass.setEaten();
                    foodLevel = GRASS_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Increase the age.
     * This could result in the rabbit's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            System.out.println("Coelho da posição " + location.getRow() + "," + location.getCol() + " morreu por velhice.");
            alive = false;
        }
    }

    /**
     * Make this rabbit more hungry. This could result in the rabbit's death.
     */
    private void incrementHunger()
    {
//        foodLevel--;
//        if(foodLevel <= 0) {
//            System.out.println("Coelho da posição " + location.getRow() + "," + location.getCol() + " morreu por fome.");
//            alive = false;
//        }
    }
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A rabbit can breed if it has reached the breeding age.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
    
    /**
     * Check whether the rabbit is alive or not.
     * @return True if the rabbit is still alive.
     */
    public boolean isAlive()
    {
        return alive;
    }

    /**
     * Tell the rabbit that it's dead now :(
     */
    public void setEaten()
    {
        alive = false;
    }
    
    /**
     * Set the animal's location.
     * @param row The vertical coordinate of the location.
     * @param col The horizontal coordinate of the location.
     */
    public void setLocation(int row, int col)
    {
        this.location = new Location(row, col);
    }

    /**
     * Set the rabbit's location.
     * @param location The rabbit's location.
     */
    public void setLocation(Location location)
    {
        this.location = location;
    }
}
