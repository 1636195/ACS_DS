package baseNoStates.requests;

import baseNoStates.Actions;
import baseNoStates.Area;
import baseNoStates.DirectoryAreas;
import baseNoStates.Door;
import baseNoStates.DoorCollectorVisitor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a request that affects all doors in a given area.
 * Only lock and unlock actions are valid for area requests.
 */
public class RequestArea implements Request {

  private static final Logger logger = LoggerFactory.getLogger(RequestArea.class);

  private final String credential;
  private final String action;
  private final String areaId;
  private final LocalDateTime now;
  private String errorReason;
  private ArrayList<RequestReader> requests = new ArrayList<>();

  public RequestArea(String credential, String action, LocalDateTime now, String areaId) {
    this.credential = credential;
    this.areaId = areaId;
    this.action = action;
    this.now = now;
  }

  public String getAction() {
    return action;
  }

  @Override
  public JSONObject answerToJson() {
    JSONObject json = new JSONObject();
    json.put("action", action);
    json.put("areaId", areaId);
    JSONArray jsonRequests = new JSONArray();
    for (RequestReader rd : requests) {
      jsonRequests.put(rd.answerToJson());
    }
    json.put("requestsDoors", jsonRequests);
    if (errorReason != null) {
      json.put("reasons", new JSONArray().put(errorReason));
    }
    return json;
  }

  @Override
  public String toString() {
    String requestsDoorsStr;
    if (requests.size() == 0) {
      requestsDoorsStr = "";
    } else {
      requestsDoorsStr = requests.toString();
    }
    return "Request{"
        + "credential=" + credential
        + ", action=" + action
        + ", now=" + now
        + ", areaId=" + areaId
        + ", requestsDoors=" + requestsDoorsStr
        + '}';
  }

  @Override
  public void process() {
    requests.clear();
    errorReason = null;

    // Area requests only support lock and unlock.
    if (!Actions.LOCK.equals(action) && !Actions.UNLOCK.equals(action)) {
      errorReason = "Invalid action for area: " + action;
      return;
    }

    // Find the requested space or partition using the Singleton instance.
    Area area = DirectoryAreas.getInstance().findAreaById(areaId);
    if (area == null) {
      errorReason = "Unknown area: " + areaId;
      logger.error(errorReason);
      return;
    }

    // Use the DoorCollectorVisitor to get all doors in the area.
    List<Door> doors = area.accept(new DoorCollectorVisitor());

    // Process one reader request for each access door.
    for (Door door : doors) {
      RequestReader requestReader = new RequestReader(
          credential,
          action,
          now,
          door.getId()
      );
      requestReader.process();
      requests.add(requestReader);
    }
  }
}