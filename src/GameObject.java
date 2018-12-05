import java.util.Random;

public class GameObject {
    private Location location;
    private boolean alive;

    public GameObject() {
        alive = true;
    }

    /**
     * Check whether the game object is active or not.
     * @return True if the fox is still alive.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Turns game object to a inactive state
     */
    public void kill() {
        alive = false;
    }

    public Location getLocation() {
        return location;
    }

    /**
     * Set the game object's location.
     * @param row The vertical coordinate of the location.
     * @param col The horizontal coordinate of the location.
     */
    public void setLocation(int row, int col)
    {
        location = new Location(row, col);
    }

    /**
     * Set the game object's location.
     * @param _location The fox's location.
     */
    public void setLocation(Location _location)
    {
        location = _location;
    }
}
