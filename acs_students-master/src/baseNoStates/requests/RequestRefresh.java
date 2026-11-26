package baseNoStates.requests;

import baseNoStates.DirectoryDoors;
import baseNoStates.Door;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

/*
 * Represents a request to refresh the state of all doors.
 * This request is used by the simulator to repaint the doors and readers.
 */
public class RequestRefresh implements Request {

  private final ArrayList<JSONObject> jsonsDoors = new ArrayList<>();

  @Override
  public JSONObject answerToJson() {
    JSONObject json = new JSONObject();
    json.put("doors", new JSONArray(jsonsDoors));
    return json;
  }

  @Override
  public String toString() {
    return "RequestRefresh{" + jsonsDoors + "}";
  }

  @Override
  public void process() {
    // Retrieve all doors from the Singleton instance.
    for (Door door : DirectoryDoors.getInstance().getAllDoors()) {
      jsonsDoors.add(door.toJson());
    }
  }
}