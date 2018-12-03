
import java.util.List;
import java.util.Random;

public class Food extends GameObject
{

    private static final double GRASS_CREATION_PROBABILITY = 0.175;
    private static final Random rand = new Random();
    private boolean exists;
    
    public Food()
    {
        exists = true;
    }
    
    public void setLocation(int row, int col)
    {
        this.location = new Location(row, col);
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }
    
    public void setEaten()
    {
        exists = false;
    }
    public void refresh(Field updatedField)
    {
        if (exists)
        {
            updatedField.place(this, location);
            if(rand.nextDouble() <= GRASS_CREATION_PROBABILITY)
            {
                
            }
        }
    }
    
    public boolean exists()
    {
        return exists;
    }
}