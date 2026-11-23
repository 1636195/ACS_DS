package baseNoStates;

/**
 * Represents a door that has remained open after its temporary unlocking period has expired.
 * Closing a propped door automatically locks it.
 */
public class DoorPropped extends DoorState {

  public DoorPropped(Door door) {
    super(door);
  }

  @Override
  protected void close() {
    door.setClosed(true);
    door.setState(new DoorLock(door));
  }

  @Override
  public String getName() {
    return "propped";
  }
}