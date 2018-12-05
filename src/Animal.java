import java.util.List;
import java.util.Random;

public abstract class Animal extends GameObject {
    // how much steps the animal has moved
    private int age;
    // max of steps that the animal can take in its life
    private int maxAge;
    // how much steps the animal can move before eating again
    private int foodLevel;
    // in how much the food level is increased after feeding
    private int foodValue;
    // age when the animal starts its breeding stage
    private int breedingStartAge;
    // probability of giving birth
    private double breedingProbability;
    // the max number of births
    private int maxLinterSize;
    // random
    private Random random;


    public Animal (int _maxAge, int _foodValue, int _breedStartAge, double _breedProb, int _maxLinterSize) {
        super();
        age = 0;
        maxAge = _maxAge;
        foodLevel = _foodValue;
        foodValue = _foodValue;
        breedingStartAge = _breedStartAge;
        breedingProbability = _breedProb;
        maxLinterSize = _maxLinterSize;
        random = new Random();
    }

    public void feed() {
        foodLevel = foodValue;
    }

    public void getOlder() {
        age++;
        if (age >= maxAge) {
            kill();
        }
    }

    public void getHungrier() {
        foodLevel--;
        if (foodLevel <= 0) {
            kill();
        }
    }

    /**
     * A fox can breed if it has reached the breeding age.
     */
    public boolean canBreed() {
        return age >= breedingStartAge;
    }

    public int getBirthsNumber() {
        if (random.nextDouble() <= breedingProbability) {
            return random.nextInt(maxLinterSize + 1);
        }
        return 0;
    }

    protected abstract void hunt(Field currentField, Field updatedField, List newFoxes);

    protected abstract Location findFood(Field field, Location location);
}
