package seedu.todoList.storage;

import seedu.todoList.model.task.*;
import seedu.todoList.commons.exceptions.IllegalValueException;

import javax.xml.bind.annotation.XmlElement;

import java.util.ArrayList;
import java.util.List;

/**
 * JAXB-friendly version of the task.
 */
public abstract class XmlAdaptedTask {
    
<<<<<<< HEAD
    StartTime startTime;
    EndTime endTime;
    
    @XmlElement(required = true)
    private String Todo;
    
=======
    @XmlElement(required = true)
    protected String name;


>>>>>>> 599478dd0130f46dae3a83f7da3f6c2fd212f22d
    /**
     * No-arg constructor for JAXB use.
     */
    public XmlAdaptedTask () {}


    /**
     * Converts a given task into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedtask
     */
    public XmlAdaptedTask(ReadOnlyTask source) {
<<<<<<< HEAD
        Todo = source.getTodo().value;

=======
        name = source.getName().fullName;
>>>>>>> 599478dd0130f46dae3a83f7da3f6c2fd212f22d
    }

    /**
     * Converts this jaxb-friendly adapted task object into the model's task object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted task
     */
<<<<<<< HEAD
    public Task toModelType() throws IllegalValueException {

        final Todo Todo = new Todo(this.Todo);
        return new Task(Todo, startTime, endTime);
    }
=======
    public abstract Task toModelType() throws IllegalValueException;
>>>>>>> 599478dd0130f46dae3a83f7da3f6c2fd212f22d
}
