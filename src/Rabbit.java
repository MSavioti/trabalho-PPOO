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
    private static final int BREEDING_AGE = 3;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 12;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.6;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 3;
    // A shared random number generator to control breeding.
    private static final Random rand = new Random();
    
    // Individual characteristics (instance fields).
    
    // The rabbit's age.
    private int age;
    // Whether the rabbit is alive or not.
    private boolean alive;
    // The rabbit's position
    //private Location location;
    // The fox's food level, which is increased by eating rabbits.
    private int foodLevel;

    //tells if the fox has eaten yet
    private boolean hasEatenYet;
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
        hasEatenYet = false;
        foodLevel = 10;
        /*if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }*/
    }

    public void hunt(Field currentField, Field updatedField, List newRabbits)
    {

        if(isAlive()) {
            //breed(updatedField, newRabbits);
            hasEatenYet = false;
            incrementAge();
            incrementHunger();
            // Move towards the source of food if found.
            Location newLocation = currentField.closestFoodLocation(location);
            if(newLocation != null) { //search for food
                Food food = (Food) currentField.getObjectAt(newLocation);

                if (food != null)
                { //found food
                    food.setEaten();
                    hasEatenYet = true;
                    foodLevel += 8;

                    if (canBreed()) {
                        breed(updatedField, newRabbits);
                    }
                }
            }
            if(newLocation == null) {
                newLocation = updatedField.freeAdjacentLocation(location);
                if(currentField.getObjectAt(newLocation) != null)
                    newLocation = null;
                //System.out.println("estou em : " + location);
                //currentField.percorrer();
            }

            // Only transfer to the updated field if there was a free location
            if(newLocation != null) {
                setLocation(newLocation);
//                System.out.println("new location: " + newLocation + "lá tem: " + currentField.getObjectAt(newLocation));
                updatedField.place(this, newLocation);
            }
        }
    }

    /**
     * Increase the age.
     * This could result in the rabbit's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            alive = false;
            System.out.println("o coelho da posicao x: " + location.getCol() + " y: " + location.getRow() + " morreu por velhice");
        }
    }

    /**
     * Make this rabbit more hungry. This could result in the fox's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            alive = false;
            System.out.println("o coelho da posicao x: " + location.getCol() + " y: " + location.getRow() + " morreu por fome");
        }
    }
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private void breed(Field updatedField, List newRabbits)
    {
        if(rand.nextDouble() <= BREEDING_PROBABILITY) {
            int births = rand.nextInt(MAX_LITTER_SIZE) + 1;
//            System.out.println("Terá " + births + " filhos");

            for(int b = 0; b < births; b++) {
                Rabbit newRabbit = new Rabbit(false);
                newRabbits.add(newRabbit);
                Location loc = updatedField.freeAdjacentLocation(location);
//                System.out.println(loc);
                newRabbit.setLocation(loc);
                updatedField.place(newRabbit, loc);
            }
        }
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
     * Tell the grass that it's dead now :(
     */
    public void beEaten()
    {
        alive = false;
        System.out.println("o coelho da posicao x: " + location.getCol() + " y: " + location.getRow() + " foi comido");
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
