import java.util.*;
import java.io.*;

/**
 * This class is the main class of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.  Users
 * can walk around some scenery. That's all. It should really be extended
 * to make it more interesting!
 * <p>
 * To play this game, create an instance of this class and call the "play"
 * method.
 * <p>
 * This main class creates and initialises all the others: it creates all
 * rooms, creates the parser and starts the game.  It also evaluates and
 * executes the commands that the parser returns.
 *
 * @author Michael Kölling and David J. Barnes
 * @version 2016.02.29
 */

public class Game {

  private Parser parser;
  private Player player;
  private Time time;
  private int turns = 0;
  private float multiplier = 1;
  private int INTPoints = 0;
  private TextReader textReader;
  private ArrayList<Room> roomList = new ArrayList<>();
  private Stack<Room> backStack = new Stack<>();
  private Room theatre, arcade, lab, classroom, pub, library, kitchen, office, hall;
  private Item elaCW, ppaCW;
  private Character ppaLecturer, elaLecturer, cs1Lecturer, fc1Lecturer;
  private boolean computerUsed, elaTeacher, ppaTeacher, cs1Teacher, fc1Teacher, released, finished, ppaHandedIn, elaHandedIn;
  private HashMap<String, Item> allItems = new HashMap<>();
  private ArrayList<String> charList = new ArrayList<>();

  /**
   * Create the game and initialise its internal map.
   */
  public Game() {
    time = new Time();
    player = new Player(3000);
    createRooms();
    parser = new Parser();
  }

  /**
   * Create all the rooms and link their exits together.
   */
  private void createRooms() {
    // create the rooms
    Room outside = new Room("outside", "outside the main entrance of the university");
    library = new Room("library", "in the Maughan Library");
    hall = new Room("hall", "in the exam hall");
    arcade = new Room("arcade", "in the Arcade");
    theatre = new Room("theatre", "in a lecture theatre");
    pub = new Room("pub", "at The Vault");
    kitchen = new Room("kitchen", "in the canteen");
    Room fifth = new Room("fifth", "on the fifth floor of Bush House");
    office = new Room("office", "in the Informatics departmental office");
    classroom = new Room("classroom", "in a classroom");
    Room sixth = new Room("sixth", "on the sixth floor of Bush House");
    lab = new Room("lab", "in a computing lab");
    Room random = new Room("random", "in a teleporter");

    Collections.addAll(roomList, outside, theatre, arcade, lab, hall, office, pub, fifth, sixth, library, kitchen, classroom);

    // initialise room exits
    outside.setExit("north", arcade);
    outside.setExit("east", library);

    library.setExit("west", outside);

    arcade.setExit("north", theatre);
    arcade.setExit("south", outside);
    arcade.setExit("east", pub);
    arcade.setExit("down", kitchen);
    arcade.setExit("up", fifth);

    theatre.setExit("south", arcade);
    theatre.setExit("east", random);

    pub.setExit("west", arcade);
    pub.setExit("south", kitchen);

    kitchen.setExit("up", arcade);
    kitchen.setExit("north", pub);

    fifth.setExit("down", arcade);
    fifth.setExit("up", sixth);
    fifth.setExit("east", classroom);
    fifth.setExit("north", office);

    office.setExit("south", fifth);

    classroom.setExit("west", fifth);

    sixth.setExit("down", fifth);
    sixth.setExit("north", lab);
    sixth.setExit("south", hall);

    hall.setExit("north", sixth);

    lab.setExit("south", sixth);

    createItems();
    createCharacters();

    player.currentRoom = outside;  // start game outside
  }

  /**
   * Creates all the items and places them in their designated room
   */
  private void createItems() {
    Item ppaBook = new Item("ppabook", "your PPA book", 1200);
    Item elaBook = new Item("elabook", "your ELA book", 1000);
    Item coffee = new Item("coffee", "a cup of coffee", 300);
    Item computer = new Item("computer", "a lab computer", 0);
    elaCW = new Item("elaCW", "your completed ELA coursework", 200);
    ppaCW = new Item("ppaCW", "your completed PPA coursework", 200);
    Item notebook = new Item("notebook", "your notebook", 400);
    Item drink = new Item("beer", "a pint of beer", 300);
    Item printer = new Item("printer", "a printer", 0);
    Item backpack = new Item("backpack", "a bigger backpack", 1);
    Item food = new Item("food", "some food", 400);
    Item exam = new Item("exam", "your final exam paper", 200);

    allItems.put(ppaBook.getName(), ppaBook);
    allItems.put(elaBook.getName(), elaBook);
    allItems.put(coffee.getName(), coffee);
    allItems.put(elaCW.getName(), elaCW);
    allItems.put(ppaCW.getName(), ppaCW);
    allItems.put(notebook.getName(), notebook);
    allItems.put(drink.getName(), drink);
    allItems.put(food.getName(), food);
    allItems.put(backpack.getName(), backpack);
    allItems.put(exam.getName(), exam);

    library.addItem(ppaBook);
    library.addItem(elaBook);

    arcade.addItem(coffee);

    lab.addItem(computer);
    lab.addItem(printer);

    pub.addItem(drink);

    hall.addItem(exam);

    kitchen.addItem(food);

    theatre.addItem(backpack);
    theatre.addItem(notebook);
  }

