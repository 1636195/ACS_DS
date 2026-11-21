package baseNoStates.requests;

import baseNoStates.DirectoryDoors;
import baseNoStates.DirectoryUsers;
import baseNoStates.Door;
import baseNoStates.User;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class RequestReader implements Request {
  private final String credential; // who
  private final String action;     // what
  private final LocalDateTime now; // when
  private final String doorId;     // where
  private String userName;
  private boolean authorized;
  private final ArrayList<String> reasons; // why not authorized
  private String doorStateName;
  private Boolean doorClosed;

  public RequestReader(String credential, String action, LocalDateTime now, String doorId) {
    this.credential = credential;
    this.action = action;
    this.doorId = doorId;
    reasons = new ArrayList<>();
    this.now = now;
  }

  public void setDoorStateName(String name) {
    doorStateName = name;
  }

  public String getAction() {
    return action;
  }

  public boolean isAuthorized() {
    return authorized;
  }

  public void addReason(String reason) {
    reasons.add(reason);
  }


  @Override
  public String toString() {
    if (userName == null) {
      userName = "unknown";
    }
    return "Request{"
            + "credential=" + credential
            + ", userName=" + userName
            + ", action=" + action
            + ", now=" + now
            + ", doorID=" + doorId
            + ", closed=" + doorClosed
            + ", authorized=" + authorized
            + ", reasons=" + reasons
            + "}";
  }

  public JSONObject answerToJson() {
    JSONObject json = new JSONObject();
    json.put("authorized", authorized);
    json.put("action", action);
    json.put("doorId", doorId);
    json.put("closed", doorClosed == null ? JSONObject.NULL : doorClosed);
    json.put("state", doorStateName == null ? JSONObject.NULL : doorStateName);
    json.put("reasons", new JSONArray(reasons));
    return json;
  }

  @Override
  public void process() {

    User user = DirectoryUsers.findUserByCredential(credential);
    Door door = DirectoryDoors.findDoorById(doorId);

    // An unknown door cannot process a request.
    if (door == null) {
      authorized = false;
      addReason("Unknown door");
      return;
    }

    // Check the user's permissions.
    authorize(user, door);

    // Execute the action only when authorized.
    door.processRequest(this);

    // Store the resulting physical state.
    doorClosed = door.isClosed();
  }

  /*
   * Determines whether a reader request is authorized.
   *
   * Authorization depends on the user's group,
   * the requested action, the door's connected
   * spaces and the date and time of the request.
   */
  private void authorize(User user, Door door) {

    // Unknown users cannot perform any action.
    if (user == null) {
      authorized = false;
      addReason("Unknown user credential");
      return;
    }

    userName = user.getName();

    // Reject requests without a valid door.
    if (door == null) {
      authorized = false;
      addReason("Unknown door");
      return;
    }

    // Check the permissions assigned to the user's group.
    if (user.getGroup().isAuthorized(action, door, now)) {
      authorized = true;
    } else {
      authorized = false;
      addReason("User not authorized by group rules");
    }
  }
}

