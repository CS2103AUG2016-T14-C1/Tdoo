package seedu.Tdoo.model;

import javafx.collections.transformation.FilteredList;
import seedu.Tdoo.commons.core.ComponentManager;
import seedu.Tdoo.commons.core.LogsCenter;
import seedu.Tdoo.commons.core.UnmodifiableObservableList;
import seedu.Tdoo.commons.events.model.*;
import seedu.Tdoo.commons.events.ui.*;
import seedu.Tdoo.commons.exceptions.*;
import seedu.Tdoo.commons.util.StringUtil;
import seedu.Tdoo.logic.commands.*;
import seedu.Tdoo.model.task.*;
import seedu.Tdoo.model.task.UniqueTaskList.TaskNotFoundException;
import seedu.Tdoo.commons.events.ui.JumpTodoListRequestEvent;

import java.util.EmptyStackException;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;

/**
 * Represents the in-memory model of the TodoList data. All changes to any model
 * should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
	private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

	private final TaskList todoList;
	private final TaskList eventList;
	private final TaskList deadlineList;
	private final FilteredList<Task> filteredTodos;
	private final FilteredList<Task> filteredEvents;
	private final FilteredList<Task> filteredDeadlines;

	private final Undoer undoer;

	/**
	 * Initializes a ModelManager with the given TodoList TodoList and its
	 * variables should not be null
	 */
	public ModelManager(TaskList src, UserPrefs userPrefs) {
		super();
		assert src != null;
		assert userPrefs != null;

		logger.fine("Initializing with TaskLists: " + src + " and user prefs " + userPrefs);

		todoList = new TaskList(src);
		eventList = new TaskList(src);
		deadlineList = new TaskList(src);
		filteredTodos = new FilteredList<>(todoList.getTasks());
		filteredEvents = new FilteredList<>(eventList.getTasks());
		filteredDeadlines = new FilteredList<>(deadlineList.getTasks());

		undoer = Undoer.getInstance(this);
	}

	public ModelManager() {
		this(new TaskList(), new UserPrefs());
	}

	public ModelManager(ReadOnlyTaskList initialTodoListData, ReadOnlyTaskList initialEventListData,
			ReadOnlyTaskList initialDeadlineListData, UserPrefs userPrefs) {
		todoList = new TaskList(initialTodoListData);
		eventList = new TaskList(initialEventListData);
		deadlineList = new TaskList(initialDeadlineListData);
		filteredTodos = new FilteredList<>(todoList.getTasks());
		filteredEvents = new FilteredList<>(eventList.getTasks());
		filteredDeadlines = new FilteredList<>(deadlineList.getTasks());

		undoer = Undoer.getInstance(this);
	}

	// @@author A0144061U
	@Override
	public void resetTodoListData(ReadOnlyTaskList newData) {
		todoList.resetData(newData);
		indicateTodoListChanged();
		updateFilteredTodoListToShowAll();
	}

	@Override
	public void resetEventListData(ReadOnlyTaskList newData) {
		eventList.resetData(newData);
		indicateEventListChanged();
		updateFilteredEventListToShowAll();
	}

	@Override
	public void resetDeadlineListData(ReadOnlyTaskList newData) {
		deadlineList.resetData(newData);
		indicateDeadlineListChanged();
		updateFilteredDeadlineListToShowAll();
	}

	@Override
	public void resetAllData() {
		undoer.prepareUndoClear("all");
		todoList.resetData();
		eventList.resetData();
		deadlineList.resetData();
		indicateTodoListChanged();
		indicateEventListChanged();
		indicateDeadlineListChanged();
	}

	@Override
	public void resetTodoListData() {
		undoer.prepareUndoClear("todo");
		todoList.resetData();
		indicateTodoListChanged();
	}

	@Override
	public void resetEventListData() {
		undoer.prepareUndoClear("event");
		eventList.resetData();
		indicateEventListChanged();
	}

	@Override
	public void resetDeadlineListData() {
		undoer.prepareUndoClear("deadline");
		deadlineList.resetData();
		indicateDeadlineListChanged();
	}

	@Override
	public void removeDoneData() {
		undoer.prepareUndoClear("all");
		todoList.removeDone();
		eventList.removeDone();
		deadlineList.removeDone();
		indicateTodoListChanged();
		indicateEventListChanged();
		indicateDeadlineListChanged();
	}

	@Override
	public void removeDoneTodoData() {
		undoer.prepareUndoClear("todo");
		todoList.removeDone();
		indicateTodoListChanged();
	}

	@Override
	public void removeDoneEventData() {
		undoer.prepareUndoClear("event");
		eventList.removeDone();
		indicateEventListChanged();
	}

	@Override
	public void removeDoneDeadlineData() {
		undoer.prepareUndoClear("deadline");
		deadlineList.removeDone();
		indicateDeadlineListChanged();
	}

	@Override
	public void restoreAllData() {
		todoList.restoreData();
		eventList.restoreData();
		deadlineList.restoreData();
		indicateTodoListChanged();
		indicateEventListChanged();
		indicateDeadlineListChanged();
	}

	@Override
	public void restoreTodoListData() {
		todoList.restoreData();
		indicateTodoListChanged();
	}

	@Override
	public void restoreEventListData() {
		eventList.restoreData();
		indicateEventListChanged();
	}

	@Override
	public void restoreDeadlineListData() {
		deadlineList.restoreData();
		indicateDeadlineListChanged();
	}
	// @@author

	@Override
	public ReadOnlyTaskList getTodoList() {
		return todoList;
	}

	@Override
	public ReadOnlyTaskList getEventList() {
		return eventList;
	}

	@Override
	public ReadOnlyTaskList getDeadlineList() {
		return deadlineList;
	}

	/** Raises an event to indicate the model has changed */
	private void indicateTodoListChanged() {
		raise(new TodoListChangedEvent(todoList));
	}

	/** Raises an event to indicate the model has changed */
	private void indicateEventListChanged() {
		raise(new EventListChangedEvent(eventList));
	}

	/** Raises an event to indicate the model has changed */
	private void indicateDeadlineListChanged() {
		raise(new DeadlineListChangedEvent(deadlineList));
	}

	/**
	 * Edit a task, event or deadline All changes to any model should be
	 * synchronized.
	 */
	@Override
	// @@author A0139923X
	public synchronized void editTask(ReadOnlyTask target, String dataType, Task task, int targetIndex)
			throws IllegalValueException, TaskNotFoundException {
	    
	    String type = "";
		/*
		 * Scenario: User wants to edit todo to change to event or deadline User
		 * enters edit todo 1 n/test d/01-01-2016 e/1000 This will remove index
		 * 1 of todolist and add into deadlinelist
		 * 
		 * During edit process, example: edit todo 1 with parameters of deadline
		 * todolist will remove the task index 1 and deadlinelist will add the
		 * edited task
		 */
		if (task instanceof Todo) {
			if (dataType.equals("todo")) {
				todoList.addTaskWithIndex(task , targetIndex);
				todoList.removeTask(target);
				todoList.sortData();
			} else if (dataType.equals("event")) {
				todoList.addTaskWithIndex(task , targetIndex);
				todoList.sortData();
                type = "todo";
				eventList.removeTask(target);
			} else if (dataType.equals("deadline")) {
				todoList.addTaskWithIndex(task , targetIndex);
				todoList.sortData();
                type = "todo";
				deadlineList.removeTask(target);
			}
			updateFilteredTodoListToShowAll();
			indicateTodoListChanged();
			raise(new JumpTodoListRequestEvent(task));
		} else if (task instanceof Event) {
			if (dataType.equals("todo")) {
				eventList.addTaskWithIndex(task , targetIndex);
				eventList.sortData();
                type = "event";
				todoList.removeTask(target);
			} else if (dataType.equals("event")) {
				eventList.addTaskWithIndex(task , targetIndex);
				eventList.sortData();
				eventList.removeTask(target);
			} else if (dataType.equals("deadline")) {
				eventList.addTaskWithIndex(task , targetIndex);
				eventList.sortData();
                type = "event";
				deadlineList.removeTask(target);
			}
			updateFilteredEventListToShowAll();
			indicateEventListChanged();
			raise(new JumpEventListRequestEvent(task));
		} else if (task instanceof Deadline) {
			if (dataType.equals("todo")) {
				deadlineList.addTaskWithIndex(task , targetIndex);
				deadlineList.sortData();
	            type = "deadline";
				todoList.removeTask(target);
			} else if (dataType.equals("event")) {
				deadlineList.addTaskWithIndex(task , targetIndex);
				deadlineList.sortData();
	            type = "deadline";
				eventList.removeTask(target);
			} else if (dataType.equals("deadline")) {
				deadlineList.addTaskWithIndex(task , targetIndex);
				deadlineList.sortData();
		        deadlineList.removeTask(target);
			}
			updateFilteredDeadlineListToShowAll();
			indicateDeadlineListChanged();
			raise(new JumpDeadlineListRequestEvent(task));
		}
		if(!undoer.undoCommand()) {
			undoer.prepareUndoEdit(target, dataType, task , targetIndex , type);
		}
	}

	// @@author A0139920A
	@Override
	public synchronized void doneTask(ReadOnlyTask target, String dataType, int undoTarget)
			throws TaskNotFoundException {
		switch (dataType) {
		case "todo":
			todoList.doneTask(target);
			indicateTodoListChanged();
			raise(new JumpTodoListRequestEvent(target));
			if(!undoer.undoCommand()) {
				undoer.prepareUndoDone("todo", undoTarget);
			}
		case "event":
			eventList.doneTask(target);
			indicateEventListChanged();
			raise(new JumpEventListRequestEvent(target));
			if(!undoer.undoCommand()) {
				undoer.prepareUndoDone("event", undoTarget);
			}
		case "deadline":
			deadlineList.doneTask(target);
			indicateDeadlineListChanged();
			raise(new JumpDeadlineListRequestEvent(target));
			if(!undoer.undoCommand()) {
				undoer.prepareUndoDone("deadline", undoTarget);
			}
		}
	}

	@Override
	// @@author A0139923X
	public synchronized void undoneTask(ReadOnlyTask target, String dataType, int undoTarget) throws TaskNotFoundException {
		switch (dataType) {
		case "todo":
			todoList.undoneTask(target);
			indicateTodoListChanged();
			if(!undoer.undoCommand()) {
				undoer.prepareUndoUndone("todo", undoTarget);
			}
		case "event":
			eventList.undoneTask(target);
			indicateEventListChanged();
			if(!undoer.undoCommand()) {
				undoer.prepareUndoUndone("event", undoTarget);
			}
		case "deadline":
			deadlineList.undoneTask(target);
			indicateDeadlineListChanged();
			if(!undoer.undoCommand()) {
				undoer.prepareUndoUndone("deadline", undoTarget);
			}
		}
	}
	// @@author A0144061U
	@Override
	public synchronized void deleteTask(ReadOnlyTask target, String dataType) throws TaskNotFoundException {
		switch (dataType) {
		case "todo":
			todoList.removeTask(target);
			indicateTodoListChanged();
			if(!undoer.undoCommand()) {
				undoer.prepareUndoDelete(target);
			}
			break;
		case "event":
			eventList.removeTask(target);
			indicateEventListChanged();
			if(!undoer.undoCommand()) {
				undoer.prepareUndoDelete(target);
			}
			break;
		case "deadline":
			deadlineList.removeTask(target);
			indicateDeadlineListChanged();
			if(!undoer.undoCommand()) {
				undoer.prepareUndoDelete(target);
			}
		}
	}

	@Override
	public synchronized void addTask(Task task) throws IllegalValueException, UniqueTaskList.DuplicatetaskException {
		if (task instanceof Todo) {
			todoList.addTask(task);
			todoList.sortData();
			updateFilteredTodoListToShowAll();
			indicateTodoListChanged();
			raise(new JumpTodoListRequestEvent(task));
			if(!undoer.undoCommand()) {
				undoer.prepareUndoAdd(task, "todo");
			}
		} else if (task instanceof Event) {
			eventList.addTask(task);
			eventList.sortData();
			updateFilteredEventListToShowAll();
			indicateEventListChanged();
			raise(new JumpEventListRequestEvent(task));
			if(!undoer.undoCommand()) {
				undoer.prepareUndoAdd(task, "event");
			}
		} else if (task instanceof Deadline) {
			deadlineList.addTask(task);
			deadlineList.sortData();
			updateFilteredDeadlineListToShowAll();
			indicateDeadlineListChanged();
			raise(new JumpDeadlineListRequestEvent(task));
			if(!undoer.undoCommand()) {
				undoer.prepareUndoAdd(task, "deadline");
			}
		} else {
			throw new IllegalValueException("Invalid data type for add");
		}
	}
	
	@Override
    //@@author A0139923X
	public synchronized void addTaskWithIndex(Task task, int targetIndex) throws IllegalValueException, UniqueTaskList.DuplicatetaskException {
        if (task instanceof Todo) {
            todoList.addTaskWithIndex(task, targetIndex);
            updateFilteredTodoListToShowAll();
            indicateTodoListChanged();
            if(!undoer.undoCommand()) {
            	undoer.prepareUndoAdd(task, "todo");
			}
        } else if (task instanceof Event) {
            eventList.addTaskWithIndex(task, targetIndex);
            updateFilteredEventListToShowAll();
            indicateEventListChanged();
            if(!undoer.undoCommand()) {
            	undoer.prepareUndoAdd(task, "event");
			}
        } else if (task instanceof Deadline) {
            deadlineList.addTaskWithIndex(task, targetIndex);
            updateFilteredDeadlineListToShowAll();
            indicateDeadlineListChanged();
            if(!undoer.undoCommand()) {
            	undoer.prepareUndoAdd(task, "deadline");
			}
        } else {
            throw new IllegalValueException("Invalid data type for add");
        }
    }
	//@@author
	
	@Override
	public synchronized void undoLatestCommand() {
		undoer.executeUndo();
	}
	// @@author

	// =========== Filtered TodoList Accessors
	// ===============================================================

	@Override
	public UnmodifiableObservableList<ReadOnlyTask> getFilteredTodoList() {
		return new UnmodifiableObservableList<>(filteredTodos);
	}

	@Override
	public void updateFilteredTodoListToShowAll() {
		filteredTodos.setPredicate(null);
	}

	@Override
	public void updateFilteredTodoList(Set<String> keywords) {
		updateFilteredTodoList(new PredicateExpression(new NameQualifier(keywords)));
	}

	private void updateFilteredTodoList(Expression expression) {
		filteredTodos.setPredicate(expression::satisfies);
	}

	// =========== Filtered EventList Accessors
	// ===============================================================

	@Override
	public UnmodifiableObservableList<ReadOnlyTask> getFilteredEventList() {
		return new UnmodifiableObservableList<>(filteredEvents);
	}

	@Override
	public void updateFilteredEventListToShowAll() {
		filteredEvents.setPredicate(null);
	}

	@Override
	public void updateFilteredEventList(Set<String> keywords) {
		updateFilteredEventList(new PredicateExpression(new NameQualifier(keywords)));
	}

	private void updateFilteredEventList(Expression expression) {
		filteredEvents.setPredicate(expression::satisfies);
	}

	// =========== Filtered DeadlineList Accessors
	// ===============================================================

	@Override
	public UnmodifiableObservableList<ReadOnlyTask> getFilteredDeadlineList() {
		return new UnmodifiableObservableList<>(filteredDeadlines);
	}

	@Override
	public void updateFilteredDeadlineListToShowAll() {
		filteredDeadlines.setPredicate(null);
	}

	@Override
	public void updateFilteredDeadlineList(Set<String> keywords) {
		updateFilteredDeadlineList(new PredicateExpression(new NameQualifier(keywords)));
	}

	private void updateFilteredDeadlineList(Expression expression) {
		filteredDeadlines.setPredicate(expression::satisfies);
	}

	// ========== Inner classes/interfaces used for filtering
	// ==================================================

	interface Expression {
		boolean satisfies(ReadOnlyTask task);

		String toString();
	}

	private class PredicateExpression implements Expression {

		private final Qualifier qualifier;

		PredicateExpression(Qualifier qualifier) {
			this.qualifier = qualifier;
		}

		@Override
		public boolean satisfies(ReadOnlyTask task) {
			return qualifier.run(task);
		}

		@Override
		public String toString() {
			return qualifier.toString();
		}
	}

	interface Qualifier {
		boolean run(ReadOnlyTask task);

		String toString();
	}

	// @@author A0139923X
	private class NameQualifier implements Qualifier {
		private Set<String> nameKeyWords;

		NameQualifier(Set<String> nameKeyWords) {
			this.nameKeyWords = nameKeyWords;
		}

		@Override
		public boolean run(ReadOnlyTask task) {
			if (nameKeyWords.toString().contains("date/")) {
				return nameKeyWords.stream()
						.filter(keyword -> StringUtil.equalsIgnoreCase(task.getStartDate().date, keyword)).findAny()
						.isPresent();
			} else {
				return nameKeyWords.stream()
						.filter(keyword -> StringUtil.equalsIgnoreCase(task.getName().name, keyword)).findAny()
						.isPresent();
			}
		}
		// @@author

		@Override
		public String toString() {
			return "name=" + String.join(", ", nameKeyWords);
		}
	}

}
