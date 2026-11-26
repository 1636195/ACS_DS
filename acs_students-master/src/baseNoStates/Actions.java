package baseNoStates;

/*
 * Defines the string constants for all actions supported by the access control system.
 * These constants are used to parse requests and to check permissions.
 */
public final class Actions {

  // Possible actions in reader and area requests
  public static final String LOCK = "lock";
  public static final String UNLOCK = "unlock";
  public static final String UNLOCK_SHORTLY = "unlock_shortly";

  // Possible actions in door requests
  public static final String OPEN = "open";
  public static final String CLOSE = "close";

  private Actions() {
    // Utility class: prevent instantiation
  }
}