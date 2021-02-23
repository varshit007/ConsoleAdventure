package student.server;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import student.adventure.Game;
import student.adventure.GameEngine;

import java.util.List;

/**
 * A class to represent values in a game state.
 *
 * Note: these fields should be JSON-serializable values, like Strings, ints, floats, doubles, etc.
 * Please don't nest objects, as the frontend won't know how to display them.
 *
 * Good example:
 * private String shoppingList;
 *
 * Bad example:
 * private ShoppingList shoppingList;
 */
@JsonSerialize
public class AdventureState {
    // TODO: Add any additional state your game needs to this object.
    // E.g.: If your game needs to display a life total, you could add:
    // private int lifeTotal;
    // ...and whatever constructor/getters/setters you'd need
  private String inventory;
  private String possibleDirections;
  private String currentBuildingName;

  public AdventureState(GameEngine game) {
    inventory = game.getInventory().toString();
    possibleDirections = game.possibleDirections().toString();
    currentBuildingName = game.getCurrentBuilding().getName();
  }

  public String getInventory() {
    return inventory;
  }

  public String getPossibleDirections() {
    return possibleDirections;
  }

  public String getCurrentBuildingName() {
    return currentBuildingName;
  }
}
