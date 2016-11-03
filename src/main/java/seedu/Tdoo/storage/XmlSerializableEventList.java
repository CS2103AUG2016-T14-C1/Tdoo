package seedu.Tdoo.storage;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.Tdoo.commons.exceptions.IllegalValueException;
import seedu.Tdoo.model.ReadOnlyTaskList;
import seedu.Tdoo.model.task.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@@author A0144061U-reused
/**
 * An Immutable TaskList that is serializable to XML format
 */
@XmlRootElement(name = "EventList")
public class XmlSerializableEventList implements XmlSerializableTaskList {

    @XmlElement
    private List<XmlAdaptedEvent> tasks;

    {
        tasks = new ArrayList<>();
    }

    /**
     * Empty constructor required for marshalling
     */
    public XmlSerializableEventList() {}

    /**
     * Conversion
     */
    public XmlSerializableEventList(ReadOnlyTaskList src) {
    	tasks.addAll(src.getTaskList().stream().map(XmlAdaptedEvent::new).collect(Collectors.toList()));
    }

    @Override
    public UniqueTaskList getUniqueTaskList() {
        UniqueTaskList lists = new UniqueTaskList();
        for (XmlAdaptedEvent p : tasks) {
            try {
                lists.add(p.toModelType());
            } catch (IllegalValueException e) {
                //TODO: better error handling
            }
        }
        return lists;
    }

    @Override
    public List<ReadOnlyTask> getTaskList() {
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
