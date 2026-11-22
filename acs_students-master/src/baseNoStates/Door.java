package baseNoStates;

import baseNoStates.requests.RequestReader;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Represents a physical door in the access control system.
 * The door stores its physical condition (open or closed)
 * and delegates its behavior to the current DoorState.
 * This design follows the State pattern, allowing new
 * door states to be added without modifying the logic
 * that processes each action.
 */
public class Door {
  private static final Logger logger = LoggerFactory.getLogger(Door.class);

  private final String id;
  private boolean closed;
  private DoorState state;

  // Spaces connected by this door.
  private Space originSpace;
  private Space destinationSpace;

  public Door(String id) {
    this.id = id;
    this.closed = true;
    this.state = new DoorUnlock(this);
  }

  public synchronized void processRequest(RequestReader request) {
    if (request.isAuthorized()) {
      state.execute(request.getAction());
    } else {
      logger.warn("Request not authorized for door {}", id);
    }
    request.setDoorStateName(getStateName());
  }

  public synchronized boolean isClosed() {
    return closed;
  }

  public synchronized void setClosed(boolean closed) {
    this.closed = closed;
  }

  public String getId() {
    return id;
  }

  /*
   * Connects the door to its origin and destination.
   * The door is registered only in the destination space
   * because it provides access to that space.
   */
  public void connectSpaces(Space origin, Space destination) {
    if (origin == null || destination == null) {
      throw new IllegalArgumentException("Both spaces must be valid");
    }
    if (originSpace != null || destinationSpace != null) {
      throw new IllegalStateException("Door " + id + " is already connected");
    }
    this.originSpace = origin;
    this.destinationSpace = destination;
    destination.addDoorGivingAccess(this);
  }

  public Space getOriginSpace() {
    return originSpace;
  }

  public Space getDestinationSpace() {
    return destinationSpace;
  }

  public synchronized void setState(DoorState newState) {
    if (newState == null) {
      throw new IllegalArgumentException("The new state cannot be null");
    }
    if (state == newState) {
      return;
    }
    // Remove the previous temporary state from the clock.
    if (state instanceof DoorUnlockShortly) {
      ((DoorUnlockShortly) state).stopObserving();
    }
    this.state = newState;
    // Register the new temporary state with the clock.
    if (state instanceof DoorUnlockShortly) {
      ((DoorUnlockShortly) state).startObserving();
    }
  }

  public synchronized boolean isCurrentState(DoorState candidate) {
    return state == candidate;
  }

  public synchronized String getStateName() {
    return state.getName();
  }

  @Override
  public String toString() {
    return "Door{" +
        "id='" + id + '\'' +
        ", closed=" + closed +
        ", state=" + getStateName() +
        '}';
  }

  public synchronized JSONObject toJson() {
    JSONObject json = new JSONObject();
    json.put("id", id);
    json.put("state", getStateName());
    json.put("closed", closed);
    // Include the connected spaces once assigned.
    if (originSpace != null && destinationSpace != null) {
      json.put("originSpace", originSpace.getId());
      json.put("destinationSpace", destinationSpace.getId());
    }
    return json;
  }
}