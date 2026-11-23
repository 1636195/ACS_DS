package baseNoStates;

/**
 * Represents a registered user of the access control system.
 * Each user belongs to a group and acquires access permissions through that group,
 * following the RBAC model.
 */
public class User {

  private final String name;
  private final String credential;
  private UserGroup group;

  public User(String name, String credential, UserGroup group) {
    if (group == null) {
      throw new IllegalArgumentException("A user must belong to a group");
    }
    this.name = name;
    this.credential = credential;
    this.group = group;
  }

  public String getName() {
    return name;
  }

  public String getCredential() {
    return credential;
  }

  public UserGroup getGroup() {
    return group;
  }

  /**
   * Allows changing the user's role without deleting or recreating the user.
   *
   * @param group the new user group
   */
  public void setGroup(UserGroup group) {
    if (group == null) {
      throw new IllegalArgumentException("User group cannot be null");
    }
    this.group = group;
  }

  @Override
  public String toString() {
    return "User{"
        + "name='" + name + '\''
        + ", group=" + group.getName()
        + '}';
  }
}