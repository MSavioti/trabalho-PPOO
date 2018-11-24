
import java.util.List;
import java.util.Random;

public class Food
{
    private Location location;
    private static final double SPAWNING_PROBABILITY = 0.8;
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
    public void refresh(Field currentField, Field updatedField)
    {
        if (exists)
            updatedField.place(this, location);
    }
    
    public boolean exists()
    {
        return exists;
    }
}
