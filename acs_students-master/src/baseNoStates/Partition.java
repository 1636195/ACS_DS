package baseNoStates;

import java.util.ArrayList;
import java.util.List;

/*
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

  /*
   * Adds a space or another partition.
   * It uses the AreaContainsVisitor to prevent cycles in the hierarchy.
   *
   * @param area the area to add
   */
  public void addArea(Area area) {
    if (area == null) {
      throw new IllegalArgumentException("Area cannot be null");
    }
    // Prevent cycles by checking if the new area already contains this partition.
    AreaContainsVisitor containsVisitor = new AreaContainsVisitor(this);
    if (area.accept(containsVisitor)) {
      throw new IllegalArgumentException("Cyclic area hierarchy is not allowed");
    }
    if (!children.contains(area)) {
      children.add(area);
    }
  }

  /*
   * Returns the direct children of this partition.
   *
   * @return a list of child areas
   */
  public List<Area> getChildren() {
    return new ArrayList<>(children);
  }

  @Override
  public <T> T accept(AreaVisitor<T> visitor) {
    return visitor.visit(this);
  }
}