package baseNoStates;

import java.util.List;

/**
 * Represents an area in the access control system.
 * An area can be an individual space or a partition containing other areas.
 * This class is the common component of the Composite design pattern,
 * allowing operations to be performed uniformly on spaces and partitions.
 *
 * The Visitor pattern is used to separate the tree structure from the
 * algorithms that traverse it.
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
   * Accepts a visitor, which will perform an operation on this area.
   * This is the entry point of the Visitor design pattern.
   *
   * @param visitor the visitor to accept
   * @param <T>     the type of result the visitor produces
   * @return the result of the visitor's operation
   */
  public abstract <T> T accept(AreaVisitor<T> visitor);

  @Override
  public String toString() {
    return id;
  }
}