  /**
   * Creates the characters of the game and puts them in the correct place
   */
  private void createCharacters() {
    ppaLecturer = new Character("kolling");
    elaLecturer = new Character("rodrigues");
    cs1Lecturer = new Character("howard");
    fc1Lecturer = new Character("kurucz");

    office.addCharacter(ppaLecturer);
    office.addCharacter(elaLecturer);
    office.addCharacter(cs1Lecturer);
    office.addCharacter(fc1Lecturer);

    Collections.addAll(charList, ppaLecturer.getName(), elaLecturer.getName(), fc1Lecturer.getName(), cs1Lecturer.getName());
  }


  /**
   * Main play routine.  Loops until end of play.
   */
  void play() {

    printWelcome();

    // Enter the main command loop.  Here we repeatedly read commands and
    // execute them until the game is over.

    while (!finished) {
      Command command = parser.getCommand();
      finished = processCommand(command);
      if (finished) {
        break;
      }
      turns += 1;
      if (turns == 220) {
        finished = quit(null);
        if (finished) {
          break;
        }
      }
      moveCharacters(ppaLecturer);
      moveCharacters(elaLecturer);
      moveCharacters(fc1Lecturer);
      moveCharacters(cs1Lecturer);
      released = checkCWRelease();
      finished = checkCWDeadline();
    }
    System.out.println("\nThank you for playing. Good bye.");
  }

  /**
   * Print out the opening message for the player.
   */
  private void printWelcome() {
    textReader = new TextReader(null);
    textReader.reader();
    look();
  }

  /**
   * Given a command, process (that is: execute) the command.
   *
   * @param command The command to be processed.
   * @return true If the command ends the game, false otherwise.
   */
  private boolean processCommand(Command command) {
    boolean wantToQuit;
    String commandWord = command.getCommandWord();
    if (commandWord != null) {
      switch (commandWord) {
        case "help":
          printHelp();
          break;
        case "go":
          goRoom(command);
          break;
        case "quit":
          wantToQuit = quit(command);
          return wantToQuit;
        case "back":
          goBack();
          break;
        case "look":
          look();
          break;
        case "use":
          useItem(command);
          break;
        case "talk":
          playerNPCInteraction(command);
          break;
        case "take":
          player.takeItem(command);
          break;
        case "drop":
          dropItem(command);
          break;
        case "items":
          System.out.println(player.getInventory());
          break;
        case "time":
          String currentTime = time.getTime(turns);
          System.out.println("\nNumber of commands used: " + turns);
          System.out.println(currentTime + "\n");
          break;
        case "timetable":
          showTimetable();
          break;
        case "deadline":
          getDeadline(command);
          break;
      }
    } else {
      System.out.println("I don't know what you mean...");
    }
    return false;
  }

  /**
   * Checks whether each coursework has been released
   */

  private boolean checkCWRelease() {
    if (turns == 50) {
      System.out.println("The PPA coursework has been released.");
      System.out.println("Use the lab computers to complete it before the deadline.");
      System.out.println("To check the deadline, type 'deadline ppa'.");
      return true;
    } else if (turns == 120) {
      System.out.println("The ELA coursework has been released.");
      System.out.println("Use the lab computers to complete it before the deadline.");
      System.out.println("To check the deadline, type 'deadline ela'.");
      return true;
    } else if (turns > 100 && turns < 120) {
      return false;
    } else if (turns < 50) {
      return false;
    } else if (turns > 170) {
      return false;
    }
    return false;
  }

  /**
   * Checks whether the deadlines of each coursework has passed.
   * If it has, then the game ends if coursework has not been handed in.
   */

  private boolean checkCWDeadline() {
    switch (turns) {
      case 100:
        System.out.println("The deadline for the PPA coursework has passed.");
        if (!ppaHandedIn) {
          System.out.println("You failed to hand in your PPA coursework on time.");
          System.out.println("GAME OVER!");
          return true;
        }
      case 170:
        System.out.println("The deadline for the ELA coursework has passed.");
        if (!elaHandedIn) {
          System.out.println("You failed to hand in your ELA coursework on time.");
          System.out.println("GAME OVER!");
          return true;
        }
      default:
        return false;
    }
  }


