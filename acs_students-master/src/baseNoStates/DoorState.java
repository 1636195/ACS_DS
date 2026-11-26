package baseNoStates;

/*
 * Base class for the State design pattern.
 * Each door delegates its actions to its current state.
 * Subclasses decide which actions are permitted and
 * how the door behaves when an action is requested.
 */
public abstract class DoorState {

  protected final Door door;

  protected DoorState(Door door) {
    this.door = door;
  }

  /*
   * Dispatches the requested action to the current state.
   *
   * @param action the action to execute
   */
  public final void execute(String action) {
    switch (action) {
      case Actions.OPEN:
        open();
        break;
      case Actions.CLOSE:
        close();
        break;
      case Actions.LOCK:
        lock();
        break;
      case Actions.UNLOCK:
        unlock();
        break;
      case Actions.UNLOCK_SHORTLY:
        unlockShortly();
        break;
      default:
        throw new IllegalArgumentException("Unknown action: " + action);
    }
  }

  // By default, an action does not change the door.
  // Each concrete state overrides its valid actions.
  protected void open() {
  }

  protected void close() {
  }

  protected void lock() {
  }

  protected void unlock() {
  }

  protected void unlockShortly() {
  }

  /*
   * Each concrete state provides its name.
   *
   * @return the state name
   */
  public abstract String getName();
}