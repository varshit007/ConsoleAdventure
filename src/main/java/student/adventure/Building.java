package student.adventure;

import java.util.List;

public class Building {
  private String name;
  private List<String> item = null;
  private Directions directions;
  private String video;
  private String image;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getItem() {
    return item;
  }

  public void setItem(List<String> item) {
    this.item = item;
  }

  public Directions getDirections() {
    return directions;
  }

  public void setDirections(Directions directions) {
    this.directions = directions;
  }

  public String getVideo() {
    return video;
  }

  public void setVideo(String video) {
    this.video = video;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

}
