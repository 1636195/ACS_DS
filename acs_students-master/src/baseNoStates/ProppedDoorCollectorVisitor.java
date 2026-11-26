package baseNoStates;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * A visitor that collects all doors currently in the "propped" state.
 * This is an example of a new traversal operation that can be added
 * without modifying the Space or Partition classes.
 */
public class ProppedDoorCollectorVisitor implements AreaVisitor<List<Door>> {

  @Override
  public List<Door> visit(Space space) {
    List<Door> proppedDoors = new ArrayList<>();
    for (Door door : space.getAccessDoors()) {
      if (door.getStateName().equals("propped")) {
        proppedDoors.add(door);
      }
    }
    return proppedDoors;
  }

  @Override
  public List<Door> visit(Partition partition) {
    Set<Door> proppedDoors = new LinkedHashSet<>();
    for (Area child : partition.getChildren()) {
      proppedDoors.addAll(child.accept(this));
    }
    return new ArrayList<>(proppedDoors);
  }
}