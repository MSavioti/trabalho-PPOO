import java.util.List;
import java.util.Random;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-11
 */
public class Rabbit
{
    // Characteristics shared by all rabbits (static fields).

    // The age at which a rabbit can start to breed.
    private static final int BREEDING_AGE = 5;
    // The age to which a rabbit can live.
    private static final int MAX_AGE = 190;
    // The likelihood of a rabbit breeding.
    private static final double BREEDING_PROBABILITY = 0.8;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 5;
    // A shared random number generator to control breeding.
    private static final Random rand = new Random();
    
    // Individual characteristics (instance fields).
    
    // The rabbit's age.
    private int age;
    // Whether the rabbit is alive or not.
    private boolean alive;
    // The rabbit's position
    private Location location;
    
    private int hp;
    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     */
    public Rabbit(boolean randomAge)
    {
        hp = 1;
        age = 0;
        alive = true;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }
    
    /**
     * This is what the rabbit does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     */
    /*public void run(Field updatedField, List newRabbits)
    {
        incrementAge();
        if(alive) {
            Location newLocation = updatedField.closestFoodLocation(location);
            if(newLocation != null)
            {
                Food food = (Food)updatedField.getObjectAt(newLocation);
                if (food != null)
                    food.setEaten();
                System.out.println("O hp do coelho x: " + location.getCol() + " y: " + location.getRow() +  " agora é hp" + hp);
            }
            else
            {
                newLocation = updatedField.freeAdjacentLocation(location);
            }
            // Only transfer to the updated field if there was a free location
            if(newLocation != null) {
                
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            }
            else {
                // can neither move nor stay - overcrowding - all locations taken
                alive = false;
                System.out.println("o coelho da posicao x: " + location.getCol() + " y: " + location.getRow() + " nao pode se mover ou ficar");
            }
        }
    }*/

    public void hunt(Field currentField, Field updatedField, List newRabbits)
    {
        if(isAlive()) {
            // Move towards the source of food if found.
            Location newLocation = currentField.closestFoodLocation(location);
            
            if(newLocation != null) {
                int births = 0;
                System.out.println("Terá " + births + " filhos");
                for(int b = 0; b < births; b++) {
                    Rabbit newRabbit = new Rabbit(false);
                    newRabbits.add(newRabbit);
                    Location loc = updatedField.randomAdjacentLocation(location);
                    newRabbit.setLocation(loc);
                    updatedField.place(newRabbit, loc);
                }
                Food food = (Food) currentField.getObjectAt(newLocation);
                if (food != null)
                    food.setEaten();
            }
            else
                newLocation = updatedField.freeAdjacentLocation(location);
            // Only transfer to the updated field if there was a free location
            if(newLocation != null) {
                setLocation(newLocation);
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
            System.out.println("coelho morrendo por idade");
        }
    }
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(rand.nextDouble() <= BREEDING_PROBABILITY) {          
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
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
    public void tryEat()
    {
        if (hp == 1)
        {
            alive = false;
            System.out.println("o coelho da posicao x: " + location.getCol() + " y: " + location.getRow() + " foi comido");
        }
        else
        {
            hp--;
            System.out.println("O coelho da posicao x: " + location.getCol() + " y: " + location.getRow() + "levou dano e esta com " + hp + "hp");
        }
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
