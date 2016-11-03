package seedu.Tdoo.storage;

import seedu.Tdoo.commons.exceptions.IllegalValueException;
import seedu.Tdoo.model.task.*;
import seedu.Tdoo.model.task.attributes.*;

import javax.xml.bind.annotation.XmlElement;

//@@author A0144061U-reused
/**
 * JAXB-friendly version of the task.
 */
public class XmlAdaptedTodo implements XmlAdaptedTask {

	@XmlElement(required = true)
	private String name;
	@XmlElement(required = true)
	private String startDate;
	@XmlElement(required = true)
	private String endDate;
	@XmlElement(required = true)
	private String priority;
	@XmlElement(required = true)
	private String isDone;

	/**
	 * No-arg constructor for JAXB use.
	 */
	public XmlAdaptedTodo() {
	}

	/**
	 * Converts a given task into this class for JAXB use.
	 *
	 * @param source
	 *            future changes to this will not affect the created
	 *            XmlAdaptedtask
	 */
	public XmlAdaptedTodo(Todo source) {
		this.name = source.getName().name;
		this.startDate = source.getStartDate().saveDate;
		this.endDate = source.getEndDate().saveEndDate;
		this.priority = source.getPriority().savePriority;
		this.isDone = source.getDone();
	}

	public XmlAdaptedTodo(ReadOnlyTask source) {
		this((Todo) source);
	}

	public Todo toModelType() throws IllegalValueException {
		final Name name = new Name(this.name);
		final StartDate date = new StartDate(this.startDate);
		final EndDate endDate = new EndDate(this.endDate);
		final Priority priority = new Priority(this.priority);
		final String isDone = new String(this.isDone);
		return new Todo(name, date, endDate, priority, isDone);
	}
}
