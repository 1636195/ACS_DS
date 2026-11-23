package baseNoStates;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a group of spaces and other partitions.
 * Partition is the composite element of the Composite design pattern.
 * It allows areas to be organized hierarchically and operations to be applied
 * to every space contained in a partition.
 */
public class Partition extends Area {

  private final List<Area> children;

  public Partition(String id) {
    super(id);
    this.children = new ArrayList<>();
  }

  /**
   * Adds a space or another partition.
   *
   * @param area the area to add
   */
  public void addArea(Area area) {
    if (area == null) {
      throw new IllegalArgumentException("Area cannot be null");
    }
    // Prevent cycles in the hierarchy.
    if (area == this || area.containsArea(this)) {
      throw new IllegalArgumentException("Cyclic area hierarchy is not allowed");
    }
    if (!children.contains(area)) {
      children.add(area);
    }
  }

  /**
   * Returns the direct children of this partition.
   *
   * @return a list of child areas
   */
  public List<Area> getChildren() {
    return new ArrayList<>(children);
  }

  @Override
  public List<Door> getDoorsGivingAccess() {
    Set<Door> doors = new LinkedHashSet<>();
    // Recursively collect doors from every child.
    for (Area child : children) {
      doors.addAll(child.getDoorsGivingAccess());
    }
    return new ArrayList<>(doors);
  }

  @Override
  public boolean containsArea(Area area) {
    if (this == area) {
      return true;
    }
    // Search recursively in all child areas.
    for (Area child : children) {
      if (child.containsArea(area)) {
        return true;
      }
    }
    return false;
  }
}