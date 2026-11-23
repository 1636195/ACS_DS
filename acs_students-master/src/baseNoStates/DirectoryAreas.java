package baseNoStates;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Acts as a singleton registry for the hierarchical tree of areas (partitions and spaces).
 * It guarantees a single source of truth for the building structure.
 */
public final class DirectoryAreas {

  private final Map<String, Area> areas;

  private DirectoryAreas() {
    areas = new LinkedHashMap<>();
  }

  // Thread-safe lazy initialization Singleton holder.
  private static class Holder {
    private static final DirectoryAreas INSTANCE = new DirectoryAreas();
  }

  public static DirectoryAreas getInstance() {
    return Holder.INSTANCE;
  }

  public void makeAreas() {
    areas.clear();

    // Create the partitions.
    Partition building = new Partition("building");
    Partition basement = new Partition("basement");
    Partition groundFloor = new Partition("ground_floor");
    Partition floor1 = new Partition("floor1");

    // Create the spaces.
    Space parking = new Space("parking");
    Space hall = new Space("hall");
    Space room1 = new Space("room1");
    Space room2 = new Space("room2");
    Space room3 = new Space("room3");
    Space corridor = new Space("corridor");
    Space it = new Space("IT");
    Space stairs = new Space("stairs");
    Space exterior = new Space("exterior");

    // Build the hierarchy using the Composite pattern.
    building.addArea(basement);
    building.addArea(groundFloor);
    building.addArea(floor1);
    building.addArea(stairs);
    building.addArea(exterior);
    basement.addArea(parking);
    groundFloor.addArea(hall);
    groundFloor.addArea(room1);
    groundFloor.addArea(room2);
    floor1.addArea(room3);
    floor1.addArea(corridor);
    floor1.addArea(it);

    // Register all areas for searching by identifier.
    for (Area area : new Area[] { building, basement, groundFloor, floor1,
        parking, hall, room1, room2, room3, corridor, it, stairs, exterior }) {
      areas.put(area.getId(), area);
    }

    // Basement doors.
    connectDoor("D1", exterior, parking);
    connectDoor("D2", stairs, parking);

    // Ground floor doors.
    connectDoor("D3", exterior, hall);
    connectDoor("D4", stairs, hall);
    connectDoor("D5", hall, room1);
    connectDoor("D6", hall, room2);

    // First floor doors.
    connectDoor("D7", stairs, corridor);
    connectDoor("D8", corridor, room3);
    connectDoor("D9", corridor, it);
  }

  /**
   * Finds an area using its unique identifier.
   *
   * @param id the area identifier
   * @return the area, or null if not found
   */
  public Area findAreaById(String id) {
    return areas.get(id);
  }

  /**
   * Connects a door with its origin and destination spaces.
   *
   * @param doorId      the door identifier
   * @param origin      the origin space
   * @param destination the destination space
   */
  private void connectDoor(String doorId, Space origin, Space destination) {
    // Access the door via the DirectoryDoors singleton
    Door door = DirectoryDoors.getInstance().findDoorById(doorId);
    if (door == null) {
      throw new IllegalArgumentException("Unknown door: " + doorId);
    }
    door.connectSpaces(origin, destination);
  }
}