package baseNoStates;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/*
 * Defines when a user group is allowed to access
 * the building.
 *
 * A schedule contains a date range, a daily time
 * range and a set of permitted days of the week.
 *
 * User groups use schedules to determine whether
 * a request is authorized at a given date and time.
 */
public class Schedule {

    private final LocalDate startDate;
    private final LocalDate endDate;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final Set<DayOfWeek> allowedDays;

    public Schedule(
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime,
            Set<DayOfWeek> allowedDays) {

        if (startDate == null || endDate == null
                || startTime == null || endTime == null
                || allowedDays == null || allowedDays.isEmpty()) {
            throw new IllegalArgumentException(
                    "Schedule parameters cannot be null or empty"
            );
        }

        if (startDate.isAfter(endDate)
                || startTime.isAfter(endTime)) {
            throw new IllegalArgumentException(
                    "Invalid schedule range"
            );
        }

        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.allowedDays = Collections.unmodifiableSet(
                EnumSet.copyOf(allowedDays)
        );
    }

    /*
     * Checks whether the requested date and time
     * belong to this schedule.
     *
     * Start and end boundaries are inclusive.
     */
    public boolean isInSchedule(LocalDateTime dateTime) {

        if (dateTime == null) {
            return false;
        }

        LocalDate date = dateTime.toLocalDate();
        LocalTime time = dateTime.toLocalTime();

        boolean validDate =
                !date.isBefore(startDate)
                        && !date.isAfter(endDate);

        boolean validDay =
                allowedDays.contains(date.getDayOfWeek());

        boolean validTime =
                !time.isBefore(startTime)
                        && !time.isAfter(endTime);

        return validDate && validDay && validTime;
    }
}
