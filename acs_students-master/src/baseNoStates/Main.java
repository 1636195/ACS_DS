package baseNoStates;

// Before executing enable assertions:
// https://se-education.org/guides/tutorials/intellijUsefulSettings.html
public class Main {

  public static void main(String[] args) {
    // Initialize the singletons
    DirectoryDoors.getInstance().makeDoors();
    DirectoryAreas.getInstance().makeAreas();
    DirectoryUsers.getInstance().makeUsers();

    new WebServer();
  }
}