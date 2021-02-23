package student.server;

import student.adventure.Game;
import student.adventure.GameEngine;

import java.sql.*;
import java.util.*;

public class AdventureGameService implements AdventureService {

  private List<GameEngine> gamesList = new ArrayList<>();
  private final static String DATABASE_URL = "jdbc:sqlite:src/main/resources/adventure.db";
  private Connection dbConnection = null;

  @Override
  public void reset() {
    gamesList.clear();

    //Trying to delete the leaderboard.
    PreparedStatement ps = null;
    try {
      String sql = "DELETE FROM leaderboard_va23";
      ps = dbConnection.prepareStatement(sql);
    } catch(SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public int newGame() throws AdventureException {
    Random rand = new Random();
    int ID = rand.nextInt(1000);

    //Check if there already exists a game with the same ID.
    boolean flag = true;
    for (GameEngine games : gamesList) {
      if (games.getID() == ID) {
        flag = false;
      }
    }
    GameEngine game;
    if (flag) {
      game = new GameEngine(ID);
    } else {
      game = new GameEngine(rand.nextInt(1000));
    }

    gamesList.add(game);
    return game.getID();
  }

  @Override
  public GameStatus getGame(int id) {
    GameEngine game = null;
    for (GameEngine games : gamesList) {
      if (games.getID() == id) {
        game = games;
      }
    }

    Map<String, List<String>> commandOptions = new HashMap<>();

    List<String> goList = new ArrayList<>();
    goList = game.possibleDirections();
    commandOptions.put("go",goList);

    List<String> takeList = new ArrayList<>();
    takeList = game.getCurrentBuilding().getItem();
    commandOptions.put("take", takeList);

    List<String> dropList = new ArrayList<>();
    dropList = game.getInventory();
    commandOptions.put("drop", dropList);

//    List<String> bldngHistory = new ArrayList<>();
//    bldngHistory = game.getBuildingHistory();
//    commandOptions.put("history", bldngHistory);

    List<String> examineList = new ArrayList<>();
    examineList.add("");
    commandOptions.put("examine", examineList);

    commandOptions.put("quit", new ArrayList<>());

    GameStatus status = new GameStatus(false,game.getID(),game.examine(),game.getCurrentBuilding().getImage(),
            game.getCurrentBuilding().getVideo(), new AdventureState(game),commandOptions);
    return status;
  }

  @Override
  public boolean destroyGame(int id) {
    for (GameEngine games : gamesList) {
      if (games.getID() == id) {
        gamesList.remove(games);
        return true;
      }
    }
    return false;
  }

  @Override
  public void executeCommand(int id, Command command) {
    GameEngine game = null;
    for (GameEngine games : gamesList) {
      if (games.getID() == id) {
        game = games;
      }
    }
    game.setName(command.getPlayerName());
    String inputToGameEngine = command.getCommandName() + " " + command.getCommandValue();
    game.playWeb(inputToGameEngine);

    //If the user enters the gym(the final destination), then update the leaderboard.
    if (game.getCurrentBuilding().getName().equals("Gym")) {
      try {
        dbConnection = DriverManager.getConnection(DATABASE_URL);
      } catch(Exception e) {
        e.printStackTrace();
      }
      PreparedStatement ps = null;
      try {
        String sql = "INSERT INTO leaderboard_va23(game.getName(), game.getScore) VALUES(?,?)";
        ps = dbConnection.prepareStatement(sql);
        ps.setString(1,game.getName());
        ps.setInt(2,game.getScore());
        ps.execute();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public Map<String, Integer> fetchLeaderboard() {
    String name = null;
    int score = 0;

    //Trying to retrieve data from the leaderboard table.
    try {
      dbConnection = DriverManager.getConnection(DATABASE_URL);
    } catch(Exception e) {
      e.printStackTrace();
    }
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
      String sql = "SELECT * FROM leaderboard_va23";
      ps = dbConnection.prepareStatement(sql);
      rs = ps.executeQuery();
      while(rs.next()) {
        name = rs.getString("name");
        score = rs.getInt("score");
      }
    } catch(SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        rs.close();
        ps.close();
        //dbConnection.close();
      } catch(SQLException e) {
        e.printStackTrace();
      }
    }
    Map<String,Integer> leaderboard = new HashMap<>();
    leaderboard.put(name,score);
    return leaderboard;
  }
}
