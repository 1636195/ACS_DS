package baseNoStates;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

/**
 * Represents a temporarily unlocked door.
 * The door can be opened or closed during ten seconds.
 * After that period, it becomes Locked if physically closed, or Propped if it remains open.
 * The clock is shared by all temporary door states.
 */
@SuppressWarnings("deprecation")
public class DoorUnlockShortly extends DoorState implements Observer {

  private static final Clock CLOCK = new Clock();
  private static final long DURATION_NANOS = TimeUnit.SECONDS.toNanos(10);
  private long startedAt;

  public DoorUnlockShortly(Door door) {
    super(door);
  }

  /**
   * Starts observing the shared clock.
   */
  public void startObserving() {
    startedAt = System.nanoTime();
    CLOCK.addObserver(this);
  }

  /**
   * Stops receiving clock notifications.
   */
  public void stopObserving() {
    CLOCK.deleteObserver(this);
  }

  @Override
  protected void open() {
    door.setClosed(false);
  }

  @Override
  protected void close() {
    door.setClosed(true);
  }

  @Override
  public String getName() {
    return "unlocked_shortly";
  }

  @Override
  public void update(Observable source, Object arg) {
    synchronized (door) {
      // Ignore notifications from an obsolete state.
      if (!door.isCurrentState(this)) {
        stopObserving();
        return;
      }
      // Check whether ten seconds have elapsed.
      long elapsed = System.nanoTime() - startedAt;
      if (elapsed < DURATION_NANOS) {
        return;
      }
      if (door.isClosed()) {
        door.setState(new DoorLock(door));
      } else {
        door.setState(new DoorPropped(door));
      }
    }
  }
}