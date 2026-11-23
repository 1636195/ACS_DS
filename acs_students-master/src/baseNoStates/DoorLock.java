package baseNoStates;

/**
 * Represents a locked door.
 * A locked door cannot be opened. It must first be unlocked before allowing access.
 * This class is a concrete state of the State pattern.
 */
public class DoorLock extends DoorState {

  public DoorLock(Door door) {
    super(door);
  }

  @Override
  protected void unlock() {
    door.setState(new DoorUnlock(door));
  }

  @Override
  protected void unlockShortly() {
    door.setState(new DoorUnlockShortly(door));
  }

  @Override
  public String getName() {
    return "locked";
  }
}