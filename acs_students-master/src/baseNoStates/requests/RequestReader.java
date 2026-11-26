package baseNoStates.requests;

import baseNoStates.DirectoryDoors;
import baseNoStates.DirectoryUsers;
import baseNoStates.Door;
import baseNoStates.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Represents a request to perform an action on a specific door.
 * The request stores the four Ws: who (credential), what (action), when (date and time)
 * and where (door identifier).
 */
public class RequestReader implements Request {

  private static final Logger logger = LoggerFactory.getLogger(RequestReader.class);

  private final String credential; // who
  private final String action; // what
  private final LocalDateTime now; // when
  private final String doorId; // where
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

  @Override
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
    // Retrieve users and doors from their respective Singleton instances.
    User user = DirectoryUsers.getInstance().findUserByCredential(credential);
    Door door = DirectoryDoors.getInstance().findDoorById(doorId);

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

    if (authorized) {
      logger.info("User {} authorized for action {} on door {}", credential, action, doorId);
    } else {
      logger.warn("User {} NOT authorized for action {} on door {}. Reasons: {}",
          credential, action, doorId, reasons);
    }
  }

  /*
   * Determines whether a reader request is authorized.
   *
   * @param user the user making the request
   * @param door the door involved in the request
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