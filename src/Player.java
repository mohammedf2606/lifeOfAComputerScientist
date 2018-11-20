import java.util.*;
/**
 * Class Player - creates a player in the game
 *
 * @author Fahim Mohammed
 * @version 2018.11.17
 */
public class Player
{
    private HashMap<String, Item> inventory;
    private Integer maxWeight;
    private Integer currentWeight = 0;
    public Room currentRoom;
    private Set<String> itemSet;

    /**
     * Constructor for objects of class Player
     */
    public Player(Integer maxWeight)
    {
        this.maxWeight = maxWeight;
        inventory = new HashMap<>();
    }

    /**
     * Adds an item to player's inventory.
     * If max weight is exceeded, error message shown.
     */
    public void takeItem(Command command)
    {
        String itemName = command.getSecondWord();
        itemSet = currentRoom.getItems().keySet();
        if (itemSet.isEmpty()) {
          System.out.println("There's nothing to take from this room.");
        }
        else if (! itemSet.contains(itemName)) {
          System.out.println("Take what?");
        }
        else {
          Item item = currentRoom.getItems().get(itemName);
          Integer weight = item.getWeight();
          if (itemName.equals("backpack")) {
              System.out.println("Looks like someone left their bag after the lecture. It's bigger than yours.");
              System.out.println("Maybe you can make USE of it...");
          }
          else if (currentWeight + weight <= maxWeight) {
              if (weight == 0) {
                  System.out.println("You can't pick that up!");
              }
              else {
                  inventory.put(itemName, item);
                  currentWeight += weight;
                  System.out.println("Grabbed " + itemName);
              }
          }
          else {
              System.out.println("No more space in bag. You gotta drop something...");
          }
        }
    }

    /**
     * Removes an item from player's inventory.
     */
    public void dropItem(Command command)
    {
      String itemName = command.getSecondWord();
      Set<String> itemSet = currentRoom.getItems().keySet();
      if (! itemSet.contains(itemName)) {
        System.out.println("Drop what?");
      }
      else {
        inventory.remove(itemName);
        System.out.println("Dropped " + itemName);
      }
    }

    /**
     * Use an item by carrying out relative tasks
     */
    public void useItem(Command command)
    {
      String itemName = command.getSecondWord();
      if(! itemSet.contains(itemName)) {
        System.out.println("Use what?");
      }
      else {
        switch (itemName) {
          case "backpack":
            maxWeight *= 2;
            System.out.println("You doubled the maximum weight you can carry. You can now carry " + maxWeight/1000 + " kg.");
          case "computer":
            
        }
      }
    }

    /**
     * Returns items currently in player's inventory
     */
    public String getInventory()
    {
        String returnString = "Inventory:";
        Set<String> keys = inventory.keySet();
        for(String item : keys) {
            returnString += " " + item;
        }
        return returnString + "\nTotal weight: " + (currentWeight/1000).toString() + " kg";
    }
}
