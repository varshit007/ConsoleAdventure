package student.adventure;

import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import static org.junit.Assert.assertEquals;

public class AdventureTest {
  private Gson gson;
  private BufferedReader br;

  @Before
  public void setUp() {
    gson = new Gson();
    try {
      br = new BufferedReader(new FileReader
              ("C:\\Users\\varsh\\IdeaProjects\\api-adventures-varshit007\\src\\main\\resources\\Buildings.json"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  @Test(expected = Exception.class)
  public void testEmptyJSONFile() {
    try {
      br = new BufferedReader(new FileReader
              ("C:\\Users\\varsh\\IdeaProjects\\api-adventures-varshit007\\src\\main\\resources\\BuildingsBrokenEmpty"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    SurroundingMap buildings = gson.fromJson(br, SurroundingMap.class);
    //There are no buildings, therefore null pointer exception will be produced.
    Assert.assertEquals(buildings.getBuildings().get(0),null);
  }

  @Test
  public void testNullFieldsJSONFile() {
    try {
      br = new BufferedReader(new FileReader
              ("C:\\Users\\varsh\\IdeaProjects\\api-adventures-varshit007\\src\\main\\resources\\BuildingsBrokenNull"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    SurroundingMap buildings = gson.fromJson(br, SurroundingMap.class);
    //The 2nd building has no name field (and therefore is null).
    Assert.assertEquals(buildings.getBuildings().get(1).getName(), null);
  }

  @Test
  public void testValidTake() {
    SurroundingMap buildings = gson.fromJson(br, SurroundingMap.class);
    GameEngine game = new GameEngine(0);
    //The 1st building has food.
    String itemTaken = game.take("food");
    Assert.assertEquals("Item taken: food", itemTaken);
  }

  @Test
  public void testInvalidTake() {
    SurroundingMap buildings = gson.fromJson(br, SurroundingMap.class);
    GameEngine game = new GameEngine(0);
    //The 1st building only food, so you cannot take water.
    String itemTaken = game.take("water");
    Assert.assertEquals("There is no such item.", itemTaken);
  }

  @Test
  public void testValidDrop() {
    SurroundingMap buildings = gson.fromJson(br, SurroundingMap.class);
    GameEngine game = new GameEngine(0);
    //The 1st building has food.
    String itemTaken = game.take("food");
    //Now the inventory has food, move to the next building to drop the food.
    game.move("north");
    String itemDropped = game.drop("food");
    Assert.assertEquals("Item dropped: food", itemDropped);
  }

  @Test
  public void testInvalidDropAlreadyExists() {
    SurroundingMap buildings = gson.fromJson(br, SurroundingMap.class);
    GameEngine game = new GameEngine(0);
    //The 1st building has food.
    String itemTaken = game.take("food");
    //Now the inventory has food, move to the next building.
    game.move("north");
    //This current building already has a coin.
    String itemDropped = game.drop("coin");
    Assert.assertEquals("You cannot drop this item.", itemDropped);
  }

  @Test
  public void testInvalidDropNotInInventory() {
    SurroundingMap buildings = gson.fromJson(br, SurroundingMap.class);
    GameEngine game = new GameEngine(0);
    //The 1st building has food.
    String itemTaken = game.take("food");
    //Now the inventory has food, move to the next building.
    game.move("north");
    //Try to drop some water, but it's not in the inventory.
    String itemDropped = game.drop("water");
    Assert.assertEquals("You cannot drop this item.", itemDropped);
  }

  @Test
  public void testValidMoveNorth() {
    SurroundingMap buildings = gson.fromJson(br, SurroundingMap.class);
    GameEngine game = new GameEngine(0);  //Current building is ISR Dining Hall
    game.move("north"); //Current building should be Daniels Hall
    Assert.assertEquals("Daniels Hall", game.getCurrentBuilding().getName());
  }

  @Test
  public void testValidMoveWest() {   //Similar tests for testMoveEast, and testMoveSouth
    SurroundingMap buildings = gson.fromJson(br, SurroundingMap.class);
    GameEngine game = new GameEngine(0);  //Current building is ISR Dining Hall
    game.move("west"); //Current building should be Coffee Shop
    Assert.assertEquals("Coffee Shop", game.getCurrentBuilding().getName());
  }

  @Test
  public void testInvalidMoveNorth() {
    SurroundingMap buildings = gson.fromJson(br, SurroundingMap.class);
    GameEngine game = new GameEngine(0);  //Current building is ISR Dining Hall
    game.move("north"); //Current building is Daniels Hall.
    //You cannot move north from Daniels Hall, so if you try to move north, you're still in the same building.
    game.move("north");
    Assert.assertEquals("Daniels Hall", game.getCurrentBuilding().getName());
  }

  @Test
  public void testInvalidMoveWest() {   //Similar tests for testInvalidMoveEast, and testInvalidMoveSouth
    SurroundingMap buildings = gson.fromJson(br, SurroundingMap.class);
    GameEngine game = new GameEngine(0);  //Current building is ISR Dining Hall
    game.move("west"); //Current building should be Coffee Shop
    Assert.assertEquals("Coffee Shop", game.getCurrentBuilding().getName());
  }

  @Test
  public void testMinimumScore() {
    SurroundingMap buildings = gson.fromJson(br, SurroundingMap.class);
    GameEngine game = new GameEngine(0);  //Current building is ISR Dining Hall
    game.move("east"); //Now you're in Townsend hall.
    game.move("south"); //Now you've reached the final destination, the gym.
    int numOfMoves = 2;  //Number of moves you took to reach the gym.
    Assert.assertEquals(numOfMoves, game.getScore());
  }

  @Test
  public void testInvalidInputToGameEngine() {
    SurroundingMap buildings = gson.fromJson(br, SurroundingMap.class);
    GameEngine game = new GameEngine(0);
    Assert.assertEquals("Please enter a valid input." ,game.playWeb("fly airplane"));
  }
}