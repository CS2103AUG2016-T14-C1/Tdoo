package seedu.Tdoo.model.task.attributes;

import seedu.Tdoo.commons.exceptions.IllegalValueException;

/**
 * Represents a Event's or Deadline's end time in the TodoList. Guarantees:
 * immutable; is valid as declared in {@link #isValidEndTime(String)}
 */
public class EndTime {

	public static final String MESSAGE_ENDTIME_CONSTRAINTS = "Event or Deadline End Time should be written in this format, must be 4 digits '10:00' and within 24 hrs format (0000 to 2359)";
	public static final String ENDTIME_VALIDATION_REGEX = "^(\\d{2}:\\d{2})$";

	public final String endTime;
	public final String saveEndTime;

	/**
	 * Validates given end time.
	 *
	 * @throws IllegalValueException
	 *             if given end time is invalid.
	 */
	public EndTime(String endTime) throws IllegalValueException {
		assert endTime != null;
		endTime = endTime.trim();
		saveEndTime = endTime.trim();
		if (!isValidEndTime(endTime)) {
			throw new IllegalValueException(MESSAGE_ENDTIME_CONSTRAINTS);
		}
		// Checking time in 24-Hr format
		String[] etimeArr = endTime.split(":");
		String hour = "";
		switch (etimeArr[0]) {
		case "00":
			hour = "12:";
			break;
		case "23":
			hour = "11:";
			break;
		case "22":
			hour = "10:";
			break;
		case "21":
			hour = "9:";
			break;
		case "20":
			hour = "8:";
			break;
		case "19":
			hour = "7:";
			break;
		case "18":
			hour = "6:";
			break;
		case "17":
			hour = "5:";
			break;
		case "16":
			hour = "4:";
			break;
		case "15":
			hour = "3:";
			break;
		case "14":
			hour = "2:";
			break;
		case "13":
			hour = "1:";
			break;
		default:
			hour = etimeArr[0].substring(1, 2) + ":";
		}
		if (Integer.parseInt(etimeArr[0]) > 11) {
			endTime = hour + etimeArr[1] + "pm";
			this.endTime = endTime;
		} else {
			endTime = hour + etimeArr[1] + "am";
			this.endTime = endTime;
		}
	}

	/**
	 * Returns if a given string is a valid event or deadline end time.
	 */
	public static boolean isValidEndTime(String endtime) {
		String[] etimeArr = endtime.split(":");
		boolean checkTime = true;

		// Check if time has hour and min, hour not more than 24hrs and min not
		// more than 59mins
		if (etimeArr.length < 2 || Integer.parseInt(etimeArr[0]) > 23 || Integer.parseInt(etimeArr[1]) > 59) {
			checkTime = false;
		}

		if (endtime.matches(ENDTIME_VALIDATION_REGEX) && checkTime) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return endTime;
	}

	@Override
	public boolean equals(Object other) {
		return other == this // short circuit if same object
				|| (other instanceof EndTime // instanceof handles nulls
						&& this.endTime.equals(((EndTime) other).endTime)); // state
																			// check
	}

	@Override
	public int hashCode() {
		return endTime.hashCode();
	}

}
