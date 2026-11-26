package baseNoStates;

import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

/*
 * Shared clock used to notify doors that are temporarily unlocked.
 * It follows the Observer pattern. Every second, the clock notifies its registered observers.
 * The timer runs on a separate thread so that the server can continue processing requests.
 */
@SuppressWarnings("deprecation")
public class Clock extends Observable {

  private final Timer timer;

  public Clock() {
    // The daemon thread will not prevent the application from shutting down.
    timer = new Timer("ACS-clock", true);

    // Notify observers every second.
    timer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        Clock.this.setChanged();
        Clock.this.notifyObservers(System.nanoTime());
      }
    }, 1000, 1000);
  }
}