package student.adventure;

import java.util.ArrayList;
import java.util.List;

public class SurroundingMap {
  private List<Building> buildings = null;

  public List<Building> getBuildings() {
    return buildings;
  }

  /**
   * This methods returns a list of all possible directions you can go from a building.
   * @param bld - The current building you are at.
   * @return a list of directions that you can go in.
   */
  public List<String> possibleDirections(String bld) {
    List<String> possibleDirections = new ArrayList<>();
    Building building = null;
    for (Building bldng : buildings) {
      if (bldng.getName().equals(bld)) {
        building = bldng;
        break;
      }
    }
    if (!(building.getDirections().getNorth().equals("NA"))) {
      possibleDirections.add("north");
    }
    if (!(building.getDirections().getEast().equals("NA"))) {
      possibleDirections.add("east");
    }
    if (!(building.getDirections().getWest().equals("NA"))) {
      possibleDirections.add("west");
    }
    if (!(building.getDirections().getSouth().equals("NA"))) {
      possibleDirections.add("south");
    }
    return possibleDirections;
  }
}
