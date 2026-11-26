package baseNoStates;

/**
 * A visitor that checks whether an area contains a specific target area.
 * It is used to prevent cycles when building the tree and to verify
 * permissions based on the area hierarchy.
 */
public class AreaContainsVisitor implements AreaVisitor<Boolean> {

  private final Area target;

  public AreaContainsVisitor(Area target) {
    this.target = target;
  }

  @Override
  public Boolean visit(Space space) {
    return space == target;
  }

  @Override
  public Boolean visit(Partition partition) {
    if (partition == target) {
      return true;
    }
    // Recursively check if any child contains the target.
    for (Area child : partition.getChildren()) {
      if (child.accept(this)) {
        return true;
      }
    }
    return false;
  }
}