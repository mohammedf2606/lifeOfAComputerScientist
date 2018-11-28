import java.util.* ;

/**
 * Class Actions - executes the commands that are inputted by the user
 * @author Fahim Mohammed
 * @version 2018.11.28
 */
public class Actions {

  Parser parser;
  Player player;
  Time time;
  int turns = 0;
  private int INTPoints = 0;
  private float multiplier = 1;
  ArrayList<Room> roomList = new ArrayList<>();
  Room hall, office;
  Item elaCW, ppaCW;
  private boolean computerUsed;
  boolean elaHandedIn, ppaHandedIn, finished, released;
  private Stack<Room> backStack = new Stack<>();
  HashMap<String, Item> allItems = new HashMap<>();
  ArrayList<Character> charList = new ArrayList<>();

  /**
   * Print out some help information.
   * Here we print some stupid, cryptic message and a list of the
   * command words.
   */
  void printHelp() {
    TextReader textReader = new TextReader("help");
    textReader.reader();
    parser.showCommands();
  }

  /**
   * Implements the deadline command.
   */
  void getDeadline(Command command) {
    if (!command.hasSecondWord()) {
      System.out.println("What deadline would you like to check?");
    }
    String module = command.getSecondWord();
    switch (module) {
      case "ppa":
        System.out.println("The deadline for PPA is " + time.getTime(100));
        break;
      case "ela":
        System.out.println("The deadline for ELA is " + time.getTime(170));
        break;
      case "cs1":
      case "fc1":
        System.out.println("You don't have coursework for this module.");
        break;
      default:
        System.out.println("That is not the name of a module!");
        break;
    }
  }

  /**
   * Checks whether player and character are in the same room
   * If they are, correct interaction occurs
   */
  void playerNPCInteraction(Command command) {
    String characterName = command.getSecondWord();
    Set<String> playerItemSet = player.inventory.keySet();
    ArrayList<String> charNames = new ArrayList<>();
    charList.forEach(c -> charNames.add(c.getName()));
    if (characterName == null || !charNames.contains(characterName)) {
      System.out.println("Talk to who?");
    } else {
      Character character = player.currentRoom.getCharacters().get(characterName);
      if (player.currentRoom == office && character.getCurrentRoom() == office) {
        switch (character.getName()) {
          case "kolling":
            if (released && playerItemSet.contains("ppaCW")) {
              System.out.println("Prof. Kölling: Well done! You got your coursework in on time!");
              player.inventory.remove("ppaCW");
              ppaHandedIn = true;
            } else if (released) {
              System.out.println("Prof. Kölling: Good luck with your coursework!");
            } else {
              System.out.println(character.getDefaultResponse());
            }
            break;
          case "rodrigues":
            if (released && playerItemSet.contains("elaCW")) {
              System.out.println("Dr. Rodrigues: Well done! You got your coursework in on time!");
              player.inventory.remove("elaCW");
              elaHandedIn = true;
            } else if (released) {
              System.out.println("Dr. Rodrigues: Good luck with your coursework!");
            } else {
              System.out.println(character.getDefaultResponse());
            }
        }
      } else if (player.currentRoom == character.getCurrentRoom()) {
        switch (player.currentRoom.getName()) {
          case "lab":
            addINTPoints("lab");
            System.out.println("You have now attended the lab session!");
            turns += 6;
            break;
          case "theatre":
            addINTPoints("theatre");
            System.out.println("You just completed a lecture!");
            if (character.getName().equals("kolling")) {
              turns += 6;
            } else {
              turns += 8;
            }
            break;
          case "classroom":
            addINTPoints("classroom");
            System.out.println("You completed a small group tutorial!");
            turns += 4;
            break;
          default:
            return;
        }
        System.out.println("You have gained " + INTPoints + " INT points!");
        multiplier = 1;
      }
    }
  }


