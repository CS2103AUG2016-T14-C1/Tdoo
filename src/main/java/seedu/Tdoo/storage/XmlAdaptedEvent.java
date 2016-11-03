package seedu.Tdoo.storage;

import seedu.Tdoo.commons.exceptions.IllegalValueException;
import seedu.Tdoo.model.task.*;
import seedu.Tdoo.model.task.attributes.*;

import javax.xml.bind.annotation.XmlElement;

//@@author A0144061U-reused
/**
 * JAXB-friendly version of the task.
 */
public class XmlAdaptedEvent implements XmlAdaptedTask {
    
	@XmlElement(required = true)
	private String name;
	@XmlElement(required = true)
	private String startDate;
	@XmlElement(required = true)
	private String endDate;
	@XmlElement(required = true)
    private String startTime;
	@XmlElement(required = true)
    private String endTime;
	@XmlElement(required = true)
    private String isDone;

    /**
     * No-arg constructor for JAXB use.
     */
    public XmlAdaptedEvent () {}


    /**
     * Converts a given task into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedtask
     */
    public XmlAdaptedEvent(Event source) {
    	name = source.getName().name;
    	startDate = source.getStartDate().saveDate;
    	endDate = source.getEndDate().saveEndDate;
        startTime = source.getStartTime().saveStartTime;
        endTime = source.getEndTime().saveEndTime;
        isDone = source.getDone();
    }
    
    public XmlAdaptedEvent(ReadOnlyTask source) {
    	this((Event) source);
    }
    
    public Task toModelType() throws IllegalValueException {
        final Name name = new Name(this.name);
        final StartDate date = new StartDate(this.startDate);
        final EndDate endDate = new EndDate(this.endDate);
        final StartTime startTime = new StartTime(this.startTime);
        final EndTime endTime = new EndTime(this.endTime);
        final String isDone = new String(this.isDone);
        return new Event(name, date, endDate, startTime, endTime, isDone);
    }
}

