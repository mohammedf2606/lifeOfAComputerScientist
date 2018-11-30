/**
 * Class Game - the part that initailises the game and contains the method that runs the code
 *
 * This class creates and initialises all the others: it creates all
 * rooms, creates the parser and starts the game.  It also evaluates
 * the commands that the parser returns.
 *
 * @author Michael Kölling, David J. Barnes and Fahim Mohammed
 * @version 2018.11.28
 */

public class Game extends Creator {

  private boolean elaTeacher, ppaTeacher, cs1Teacher, fc1Teacher, releasedOnce, deadlineOnce;

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
    System.out.print('\u000C');
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
          player.dropItem(command, allItems);
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
    if (turns >= 50 && !releasedOnce) {
      System.out.println("\nThe PPA coursework has been released.");
      System.out.println("Use the lab computers to complete it before the deadline.");
      System.out.println("To check the deadline, type 'deadline ppa'.\n");
      releasedOnce = true;
      return true;
    } else if (turns >= 120 && !releasedOnce) {
      System.out.println("\nThe ELA coursework has been released.");
      System.out.println("Use the lab computers to complete it before the deadline.");
      System.out.println("To check the deadline, type 'deadline ela'.\n");
      releasedOnce = true;
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
    if (turns >= 100 && !deadlineOnce) {
      System.out.println("\nThe deadline for the PPA coursework has passed.");
      if (!ppaHandedIn) {
        System.out.println("You failed to hand in your PPA coursework on time.");
        System.out.println("GAME OVER!");
        return true;
      }
      deadlineOnce = true;
    }
    else if (turns >= 170 && !deadlineOnce) {
      System.out.println("\nThe deadline for the ELA coursework has passed.");
      if (!elaHandedIn) {
        System.out.println("You failed to hand in your ELA coursework on time.");
        System.out.println("GAME OVER!");
        return true;
      }
      deadlineOnce = true;
    }
    return false;
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
      case 2: case 12:
        if (ppaTeacher) {
          character.changeRooms(theatre);
          theatre.addCharacter(character);
        } else {
          goToOffice = true;
        }
        break;
      case 3: case 13:
        if (elaTeacher) {
          character.changeRooms(theatre);
          theatre.addCharacter(character);
        } else {
          goToOffice = true;
        }
        break;
      case 4: case 14:
        if (cs1Teacher) {
          character.changeRooms(lab);
          lab.addCharacter(character);
        } else {
          goToOffice = true;
        }
        break;
      case 6: case 16:
        if (elaTeacher) {
          character.changeRooms(classroom);
          classroom.addCharacter(character);
        } else {
          goToOffice = true;
        }
        break;
      case 7: case 17:
        if (fc1Teacher) {
          character.changeRooms(classroom);
          classroom.addCharacter(character);
        } else {
          goToOffice = true;
        }
        break;
      case 8: case 18:
        if (cs1Teacher) {
          character.changeRooms(theatre);
          theatre.addCharacter(character);
        } else {
          goToOffice = true;
        }
        break;
      case 9: case 19:
        if (fc1Teacher) {
          character.changeRooms(theatre);
          theatre.addCharacter(character);
        } else {
          goToOffice = true;
        }
        break;
      case 11:
        if (fc1Teacher) {
          character.changeRooms(theatre);
          theatre.addCharacter(character);
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
      default:
        goToOffice = true;
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
