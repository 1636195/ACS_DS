package baseNoStates;

import java.util.LinkedHashMap;
import java.util.Map;

/*
 * Creates and stores the hierarchy of areas in the building.
 *
 * Partitions group spaces and other partitions, while each
 * space stores the doors that provide access to it.
 *
 * This directory also connects every door with its origin
 * and destination spaces.
 */
public final class DirectoryAreas {

    private static final Map<String, Area> areas =
            new LinkedHashMap<>();

    private DirectoryAreas() {
    }

    public static void makeAreas() {
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
        for (Area area : new Area[] {
                building, basement, groundFloor, floor1,
                parking, hall, room1, room2, room3,
                corridor, it, stairs, exterior
        }) {
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

    // Finds an area using its unique identifier.
    public static Area findAreaById(String id) {
        return areas.get(id);
    }

    // Connects a door with its origin and destination.
    private static void connectDoor(
            String doorId, Space origin, Space destination) {

        Door door = DirectoryDoors.findDoorById(doorId);

        if (door == null) {
            throw new IllegalArgumentException(
                    "Unknown door: " + doorId
            );
        }

        door.connectSpaces(origin, destination);
    }
}