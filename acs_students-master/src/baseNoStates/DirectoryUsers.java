package baseNoStates;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumSet;

/*
 * Creates and stores the registered users and their
 * access groups.
 *
 * Each group defines its permitted actions, areas
 * and schedule. Users inherit these permissions
 * through their group.
 */
public final class DirectoryUsers {

  private static final ArrayList<User> users = new ArrayList<>();

  private DirectoryUsers() {
  }

  public static void makeUsers() {
    users.clear();

    // Create the four groups.
    UserGroup blank = new UserGroup("Blank", false);
    UserGroup employees = new UserGroup("Employee", false);
    UserGroup managers = new UserGroup("Manager", false);
    UserGroup admin = new UserGroup("Administrator", true);

    // ---------------------------------------
    // EMPLOYEE GROUP
    // ---------------------------------------

    Schedule employeeSchedule = new Schedule(
            LocalDate.of(2026, 9, 1),
            LocalDate.of(2027, 3, 1),
            LocalTime.of(9, 0),
            LocalTime.of(17, 0),
            EnumSet.range(
                    DayOfWeek.MONDAY,
                    DayOfWeek.FRIDAY
            )
    );

    employees.setSchedule(employeeSchedule);

    employees.addAllowedAction(Actions.OPEN);
    employees.addAllowedAction(Actions.CLOSE);
    employees.addAllowedAction(Actions.UNLOCK_SHORTLY);

    // Employees may access all areas except parking.
    employees.addAllowedArea(
            DirectoryAreas.findAreaById("ground_floor")
    );
    employees.addAllowedArea(
            DirectoryAreas.findAreaById("floor1")
    );
    employees.addAllowedArea(
            DirectoryAreas.findAreaById("stairs")
    );
    employees.addAllowedArea(
            DirectoryAreas.findAreaById("exterior")
    );

    // ---------------------------------------
    // MANAGER GROUP
    // ---------------------------------------

    Schedule managerSchedule = new Schedule(
            LocalDate.of(2026, 9, 1),
            LocalDate.of(2027, 3, 1),
            LocalTime.of(8, 0),
            LocalTime.of(20, 0),
            EnumSet.range(
                    DayOfWeek.MONDAY,
                    DayOfWeek.SATURDAY
            )
    );

    managers.setSchedule(managerSchedule);

    // Managers can perform every action.
    managers.addAllowedAction(Actions.OPEN);
    managers.addAllowedAction(Actions.CLOSE);
    managers.addAllowedAction(Actions.LOCK);
    managers.addAllowedAction(Actions.UNLOCK);
    managers.addAllowedAction(Actions.UNLOCK_SHORTLY);

    // Grant access to the entire building.
    managers.addAllowedArea(
            DirectoryAreas.findAreaById("building")
    );

    // ---------------------------------------
    // ADMINISTRATOR GROUP
    // ---------------------------------------

    // Administrators have unrestricted access.
    // UserGroup handles this through the
    // administrator flag.

    // ---------------------------------------
    // BLANK GROUP
    // ---------------------------------------

    // No actions, areas or schedule are assigned.
    // Users remain registered without privileges.

    // ---------------------------------------
    // REGISTER USERS
    // ---------------------------------------

    users.add(new User("Bernat", "12345", blank));
    users.add(new User("Blai", "77532", blank));

    users.add(new User("Ernest", "74984", employees));
    users.add(new User("Eulalia", "43295", employees));

    users.add(new User("Manel", "95783", managers));
    users.add(new User("Marta", "05827", managers));

    users.add(new User("Ana", "11343", admin));
  }

  // Finds a user by their unique credential.
  public static User findUserByCredential(String credential) {
    for (User user : users) {
      if (user.getCredential().equals(credential)) {
        return user;
      }
    }

    System.out.println(
            "User with credential " + credential + " not found"
    );

    return null;
  }
}
