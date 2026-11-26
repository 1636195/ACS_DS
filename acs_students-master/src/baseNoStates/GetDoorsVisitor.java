package baseNoStates;

import java.util.ArrayList;
import java.util.List;

/**
 * Visitor that collects all doors giving access within an area.
 * It replaces the old getDoorsGivingAccess() method.
 */
public class GetDoorsVisitor implements AreaVisitor<List<Door>> {

  @Override
  public List<Door> visit(Space space) {
    // A space is a leaf, so it returns its direct access doors.
    return space.getAccessDoors();
  }

  @Override
  public List<Door> visit(Partition partition) {
    // A partition aggregates the doors of all its children.
    List<Door> allDoors = new ArrayList<>();
    for (Area child : partition.getChildren()) {
      allDoors.addAll(child.accept(this));
    }
    return allDoors;
  }
}