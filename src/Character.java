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
    private ArrayList<String> defaultResponses;
    private Random rng;

    /**
     * Constructor for objects of class Character
     */
    public Character(String name) {
      this.name = name;
      defaultResponses = new ArrayList<>();
      fillDefaultResponses();
      rng = new Random();
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

    private void fillDefaultResponses()
    {
      defaultResponses.add("Hey! How are you doing?");
      defaultResponses.add("Good to see you. Need help?");
      defaultResponses.add("I hope you like the course so far.");
      defaultResponses.add("Nice to see you. Any questions?");
    }

    public String getDefaultResponse()
    {
      int index = rng.nextInt(defaultResponses.size());
      return defaultResponses.get(index);
    }
}
