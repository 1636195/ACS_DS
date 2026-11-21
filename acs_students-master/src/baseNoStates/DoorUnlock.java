package baseNoStates;

/*
 * Represents an unlocked door.
 *
 * An unlocked door can be opened and closed.
 * It can only be locked when physically closed.
 *
 * This class is a concrete state of the State pattern.
 */
public class DoorUnlock extends DoorState {

    public DoorUnlock(Door door) {
        super(door);
    }

    @Override
    protected void open() {
        if (door.isClosed()) {
            door.setClosed(false);
        }
    }

    @Override
    protected void close() {
        if (!door.isClosed()) {
            door.setClosed(true);
        }
    }

    @Override
    protected void lock() {
        if (door.isClosed()) {
            door.setState(new DoorLock(door));
        }
    }

    @Override
    public String getName() {
        return "unlocked";
    }
}
