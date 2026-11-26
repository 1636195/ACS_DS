package baseNoStates;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a group of users sharing access permissions.
 * Permissions specify which actions can be performed,
 * in which areas and during which schedule.
 * Users acquire permissions through their group,
 * following the Role-Based Access Control (RBAC) model.
 */
public class UserGroup {

  private final String name;
  private final boolean administrator;
  private Schedule schedule;
  private final Set<String> allowedActions;
  private final List<Area> allowedAreas;

  public UserGroup(String name, boolean administrator) {
    if (name == null || name.trim().isEmpty()) {
      throw new IllegalArgumentException("Group name cannot be empty");
    }
    this.name = name;
    this.administrator = administrator;
    this.allowedActions = new HashSet<>();
    this.allowedAreas = new ArrayList<>();
  }

  public String getName() {
    return name;
  }

  /**
   * Assigns a schedule to the group.
   *
   * @param schedule the schedule
   */
  public void setSchedule(Schedule schedule) {
    this.schedule = schedule;
  }

  /**
   * Grants permission to perform an action.
   *
   * @param action the allowed action
   */
  public void addAllowedAction(String action) {
    if (action == null) {
      throw new IllegalArgumentException("Action cannot be null");
    }
    allowedActions.add(action);
  }

  /**
   * Grants access to a space or partition.
   *
   * @param area the allowed area
   */
  public void addAllowedArea(Area area) {
    if (area == null) {
      throw new IllegalArgumentException("Area cannot be null");
    }
    if (!allowedAreas.contains(area)) {
      allowedAreas.add(area);
    }
  }

  /**
   * Checks if the group has access to an area.
   * Access to a partition includes all its descendant spaces and partitions.
   * It uses the AreaContainsVisitor to perform the check.
   *
   * @param area the area to check
   * @return true if the group can access the area, false otherwise
   */
  public boolean canBeInArea(Area area) {
    if (area == null) {
      return false;
    }
    if (administrator) {
      return true;
    }
    AreaContainsVisitor containsVisitor = new AreaContainsVisitor(area);
    for (Area allowedArea : allowedAreas) {
      if (allowedArea.accept(containsVisitor)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks the four authorization dimensions:
   * who (group), what (action), where (spaces) and when (schedule).
   *
   * @param action   the requested action
   * @param door     the door involved in the request
   * @param dateTime the date and time of the request
   * @return true if the group is authorized, false otherwise
   */
  public boolean isAuthorized(String action, Door door, LocalDateTime dateTime) {
    if (action == null || door == null || dateTime == null) {
      return false;
    }

    // Only actions recognized by the ACS are valid.
    boolean validAction = Actions.OPEN.equals(action)
        || Actions.CLOSE.equals(action)
        || Actions.LOCK.equals(action)
        || Actions.UNLOCK.equals(action)
        || Actions.UNLOCK_SHORTLY.equals(action);
    if (!validAction) {
      return false;
    }

    Space origin = door.getOriginSpace();
    Space destination = door.getDestinationSpace();

    // A door must be connected to two valid spaces.
    if (origin == null || destination == null) {
      return false;
    }

    // Administrators have unrestricted permissions.
    if (administrator) {
      return true;
    }

    // Groups without a schedule have no active permissions.
    if (schedule == null || !schedule.isInSchedule(dateTime)) {
      return false;
    }

    // Check whether the requested action is allowed.
    if (!allowedActions.contains(action)) {
      return false;
    }

    // Both the origin and destination must be accessible.
    return canBeInArea(origin) && canBeInArea(destination);
  }
}