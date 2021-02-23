package student.adventure;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class GameEngine {

  private SurroundingMap buildings;
  private Building currentBuilding;
  private String currentBuildingName;
  private BufferedReader br;
  private List<String> inventory;
  private List<String> buildingHistory;
  private int ID;
  private String name;
  private int score = 0;

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public GameEngine(int ID) {
    //Loading the JSON map.
    Gson gson = new Gson();
    try {
      br = new BufferedReader(new FileReader
              ("C:\\Users\\varsh\\IdeaProjects\\api-adventures-varshit007\\src\\main\\resources\\Buildings.json"));

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    //Initializing few private variables.
    this.ID = ID;
    buildings = gson.fromJson(br, SurroundingMap.class);
    inventory = new ArrayList<>();
    currentBuilding = buildings.getBuildings().get(0);
    currentBuildingName = currentBuilding.getName();
    buildingHistory = new ArrayList<>();
    buildingHistory.add(currentBuildingName);
  }

  public int getID() {
    return this.ID;
  }

  public Building getCurrentBuilding() {
    return this.currentBuilding;
  }

  //This methods returns a list of all possible directions you can go from a building.
  public List<String> possibleDirections() {
    return buildings.possibleDirections(currentBuilding.getName());
  }

  public List<String> getInventory() {
    return inventory;
  }

  public List<String> getBuildingHistory() {
    return buildingHistory;
  }

  /**
   * This is the brain of the game engine.
   * Depending on the command passed in, it will call different helper methods.
   * @param command - The command to be executed.
   * @return a string giving a message on whether the command was successful or not.
   */
  public String playWeb(String command) {
    String input = command;
    input = input.trim().toLowerCase();

    String[] inputAsArray = command.split(" ");

    if (inputAsArray[0].equals("go")) {
      String tempCurrentBldngName = currentBuilding.getName();
      move(inputAsArray[1]);
      //if after moving you're at the same building, then you've not entered the right direction.
      if (tempCurrentBldngName.equals(currentBuilding.getName())) {
        return "Please enter a valid direction.";
      }
      return examine();

    } else if (inputAsArray[0] == "examine") {
      return examine();

    } else if (inputAsArray[0].equals("take")) {
      return take(inputAsArray[1]);

    } else if (inputAsArray[0].equals("drop")) {
      return drop(inputAsArray[1]);

    } else if (inputAsArray[0].equals("quit")) {
      return "You quit the game.";

    } else if (inputAsArray[0] == "history") {
      return buildingHistory.toString();

    } else {
      return "Please enter a valid input.";
    }
  }

  public String examine() {
    String returnString = "You are at: " + currentBuilding.getName() + "\nYou can go: " +
            buildings.possibleDirections(currentBuilding.getName());
    if (currentBuilding.getItem().size() != 0) {
      returnString += "\nItems available: " + currentBuilding.getItem();
    }
    returnString += "\nYour building history is: " + buildingHistory.toString();
    return returnString;
  }

  //Returns the item taken if any.
  public String take(String itemToTake) {
    if (currentBuilding.getItem().contains(itemToTake)) {
      currentBuilding.getItem().remove(itemToTake);
      inventory.add(itemToTake);
      return "Item taken: " + itemToTake;
    } else {
      return "There is no such item.";
    }
  }

  //Returns whats item is dropped if any.
  public String drop(String itemDropped) {
    if (inventory.contains(itemDropped) && !(currentBuilding.getItem().contains(itemDropped))) {
      currentBuilding.getItem().add(itemDropped);
      inventory.remove(itemDropped);
      return "Item dropped: " + itemDropped;
    } else {
      return "You cannot drop this item.";
    }
  }

  //Moves in the direction of the argument.
  public Building move(String direction) {
    if (direction.equals("north")) {
      currentBuildingName = currentBuilding.getDirections().getNorth();
    } else if (direction.equals("east")) {
      currentBuildingName = currentBuilding.getDirections().getEast();
    } else if (direction.equals("west")) {
      currentBuildingName = currentBuilding.getDirections().getWest();
    } else if (direction.equals("south")) {
      currentBuildingName = currentBuilding.getDirections().getSouth();
    }

    for (Building bldng : buildings.getBuildings()) {
      if (bldng.getName().equals(currentBuildingName)) {
        currentBuilding = bldng;
        score++;
        buildingHistory.add(currentBuilding.getName());
        return currentBuilding;
      }
    }
    return currentBuilding;
  }
}
