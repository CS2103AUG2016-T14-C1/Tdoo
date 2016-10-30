package seedu.Tdoo.model.task.attributes;

import seedu.Tdoo.commons.exceptions.IllegalValueException;

/**
 * Represents a task's name in the TodoList.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_NAME_CONSTRAINTS = "task names can accept any characters";
    public static final String NAME_VALIDATION_REGEX = ".+";

    public final String name;

    /**
     * Validates given name.
     *
     * @throws IllegalValueException if given name string is invalid.
     */
    public Name(String name) throws IllegalValueException {
        assert name != null;
        name = name.trim();
        if (!isValidName(name)) {
            throw new IllegalValueException(MESSAGE_NAME_CONSTRAINTS);
        }
        this.name = name;
    }

    /**
     * Returns true if a given string is a valid task name.
     */
    public static boolean isValidName(String test) {
        return test.matches(NAME_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Name // instanceof handles nulls
                && this.name.equals(((Name) other).name)); // state check
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
