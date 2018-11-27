import java.util.*;

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

public class Game extends Actions {
  
  private Room theatre, arcade, lab, classroom, pub, library, kitchen;
  private boolean elaTeacher, ppaTeacher, cs1Teacher, fc1Teacher;
  
  /**
   * Create the game and initialise its internal map.
   */
  public Game() {
    time = new Time();
    player = new Player(3000);
    parser = new Parser();
    createRooms();
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
    Character ppaLecturer = new Character("kolling");
    Character elaLecturer = new Character("rodrigues");
    Character cs1Lecturer = new Character("howard");
    Character fc1Lecturer = new Character("kurucz");

    office.addCharacter(ppaLecturer);
    office.addCharacter(elaLecturer);
    office.addCharacter(cs1Lecturer);
    office.addCharacter(fc1Lecturer);

    Collections.addAll(charList, ppaLecturer, elaLecturer, fc1Lecturer, cs1Lecturer);
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
      charList.forEach(c -> roomList.forEach(r -> r.removeCharacter(c)));
      charList.forEach(this::moveCharacters);
      released = checkCWRelease();
      finished = checkCWDeadline();
    }
    System.out.println("\nThank you for playing. Good bye.");
  }

  /**
   * Print out the opening message for the player.
   */
  private void printWelcome() {
    TextReader textReader = new TextReader(null);
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
    return true;
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
    boolean goToOffice = false;
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
      case 2:
      case 12:
      case 8:
      case 18:
        if (ppaTeacher) {
          character.changeRooms(theatre);
          theatre.addCharacter(character);
        } else {
          goToOffice = true;
        }
        break;
      case 3:
      case 13:
        if (elaTeacher) {
          character.changeRooms(theatre);
          theatre.addCharacter(character);
        } else {
          goToOffice = true;
        }
        break;
      case 4:
      case 14:
        if (cs1Teacher) {
          character.changeRooms(theatre);
          theatre.addCharacter(character);
        } else {
          goToOffice = true;
        }
        break;
      case 5:
      case 15:
        if (cs1Teacher) {
          character.changeRooms(lab);
          lab.addCharacter(character);
        } else {
          goToOffice = true;
        }
        break;
      case 6:
      case 16:
        if (elaTeacher) {
          character.changeRooms(classroom);
          classroom.addCharacter(character);
        } else {
          goToOffice = true;
        }
        break;
      case 7:
      case 17:
        if (fc1Teacher) {
          character.changeRooms(classroom);
          classroom.addCharacter(character);
        } else {
          goToOffice = true;
        }
        break;
      case 9:
      case 19:
        if (fc1Teacher) {
          character.changeRooms(theatre);
          theatre.addCharacter(character);
        } else {
          goToOffice = true;
        }
        break;
      case 11:
        if (fc1Teacher) {
          character.changeRooms(classroom);
          classroom.addCharacter(character);
        } else {
          goToOffice = true;
        }
        break;
      case 10:
        if (ppaTeacher) {
          character.changeRooms(lab);
          lab.addCharacter(character);
        } else {
          goToOffice = true;
        }
        break;
    }
    if (goToOffice) {
      character.changeRooms(office);
      office.addCharacter(character);
    }
    ppaTeacher = false;
    elaTeacher = false;
    cs1Teacher = false;
    fc1Teacher = false;
  }
}