  /**
   * Moves the character to a particular room dependant of turns
   *
   * @param character The character being moved
   */

  private void moveCharacters(Character character) {
    switch (character.getName()) {
      case "kolling":
        ppaTeacher = true;
        break;
      case "rodrigues":
        elaTeacher = true;
        break;
      case "howard":
        cs1Teacher = true;
        break;
      case "kurucz":
        fc1Teacher = true;
        break;
    }
    switch (time.getTimeIndex(turns)) {
      case 2: case 12: case 8: case 18:
        if (ppaTeacher) {
          character.changeRooms(theatre);
          theatre.addCharacter(character);
        }
        break;
      case 3: case 13:
        if (elaTeacher) {
          character.changeRooms(theatre);
          theatre.addCharacter(character);
        }
        break;
      case 4: case 14:
        if (cs1Teacher) {
          character.changeRooms(theatre);
          theatre.addCharacter(character);
        }
        break;
      case 5: case 15:
        if (cs1Teacher) {
          character.changeRooms(lab);
          lab.addCharacter(character);
        }
        break;
      case 6: case 16:
        if (elaTeacher) {
          character.changeRooms(classroom);
          classroom.addCharacter(character);
        }
        break;
      case 7: case 17:
        if (fc1Teacher) {
          character.changeRooms(classroom);
          classroom.addCharacter(character);
        }
        break;
      case 9: case 19:
        if (fc1Teacher) {
          character.changeRooms(theatre);
          theatre.addCharacter(character);
        }
        break;
      case 11:
        if (fc1Teacher) {
          character.changeRooms(classroom);
          classroom.addCharacter(character);
        }
        break;
      case 10:
        if (ppaTeacher) {
          character.changeRooms(lab);
          lab.addCharacter(character);
        }
        break;
      default:
        roomList.forEach(r -> r.removeCharacter(character));
        character.changeRooms(office);
    }
    ppaTeacher = false;
    elaTeacher = false;
    cs1Teacher = false;
    fc1Teacher = false;
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

  // implementations of user commands:

  /**
   * Print out some help information.
   * Here we print some stupid, cryptic message and a list of the
   * command words.
   */

  private void printHelp() {
    textReader = new TextReader("help");
    textReader.reader();
    parser.showCommands();
  }

  /**
   * Implements the deadline command.
   */

  private void getDeadline(Command command) {
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
   * Removes an item from player's inventory.
   */
  private void dropItem(Command command) {
    String itemName = command.getSecondWord();
    Set<String> playerItemSet = player.inventory.keySet();
    if (!playerItemSet.contains(itemName)) {
      System.out.println("Drop what?");
    } else {
      player.inventory.remove(itemName);
      player.currentRoom.items.put(itemName, allItems.get(itemName));
      System.out.println("Dropped " + itemName);
    }
  }

  /**
   * Checks whether player and character are in the same room
   * If they are, correct interaction occurs
   */
  private void playerNPCInteraction(Command command) {
    String characterName = command.getSecondWord();
    Set<String> playerItemSet = player.inventory.keySet();
    if (characterName == null || !charList.contains(characterName)) {
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
  private void useItem(Command command) {
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
            try {
              FileReader fr = new FileReader("win.txt");
              BufferedReader br = new BufferedReader(fr);
              String line;
              while ((line = br.readLine()) != null) {
                System.out.println(line);
              }
            } catch (Exception e) {
              System.out.println("There was an error trying to open win.txt.");
            }
            float finalGrade = (float)INTPoints/2000;
            if (finalGrade >= 0.7) {
              System.out.println("First");
            }
            else if (finalGrade >= 0.5) {
              System.out.println("2:1");
            }
            else if (finalGrade >= 0.4) {
              System.out.println("2:2");
            }
            else {
              System.out.println("Third");
            }
            finished = true;
            return;
          }
          else if (player.currentRoom != hall) {
            System.out.println("You can't sit your exam here");
          }
          else if (turns < 200) {
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
  private void goBack() {
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
  private void look() {
    System.out.println("\n" + player.currentRoom.getLongDescription());
  }

  /**
   * Try to in to one direction. If there is an exit, enter the new
   * room, otherwise print an error message.
   */
  private void goRoom(Command command) {
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
  private void showTimetable() {
    textReader = new TextReader("timetable");
    textReader.reader();
  }

  /**
   * "Quit" was entered. Check the rest of the command to see
   * whether we really quit the game.
   *
   * @return true, if this command quits the game, false otherwise.
   */
  private boolean quit(Command command) {
    if (command == null) {
      textReader = new TextReader("turn");
      textReader.reader();
      return true;
    } else if (command.hasSecondWord()) {
      System.out.println("Quit what?");
      return false;
    } else {
      return true;  // signal that we want to quit
    }
  }
}
