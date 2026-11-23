package baseNoStates;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an individual physical space in a building.
 * A space contains the doors that provide access to it.
 * These are the doors whose destination is this space.
 * Space is a leaf in the Composite design pattern.
 * Unlike a Partition, it does not contain other areas.
 */
public class Space extends Area {

  private final List<Door> accessDoors;

  public Space(String id) {
    super(id);
    this.accessDoors = new ArrayList<>();
  }

  /**
   * Registers a door that gives access to this space.
   *
   * @param door the door to register
   */
  public void addDoorGivingAccess(Door door) {
    if (door == null) {
      throw new IllegalArgumentException("Access door cannot be null");
    }
    // Prevent duplicate door registrations.
    if (!accessDoors.contains(door)) {
      accessDoors.add(door);
    }
  }

  @Override
  public List<Door> getDoorsGivingAccess() {
    return new ArrayList<>(accessDoors);
  }

  @Override
  public boolean containsArea(Area area) {
    return this == area;
  }
}