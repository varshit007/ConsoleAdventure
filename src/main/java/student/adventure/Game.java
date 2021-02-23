package student.adventure;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Game {
  private SurroundingMap buildings;
  private Building currentBuilding;
  private String currentBuildingName;
  private BufferedReader br;
  private List<String> inventory;
  private List<String> buildingHistory;
  private String userInput;
  private int ID;

  public Game(int ID) {
    //Loading the JSON map.
    Gson gson = new Gson();
    try {
      br = new BufferedReader(new FileReader
              ("C:\\Users\\varsh\\IdeaProjects\\api-adventures-varshit007\\src\\main\\resources\\Buildings.json"));

    } catch(FileNotFoundException e) {
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

  public void playCLI() {
    examine();
    while(!(currentBuilding.getName().equals("Gym"))) {
      //Keeps track of visited buildings history.
      if (!(buildingHistory.get(buildingHistory.size()-1).equals(currentBuilding.getName()))) {
        buildingHistory.add(currentBuilding.getName());
      }

      userInput = UserInterface.getUserInput();

      if (userInput.contains("examine")) {
        examine();
      } else if (userInput.contains("history")) {
        System.out.println(buildingHistory);
        System.out.print("> ");
      } else if (userInput.contains("take")) {
        take(userInput);
        System.out.print("> ");
      } else if (userInput.contains("drop")) {
        drop(userInput);
        System.out.print("> ");
      } else if (userInput.contains("go")) {
        move(userInput);
        if ((currentBuilding.getName().equals("Gym"))) {
          System.out.println("You are currently in: " + currentBuilding.getName());
          System.out.println("You reached your destination!");
          break;
        }
        examine();
      } else if (userInput.contains("quit")) {
        break;
      } else {
        System.out.println("Please enter a valid input");
        System.out.print("> ");
      }
    }
  }

  private void examine() {
    System.out.println("You are currently in: " + currentBuilding.getName());
    System.out.println("You can go: " + buildings.possibleDirections(currentBuildingName));
    if (currentBuilding.getItem().size() != 0) {
      System.out.println("Items available: " + currentBuilding.getItem());
    }
    System.out.print("> ");
  }

  private void take(String userInput) {
    String[] arrayUserInput = userInput.split(" ");
    String item = arrayUserInput[arrayUserInput.length - 1];

    if (currentBuilding.getItem().contains(item)) {
      currentBuilding.getItem().remove(item);
      inventory.add(item);
    } else {
      System.out.println("There is no such item.");
    }
  }

  private void drop(String userInput) {
    String[] arrayUserInput = userInput.split(" ");
    String item = arrayUserInput[arrayUserInput.length - 1];

    if (inventory.contains(item) && !(currentBuilding.getItem().contains(item))) {
      currentBuilding.getItem().add(item);
      inventory.remove(item);
    } else {
      System.out.println("You cannot drop this item.");
    }
  }

  private void move(String userInput) {
    if (userInput.contains("north")) {
      currentBuildingName = currentBuilding.getDirections().getNorth();
    } else if (userInput.contains("east")) {
      currentBuildingName = currentBuilding.getDirections().getEast();
    } else if (userInput.contains("west")) {
      currentBuildingName = currentBuilding.getDirections().getWest();
    } else if (userInput.contains("south")) {
      currentBuildingName = currentBuilding.getDirections().getSouth();
    } else {
      System.out.println("Please enter a valid direction.");
    }
    if (currentBuildingName != null && currentBuildingName.equals("NA")) {
      System.out.println("Please enter a valid direction");
    }
    for (Building bldngs : buildings.getBuildings()) {
      if (bldngs.getName().equals(currentBuildingName)) {
        currentBuilding = bldngs;
        break;
      }
    }
  }
}
