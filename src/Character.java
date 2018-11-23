import java.util.*;

/**
 * Write a description of class Character here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Character
{

    private String name;
    private Room currentRoom;

    /**
     * Constructor for objects of class Character
     */
    public Character(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void changeRooms(Room targetRoom) {
      currentRoom = targetRoom;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }
}