  /**
   * Use an item by carrying out relative tasks
   */
  void useItem(Command command) {
    String itemName = command.getSecondWord();
    Set<String> playerItemSet = player.inventory.keySet();
    Set<String> unmovableItems = new HashSet<>(Arrays.asList("computer", "printer"));
    if (!playerItemSet.contains(itemName) && !unmovableItems.contains(itemName)) {
      System.out.println("Use what?");
    } else {
      switch (itemName) {
        case "backpack":
          player.maxWeight *= 2;
          System.out.println("You doubled the maximum weight you can carry. You can now carry " + player.maxWeight / 1000 + " kg.");
          break;
        case "computer":
          if (released) {
            computerUsed = true;
            System.out.println("Now use the printer to print out your completed coursework.");
          } else {
            System.out.println("There's nothing to do on the computer yet.");
          }
          break;
        case "printer":
          if (computerUsed) {
            if (time.getTimeIndex(turns) >= 5 && time.getTimeIndex(turns) <= 12) {
              player.inventory.put(ppaCW.getName(), ppaCW);
              System.out.println("Now hand in your coursework to Prof. Kölling!");
            } else {
              player.inventory.put(elaCW.getName(), elaCW);
              System.out.println("Now hand in your coursework to Dr. Rodrigues!");
            }
          } else {
            System.out.println("There's nothing to print.");
          }
          break;
        case "exam":
          if (player.currentRoom == hall && turns > 200) {
            TextReader textReader = new TextReader("win");
            textReader.reader();
            float finalGrade = (float) INTPoints / 2000;
            if (finalGrade >= 0.7) {
              System.out.println("First");
            } else if (finalGrade >= 0.5) {
              System.out.println("2:1");
            } else if (finalGrade >= 0.4) {
              System.out.println("2:2");
            } else {
              System.out.println("Third");
            }
            finished = true;
            return;
          } else if (player.currentRoom != hall) {
            System.out.println("You can't sit your exam here");
          } else if (turns < 200) {
            System.out.println("You can't sit your exam yet. It's not exam week yet.");
          }
          return;
        case "beer":
          System.out.println("Beer will not help you learn!");
          multiplier -= (float) allItems.get(itemName).getWeight() / player.maxWeight;
          System.out.println("You will now lose " + Math.round(((1 - multiplier) * 100))
                  + "% of the INT points from your next timetabled lesson!");
          break;
        default:
          multiplier += (float) allItems.get(itemName).getWeight() / player.maxWeight;
          System.out.println("You will now gain " + Math.round(((multiplier - 1) * 100))
                  + "% more INT points from your next timetabled lesson!");
      }
      if (!player.inventory.isEmpty() && !computerUsed) {
        player.currentWeight -= player.inventory.get(itemName).getWeight();
        player.inventory.remove(itemName);
      }
    }
  }

  /**
   * Try to go to the previous room, else error message.
   */
  void goBack() {
    if (!backStack.empty()) {
      player.currentRoom = backStack.pop();
      look();
    } else {
      System.out.println("You are back to the start!");
    }
  }

  /**
   * Prints what is in the current room and the room's description
   */
  void look() {
    System.out.println("\n" + player.currentRoom.getLongDescription());
  }

  /**
   * Try to in to one direction. If there is an exit, enter the new
   * room, otherwise print an error message.
   */
  void goRoom(Command command) {
    if (!command.hasSecondWord()) {
      // if there is no second word, we don't know where to go...
      System.out.println("Go where?");
      return;
    }

    String direction = command.getSecondWord();

    // Try to leave current room.
    Room nextRoom = player.currentRoom.getExit(direction);
    Random randomiser = new Random();

    if (nextRoom == null) {
      System.out.println("There is no door!");
    } else {
      if (nextRoom.getName().equals("random")) {
        System.out.println("You are " + nextRoom.getShortDescription() + ".\n");
        while (nextRoom.getName().equals("random")) {
          nextRoom = roomList.get(randomiser.nextInt(roomList.size()));
        }
      }
      Room previousRoom = player.currentRoom;
      backStack.push(previousRoom);
      player.currentRoom = nextRoom;
      look();
    }
  }

  /**
   * Reads timetable text file and prints to terminal
   */
  void showTimetable() {
    TextReader textReader = new TextReader("timetable");
    textReader.reader();
  }

  /**
   * "Quit" was entered. Check the rest of the command to see
   * whether we really quit the game.
   *
   * @return true, if this command quits the game, false otherwise.
   */
  boolean quit(Command command) {
    if (command == null) {
      TextReader textReader = new TextReader("turn");
      textReader.reader();
      return true;
    } else if (command.hasSecondWord()) {
      System.out.println("Quit what?");
      return false;
    } else {
      return true;  // signal that we want to quit
    }
  }

  /**
   * Adds INT points to a player's current total.
   */
  private void addINTPoints(String roomName) {
    switch (roomName) {
      case "lab":
      case "theatre":
        INTPoints += multiplier * 100;
        break;
      case "classroom":
        INTPoints += multiplier * 50;
    }
  }
}
