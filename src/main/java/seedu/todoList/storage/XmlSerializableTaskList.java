package seedu.todoList.storage;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.todoList.commons.exceptions.IllegalValueException;
import seedu.todoList.model.ReadOnlyTodoList;
import seedu.todoList.model.task.ReadOnlyTask;
import seedu.todoList.model.task.UniqueTaskList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An Immutable TaskList that is serializable to XML format
 */
@XmlRootElement(name = "TaskList")
public class XmlSerializableTaskList implements ReadOnlyTodoList {

    @XmlElement
    private List<XmlAdaptedTask> tasks;
<<<<<<< HEAD:src/main/java/seedu/todoList/storage/XmlSerializableTodoList.java

=======
>>>>>>> 599478dd0130f46dae3a83f7da3f6c2fd212f22d:src/main/java/seedu/todoList/storage/XmlSerializableTaskList.java

    {
        tasks = new ArrayList<>();
    }

    /**
     * Empty constructor required for marshalling
     */
    public XmlSerializableTaskList() {}

    /**
     * Conversion
     */
    public XmlSerializableTaskList(ReadOnlyTodoList src) {
        tasks.addAll(src.gettaskList().stream().map(XmlAdaptedTask::new).collect(Collectors.toList()));
    }

    @Override
    public UniqueTaskList getUniqueTaskList() {
        UniqueTaskList lists = new UniqueTaskList();
        for (XmlAdaptedTask p : tasks) {
            try {
                lists.add(p.toModelType());
            } catch (IllegalValueException e) {
                //TODO: better error handling
            }
        }
        return lists;
    }

    @Override
    public List<ReadOnlyTask> gettaskList() {
        return tasks.stream().map(p -> {
            try {
                return p.toModelType();
            } catch (IllegalValueException e) {
                e.printStackTrace();
                //TODO: better error handling
                return null;
            }
        }).collect(Collectors.toCollection(ArrayList::new));
    }
}
