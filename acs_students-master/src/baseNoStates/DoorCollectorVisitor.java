package baseNoStates;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/*
 * A visitor that traverses the area tree and collects all doors
 * that give access to any space within the visited area.
 * It is used by area requests to apply an action to every relevant door.
 */
public class DoorCollectorVisitor implements AreaVisitor<List<Door>> {

  @Override
  public List<Door> visit(Space space) {
    // A leaf space returns its direct access doors.
    return space.getAccessDoors();
  }

  @Override
  public List<Door> visit(Partition partition) {
    Set<Door> doors = new LinkedHashSet<>();
    // A partition aggregates the results from all its children.
    for (Area child : partition.getChildren()) {
      doors.addAll(child.accept(this));
    }
    return new ArrayList<>(doors);
  }
}