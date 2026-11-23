package baseNoStates;

import java.util.List;

/**
 * Represents an area in the access control system.
 * An area can be an individual space or a partition containing other areas.
 * This class is the common component of the Composite design pattern,
 * allowing operations to be performed uniformly on spaces and partitions.
 */
public abstract class Area {

  private final String id;

  protected Area(String id) {
    if (id == null || id.trim().isEmpty()) {
      throw new IllegalArgumentException("Area identifier cannot be empty");
    }
    this.id = id;
  }

  public String getId() {
    return id;
  }

  /**
   * Returns all doors that give access to this area.
   * Partitions will collect doors recursively.
   *
   * @return a list of doors
   */
  public abstract List<Door> getDoorsGivingAccess();

  /**
   * Determines whether this area contains another area, including itself.
   * Used for access permissions.
   *
   * @param area the area to check
   * @return true if this area contains the given area, false otherwise
   */
  public abstract boolean containsArea(Area area);

  @Override
  public String toString() {
    return id;
  }
}