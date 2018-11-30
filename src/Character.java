import java.util.*;

/**
 * Class Character - a character in the game
 *
 * @author Fahim Mohammed
 * @version 2018.11.29
 */
public class Character
{

  private String name;
  private Room currentRoom;
  private ArrayList<String> defaultResponses;
  private Random rng = new Random();

  /**
   * Constructor for objects of class Character
   * @param name Gives a character its name
   */
  public Character(String name) {
    this.name = name;
    defaultResponses = new ArrayList<>();
    fillDefaultResponses();
  }

  /**
   * Returns the name of a character
   */
  public String getName() {
      return name;
  }

  /**
   * Changes the room the character currently is in
   * @param targetRoom The room the character is moving to
   */
  void changeRooms(Room targetRoom) {
      currentRoom = targetRoom;
  }

  /**
   * Returns the room the character is currently in
   */
  Room getCurrentRoom() {
      return currentRoom;
  }

  /**
   * Adds a number of default responses to a list
   */
  private void fillDefaultResponses()
  {
    defaultResponses.add("Hey! How are you doing?");
    defaultResponses.add("Good to see you. Need help?");
    defaultResponses.add("I hope you like the course so far.");
    defaultResponses.add("Nice to see you. Any questions?");
  }

  /**
   * Returns a random string from a list of default responses
   */
  String getDefaultResponse()
  {
    int index = rng.nextInt(defaultResponses.size());
    return defaultResponses.get(index);
  }
}
