package student.adventure;

import java.util.Scanner;

public class UserInterface {

  public static Scanner scan = new Scanner(System.in);
  public static String userInput = "";

  public static String getUserInput() {
    userInput = scan.nextLine();
    userInput = userInput.trim().toLowerCase();
    return userInput;
  }
}
