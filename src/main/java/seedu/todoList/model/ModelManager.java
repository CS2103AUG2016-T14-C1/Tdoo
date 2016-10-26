package seedu.todoList.model;

import javafx.collections.transformation.FilteredList;
import seedu.todoList.commons.core.ComponentManager;
import seedu.todoList.commons.core.LogsCenter;
import seedu.todoList.commons.core.UnmodifiableObservableList;
import seedu.todoList.commons.events.model.*;
import seedu.todoList.commons.util.StringUtil;
import seedu.todoList.model.task.*;
import seedu.todoList.model.task.UniqueTaskList.TaskNotFoundException;
import seedu.todoList.commons.exceptions.*;

import java.util.Set;
import java.util.logging.Logger;

/**
 * Represents the in-memory model of the TodoList data.
 * All changes to any model should be synchronized.
 */
public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final TaskList todoList;
    private final TaskList eventList;
    private final TaskList deadlineList;
    private final FilteredList<Task> filteredTodos;
    private final FilteredList<Task> filteredEvents;
    private final FilteredList<Task> filteredDeadlines;

    /**
     * Initializes a ModelManager with the given TodoList
     * TodoList and its variables should not be null
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
    }

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
    public void resetTodoListData() {
        todoList.resetData();
        indicateTodoListChanged();
    } 
    @Override
    public void resetEventListData() {
        eventList.resetData();
        indicateEventListChanged();
    } 
    @Override
    public void resetDeadlineListData() {
        deadlineList.resetData();
        indicateDeadlineListChanged();
    }

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
    
    @Override
    public synchronized void editTask(ReadOnlyTask target, String dataType, Task task) throws IllegalValueException, TaskNotFoundException {
    	
        /*
         *  Scenario: User wants to edit todo to change to event or deadline
         *  User enters edit todo 1 n/test d/01-01-2016 e/1000
         *  This will remove index 1 of todolist and add into deadlinelist
         *  
         *  During edit process, example: edit todo 1 with parameters of deadline
         *  todolist will remove the task index 1 and deadlinelist will add the edited task
         */
    	if(task instanceof Todo) {
    	    if(dataType.equals("todo")){
                todoList.addTask(task);
                todoList.removeTask(target);
            }else if(dataType.equals("event")){
                todoList.addTask(task);
                eventList.removeTask(target);
            }else if(dataType.equals("deadline")){
                todoList.addTask(task);
                deadlineList.removeTask(target);
            }
    		updateFilteredTodoListToShowAll();
    		indicateTodoListChanged();
    	}
    	else if(task instanceof Event) {
    	    if(dataType.equals("todo")){
                eventList.addTask(task);
                todoList.removeTask(target);
            }else if(dataType.equals("event")){
                eventList.addTask(task);
                eventList.removeTask(target);
            }else if(dataType.equals("deadline")){
                eventList.addTask(task);
                deadlineList.removeTask(target);
            }
    		updateFilteredEventListToShowAll();
    		indicateEventListChanged();
    	}
    	else if(task instanceof Deadline) {
    	    if(dataType.equals("todo")){
    	        deadlineList.addTask(task);
    	        todoList.removeTask(target);
    	    }else if(dataType.equals("event")){
    	        deadlineList.addTask(task);
                eventList.removeTask(target);
    	    }else if(dataType.equals("deadline")){
    	        deadlineList.addTask(task);
                deadlineList.removeTask(target);
    	    }
    		updateFilteredDeadlineListToShowAll();
    		indicateDeadlineListChanged();
    	}
    }

    @Override
    public synchronized void doneTask(ReadOnlyTask target, String dataType) throws TaskNotFoundException {
    	switch(dataType) {
    		case "todo":
    			todoList.doneTask(target);
    			indicateTodoListChanged();
    		case "event":
    			eventList.doneTask(target);
    			indicateEventListChanged();
    		case "deadline":
    			deadlineList.doneTask(target);
    			indicateDeadlineListChanged();
    	}
    }
    
    @Override
    public synchronized void deleteTask(ReadOnlyTask target, String dataType) throws TaskNotFoundException {
    	switch(dataType) {
    		case "todo":
    			todoList.removeTask(target);
    			indicateTodoListChanged();
    		case "event":
    			eventList.removeTask(target);
    			indicateEventListChanged();
    		case "deadline":
    			deadlineList.removeTask(target);
    			indicateDeadlineListChanged();
    	}
    }

    @Override
    public synchronized void addTask(Task task) throws IllegalValueException, UniqueTaskList.DuplicatetaskException {
    	if(task instanceof Todo) {
    		todoList.addTask(task);
    		updateFilteredTodoListToShowAll();
    		indicateTodoListChanged();
    	}
    	else if(task instanceof Event) {
    		eventList.addTask(task);
    		updateFilteredEventListToShowAll();
    		indicateEventListChanged();
    	}
    	else if(task instanceof Deadline) {
    		deadlineList.addTask(task);
    		updateFilteredDeadlineListToShowAll();
    		indicateDeadlineListChanged();
    	}
    	else {
    		throw new IllegalValueException("Invalid data type for add");
    	}
    }


    //=========== Filtered TodoList Accessors ===============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTodoList() {
        return new UnmodifiableObservableList<>(filteredTodos);
    }


    @Override
    public void updateFilteredTodoListToShowAll() {
        filteredTodos.setPredicate(null);
    }

    @Override
    public void updateFilteredTodoList(Set<String> keywords){
        updateFilteredTodoList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredTodoList(Expression expression) {
        filteredTodos.setPredicate(expression::satisfies);
    }
    
    //=========== Filtered EventList Accessors ===============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredEventList() {
        return new UnmodifiableObservableList<>(filteredEvents);
    }

    @Override
    public void updateFilteredEventListToShowAll() {
        filteredEvents.setPredicate(null);
    }

    @Override
    public void updateFilteredEventList(Set<String> keywords){
        updateFilteredEventList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredEventList(Expression expression) {
        filteredEvents.setPredicate(expression::satisfies);
    }
    
    //=========== Filtered DeadlineList Accessors ===============================================================

    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredDeadlineList() {
        return new UnmodifiableObservableList<>(filteredDeadlines);
    }

    @Override
    public void updateFilteredDeadlineListToShowAll() {
        filteredDeadlines.setPredicate(null);
    }

    @Override
    public void updateFilteredDeadlineList(Set<String> keywords){
        updateFilteredDeadlineList(new PredicateExpression(new NameQualifier(keywords)));
    }

    private void updateFilteredDeadlineList(Expression expression) {
        filteredDeadlines.setPredicate(expression::satisfies);
    }

    //========== Inner classes/interfaces used for filtering ==================================================

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

    private class NameQualifier implements Qualifier {
        private Set<String> nameKeyWords;

        NameQualifier(Set<String> nameKeyWords) {
            this.nameKeyWords = nameKeyWords;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            return nameKeyWords.stream()
                    .filter(keyword -> StringUtil.containsIgnoreCase(task.getName().name, keyword))
                    .findAny()
                    .isPresent();
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
    }

}
