import java.util.HashMap;
import java.util.Set;

/**
 * Class Room - a room in an adventure game.
 *
 * A "Room" represents one location in the scenery of the game.  It is
 * connected to other rooms via exits.  For each existing exit, the room
 * stores a reference to the neighboring room.
 *
 * @author Michael Kölling, David J. Barnes and Fahim Mohammed
 * @version 2018.11.28
 */

public class Room {
  HashMap<String, Item> items;        // stores items of this room.
  private String description;
  private HashMap<String, Room> exits;        // stores exits of this room.
  private String name;
  private HashMap<String, Character> characters;

  /**
   * Create a room described "description". Initially, it has
   * no exits. "description" is something like "a kitchen" or
   * "an open court yard".
   *
   * @param description The room's description.
   */
  public Room(String name, String description) {
      this.name = name;
      this.description = description;
      exits = new HashMap<>();
      items = new HashMap<>();
      characters = new HashMap<>();
  }

  /**
   * Define an exit from this room.
   *
   * @param direction The direction of the exit.
   * @param neighbor  The room to which the exit leads.
   */
  void setExit(String direction, Room neighbor) {
      exits.put(direction, neighbor);
  }

  /**
   * Define an item in a room
   *
   * @param item An item object to place in the room
   */
  void addItem(Item item) {
      items.put(item.getName(), item);
  }

  /**
   * Define a character in a room
   *
   * @param character A character object to place in the room
   */
  void addCharacter(Character character) {
    characters.put(character.getName(), character);
  }

  /**
   * Removes a character in a room
   *
   * @param character A character object to remove from the room
   */
  void removeCharacter(Character character) {
    characters.remove(character.getName());
  }

  /**
   * @return The name of the room
   * (the one that was defined in the constructor).
   */
  public String getName() {
      return name;
  }

  /**
   * @return The short description of the room
   * (the one that was defined in the constructor).
   */
  String getShortDescription() {
    return description;
  }

  /**
   * Return a description of the room in the form:
   * You are in the kitchen.
   * Exits: north west
   *
   * @return A long description of this room
   */
  String getLongDescription() {
      return "You are " + description + ".\n"
      + getExitString() + getItemString() + getCharString();
  }

  /**
   * Return a string describing the room's exits, for example
   * "Exits: north west".
   *
   * @return Details of the room's exits.
   */
  private String getExitString() {
      StringBuilder returnString = new StringBuilder("Exits:");
      Set<String> keys = exits.keySet();
      keys.forEach(e -> returnString.append(" ").append(e));
      return returnString.toString();
  }

  /**
   * Return the room that is reached if we go from this room in direction
   * "direction". If there is no room in that direction, return null.
   *
   * @param direction The exit's direction.
   * @return The room in the given direction.
   */
  Room getExit(String direction) {
    return exits.get(direction);
  }

  /**
   * Return a string describing the items in the room, for example
   * "Items: beer".
   *
   * @return Details of the room's items.
   */
  private String getItemString() {
    if (items.isEmpty()) {
        return "";
    }
    StringBuilder returnString = new StringBuilder("\nItems:");
    Set<String> keys = items.keySet();
    keys.forEach(i -> returnString.append(" ").append(i));
    return returnString.toString();
  }

  /**
   * Return a string describing the charcters in the room, for example
   * "Character: Prof. Kölling".
   *
   * @return Details of the room's characters.
   */
  private String getCharString() {
    if (characters.isEmpty()) {
        return "";
    }
    StringBuilder returnString = new StringBuilder("\nCharacters:");
    Set<String> keys = characters.keySet();
    keys.forEach(c -> returnString.append(" ").append(c));
    return returnString.toString();
  }

  /**
   * Returns the characters in the current room
   */
  HashMap<String, Character> getCharacters()
  {
    return characters;
  }

  /**
   * Returns the items in the current room
   */
  HashMap<String, Item> getItems() {
      return items;
  }
}
