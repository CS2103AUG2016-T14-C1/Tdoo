package seedu.todoList.logic;

import com.google.common.eventbus.Subscribe;

import seedu.todoList.commons.core.EventsCenter;
import seedu.todoList.commons.events.model.TodoListChangedEvent;
import seedu.todoList.commons.events.ui.JumpToListRequestEvent;
import seedu.todoList.commons.events.ui.ShowHelpRequestEvent;
import seedu.todoList.logic.Logic;
import seedu.todoList.logic.LogicManager;
import seedu.todoList.logic.commands.*;
import seedu.todoList.model.Model;
import seedu.todoList.model.ModelManager;
import seedu.todoList.model.ReadOnlyTaskList;
import seedu.todoList.model.TaskList;

import seedu.todoList.model.task.*;
import seedu.todoList.model.task.attributes.*;
import seedu.todoList.storage.StorageManager;
import seedu.todoList.logic.commands.AddCommand;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.todoList.commons.core.Messages.*;

public class LogicManagerTest {

    /**
     * See https://github.com/junit-team/junit4/wiki/rules#temporaryfolder-rule
     */
    @Rule
    public TemporaryFolder saveFolder = new TemporaryFolder();

    private Model model;
    private Logic logic;

    //These are for checking the correctness of the events raised
    private ReadOnlyTaskList latestSavedTodoList;
    private boolean helpShown;
    private int targetedJumpIndex;

    @Subscribe
    private void handleLocalModelChangedEvent(TodoListChangedEvent abce) {
        latestSavedTodoList = new TaskList(abce.data);
    }

    @Subscribe
    private void handleShowHelpRequestEvent(ShowHelpRequestEvent she) {
        helpShown = true;
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent je) {
        targetedJumpIndex = je.targetIndex;
    }

    @Before
    public void setup() {
        model = new ModelManager();
        String tempTodoListFile = saveFolder.getRoot().getPath() + "TempTodoList.xml";
        String tempEventListFile = saveFolder.getRoot().getPath() + "TempEventList.xml";
        String tempDeadlineListFile = saveFolder.getRoot().getPath() + "TempDeadlineList.xml";
        String tempPreferencesFile = saveFolder.getRoot().getPath() + "TempPreferences.json";
        logic = new LogicManager(model, new StorageManager(tempTodoListFile, tempEventListFile, tempDeadlineListFile, tempPreferencesFile));
        EventsCenter.getInstance().registerHandler(this);

        latestSavedTodoList = new TaskList(model.getTodoList()); // last saved assumed to be up to date before.
        helpShown = false;
        targetedJumpIndex = -1; // non yet
    }

    @After
    public void teardown() {
        EventsCenter.clearSubscribers();
    }

    @Test
    public void execute_invalid() throws Exception {
        String invalidCommand = "       ";
        assertCommandBehavior(invalidCommand,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
    }

    /**
     * Executes the command and confirms that the result message is correct.
     * Both the 'TodoList' and the 'last shown list' are expected to be empty.
     * @see #assertCommandBehavior(String, String, ReadOnlyTodoList, List)
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage) throws Exception {
        assertCommandBehavior(inputCommand, expectedMessage, new TaskList(), Collections.emptyList());
    }

    /**
     * Executes the command and confirms that the result message is correct and
     * also confirms that the following three parts of the LogicManager object's state are as expected:<br>
     *      - the internal TodoList data are same as those in the {@code expectedTodoList} <br>
     *      - the backing list shown by UI matches the {@code shownList} <br>
     *      - {@code expectedTodoList} was saved to the storage file. <br>
     */
    private void assertCommandBehavior(String inputCommand, String expectedMessage,
                                       ReadOnlyTaskList expectedTodoList,
                                       List<? extends ReadOnlyTask> expectedShownList) throws Exception {

        //Execute the command
        CommandResult result = logic.execute(inputCommand);

        //Confirm the ui display elements should contain the right data
        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedShownList, model.getFilteredTodoList());

        //Confirm the state of data (saved and in-memory) is as expected
        assertEquals(expectedTodoList, model.getTodoList());
        assertEquals(expectedTodoList, latestSavedTodoList);
    }


    @Test
    public void execute_unknownCommandWord() throws Exception {
        String unknownCommand = "uicfhmowqewca";
        assertCommandBehavior(unknownCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_help() throws Exception {
        assertCommandBehavior("help", HelpCommand.SHOWING_HELP_MESSAGE);
        assertTrue(helpShown);
    }

    @Test
    public void execute_exit() throws Exception {
        assertCommandBehavior("exit", ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT);
    }

    @Test
    public void execute_clear() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        model.addTask(helper.generatetask(1));
        model.addTask(helper.generatetask(2));
        model.addTask(helper.generatetask(3));

        assertCommandBehavior("clear", ClearCommand.MESSAGE_SUCCESS, new TaskList(), Collections.emptyList());
    }


    @Test
    public void execute_add_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);
        assertCommandBehavior(
                "add wrong args wrong args", expectedMessage);
        assertCommandBehavior(
                "add Valid Name 12345 e/valid@email.butNoPhonePrefix a/valid, Todo", expectedMessage);
        assertCommandBehavior(
                "add Valid Name p/12345 valid@email.butNoPrefix a/valid, Todo", expectedMessage);
        assertCommandBehavior(
                "add Valid Name p/12345 e/valid@email.butNoTodoPrefix valid, Todo", expectedMessage);
    }

    @Test
    public void execute_add_invalidtaskData() throws Exception {
        assertCommandBehavior(
                "add []\\[;] p/12345 e/valid@e.mail a/valid, Todo", Name.MESSAGE_NAME_CONSTRAINTS);
        assertCommandBehavior(
                "add Valid Name p/not_numbers e/valid@e.mail a/valid, Todo", StartDate.MESSAGE_DATE_CONSTRAINTS);
        assertCommandBehavior(
                "add Valid Name p/12345 e/notAnEmail a/valid, Todo", Priority.MESSAGE_PRIORITY_CONSTRAINTS);

    }

    @Test
    public void execute_add_successful() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();

        Todo toBeAdded = helper.a111();
        TaskList expectedAB = new TaskList();

        expectedAB.addTask(toBeAdded);

        // execute command and verify result
        assertCommandBehavior(helper.generateAddCommand(toBeAdded),
                String.format(AddCommand.MESSAGE_SUCCESS, toBeAdded),
                expectedAB,
                expectedAB.getTaskList());

    }

    @Test
    public void execute_addDuplicate_notAllowed() throws Exception {
        // setup expectations
        TestDataHelper helper = new TestDataHelper();

        Todo toBeAdded = helper.a111();
        TaskList expectedAB = new TaskList();

        expectedAB.addTask(toBeAdded);

        // setup starting state
        model.addTask(toBeAdded); // task already in internal TodoList

        // execute command and verify result
        assertCommandBehavior(
                helper.generateAddCommand(toBeAdded),
                AddCommand.MESSAGE_DUPLICATE_TASK,
                expectedAB,
                expectedAB.getTaskList());

    }


    @Test
    public void execute_list_showsAlltasks() throws Exception {
        // prepare expectations
        TestDataHelper helper = new TestDataHelper();
        TaskList expectedAB = helper.generateTodoList(2);
        List<? extends ReadOnlyTask> expectedList = expectedAB.getTaskList();

        // prepare TodoList state
        helper.addToModel(model, 2);

        assertCommandBehavior("list",
                ListCommand.MESSAGE_SUCCESS,
                expectedAB,
                expectedList);
    }


    /**
     * Confirms the 'invalid argument index number behaviour' for the given command
     * targeting a single task in the shown list, using visible index.
     * @param commandWord to test assuming it targets a single task in the last shown list based on visible index.
     */
    private void assertIncorrectIndexFormatBehaviorForCommand(String commandWord, String expectedMessage) throws Exception {
        assertCommandBehavior(commandWord , expectedMessage); //index missing
        assertCommandBehavior(commandWord + " +1", expectedMessage); //index should be unsigned
        assertCommandBehavior(commandWord + " -1", expectedMessage); //index should be unsigned
        assertCommandBehavior(commandWord + " 0", expectedMessage); //index cannot be 0
        assertCommandBehavior(commandWord + " not_a_number", expectedMessage);
    }

    /**
     * Confirms the 'invalid argument index number behaviour' for the given command
     * targeting a single task in the shown list, using visible index.
     * @param commandWord to test assuming it targets a single task in the last shown list based on visible index.
     */
    private void assertIndexNotFoundBehaviorForCommand(String commandWord) throws Exception {
        String expectedMessage = MESSAGE_INVALID_task_DISPLAYED_INDEX;
        TestDataHelper helper = new TestDataHelper();
        List<Task> taskList = helper.generatetaskList(2);

        // set AB state to 2 tasks
        model.resetTodoListData(new TaskList());
        for (Task p : taskList) {
            model.addTask(p);
        }

        assertCommandBehavior(commandWord + " 3", expectedMessage, model.getTodoList(), taskList);
    }

    @Test
    public void execute_selectInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("select", expectedMessage);
    }

    /*@Test
    public void execute_selectIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("select");
    }

    @Test
    public void execute_select_jumpsToCorrecttask() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Task> threetasks = helper.generatetaskList(3);

        TaskList expectedAB = helper.generateTodoList(threetasks);
        helper.addToModel(model, threetasks);

        assertCommandBehavior("select 2",
                String.format(SelectCommand.MESSAGE_SELECT_task_SUCCESS, 2),
                expectedAB,
                expectedAB.getTaskList());
        assertEquals(1, targetedJumpIndex);
        assertEquals(model.getFilteredTodoList().get(1), threetasks.get(1));
    }*/


    @Test
    public void execute_deleteInvalidArgsFormat_errorMessageShown() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE);
        assertIncorrectIndexFormatBehaviorForCommand("delete", expectedMessage);
    }

    @Test
    public void execute_deleteIndexNotFound_errorMessageShown() throws Exception {
        assertIndexNotFoundBehaviorForCommand("delete");
    }

    @Test
    public void execute_delete_removesCorrecttask() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        List<Task> threetasks = helper.generatetaskList(3);

        TaskList expectedAB = helper.generateTodoList(threetasks);
        expectedAB.removeTask(threetasks.get(1));
        helper.addToModel(model, threetasks);

        assertCommandBehavior("delete 2",
                String.format(DeleteCommand.MESSAGE_DELETE_task_SUCCESS, threetasks.get(1)),
                expectedAB,
                expectedAB.getTaskList());
    }


    @Test
    public void execute_find_invalidArgsFormat() throws Exception {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE);
        assertCommandBehavior("find ", expectedMessage);
    }

    @Test
    //@@ Author A0132157M
    public void execute_find_onlyMatchesFullWordsInNames() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task pTarget1 = helper.generatetaskWithToDo("bla bla KEY bla");
        Task pTarget2 = helper.generatetaskWithToDo("bla KEY bla bceofeia");
        Task p1 = helper.generatetaskWithToDo("KE Y");
        Task p2 = helper.generatetaskWithToDo("KEYKEYKEY sduauo");

        List<Task> fourtasks = helper.generatetaskList(p1, pTarget1, p2, pTarget2);
        TaskList expectedAB = helper.generateTodoList(fourtasks);
        List<Task> expectedList = helper.generatetaskList(pTarget1, pTarget2);
        helper.addToModel(model, fourtasks);

        assertCommandBehavior("find KEY",
                Command.getMessageFortaskListShownSummary(expectedList.size()),
                expectedAB,
                expectedList);
    }

    @Test
    public void execute_find_isNotCaseSensitive() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task p1 = helper.generatetaskWithToDo("bla bla KEY bla");
        Task p2 = helper.generatetaskWithToDo("bla KEY bla bceofeia");
        Task p3 = helper.generatetaskWithToDo("key key");
        Task p4 = helper.generatetaskWithToDo("KEy sduauo");

        List<Task> fourtasks = helper.generatetaskList(p3, p1, p4, p2);
        TaskList expectedAB = helper.generateTodoList(fourtasks);
        List<Task> expectedList = fourtasks;
        helper.addToModel(model, fourtasks);

        assertCommandBehavior("find KEY",
                Command.getMessageFortaskListShownSummary(expectedList.size()),
                expectedAB,
                expectedList);
    }

    @Test
    public void execute_find_matchesIfAnyKeywordPresent() throws Exception {
        TestDataHelper helper = new TestDataHelper();
        Task pTarget1 = helper.generatetaskWithToDo("bla bla KEY bla");
        Task pTarget2 = helper.generatetaskWithToDo("bla rAnDoM bla bceofeia");
        Task pTarget3 = helper.generatetaskWithToDo("key key");
        Task p1 = helper.generatetaskWithToDo("sduauo");

        List<Task> fourtasks = helper.generatetaskList(pTarget1, p1, pTarget2, pTarget3);
        TaskList expectedAB = helper.generateTodoList(fourtasks);
        List<Task> expectedList = helper.generatetaskList(pTarget1, pTarget2, pTarget3);
        helper.addToModel(model, fourtasks);

        assertCommandBehavior("find key rAnDoM",
                Command.getMessageFortaskListShownSummary(expectedList.size()),
                expectedAB,
                expectedList);
    }


    /**
     * A utility class to generate test data.
     */
    //@@ Author A0132157M
    class TestDataHelper{

        Todo a111() throws Exception {
            Name name = new Name("Assignment 111");
            StartDate sdate = new StartDate("04-11-2017");
            EndDate edate = new EndDate("02-12-2017");
            Priority priority = new Priority("111");
            Done done = new Done("done");
            
            //EndTime endTime = new EndTime("1111");
            //Tag tag1 = new Tag("tag1");
            //Tag tag2 = new Tag("tag2");
            //UniqueTagList tags = new UniqueTagList(tag1, tag2);
            return new Todo(name, sdate, edate, priority, done);
        }

        /**
         * Generates a valid task using the given seed.
         * Running this function with the same parameter values guarantees the returned task will have the same state.
         * Each unique seed will generate a unique task object.
         *
         * @param seed used to generate the task data field values
         */
        //@@ Author A0132157M
        Task generatetask(int seed) throws Exception {
            return new Todo(
                    new Name("task " + seed),
                    new StartDate("4th October 2018"),
                    new EndDate("4th October 2019"),
                    new Priority( "priority " + seed),
                    new Done("done")
                    //new EndTime("EndTime " + seed)
                    //new UniqueTagList(new Tag("tag" + Math.abs(seed)), new Tag("tag" + Math.abs(seed + 1)))
            );
        }

        /** Generates the correct add command based on the task given */
      //@@ Author A0132157M
        String generateAddCommand(Todo p) {
            StringBuffer cmd = new StringBuffer();

            cmd.append("add ");

            cmd.append(p.getName().name);
            cmd.append(" from").append(p.getStartDate().date);
            cmd.append(" to").append(p.getEndDate().endDate);
            cmd.append(" p/").append(p.getPriority().priority);
            //cmd.append(" e/").append(p.getEndTime().endTime);

            /*UniqueTagList tags = p.getTags();
            for(Tag t: tags){
                cmd.append(" t/").append(t.tagName);
            }*/

            return cmd.toString();
        }

        /**
         * Generates an TodoList with auto-generated tasks.
         */
        TaskList generateTodoList(int numGenerated) throws Exception{
            TaskList TodoList = new TaskList();
            addToTodoList(TodoList, numGenerated);
            return TodoList;
        }

        /**
         * Generates an TodoList based on the list of tasks given.
         */
        TaskList generateTodoList(List<Task> tasks) throws Exception{
            TaskList TodoList = new TaskList();
            addToTodoList(TodoList, tasks);
            return TodoList;
        }

        /**
         * Adds auto-generated task objects to the given TodoList
         * @param TodoList The TodoList to which the tasks will be added
         */
        void addToTodoList(TaskList TodoList, int numGenerated) throws Exception{
            addToTodoList(TodoList, generatetaskList(numGenerated));
        }

        /**
         * Adds the given list of tasks to the given TodoList
         */
        void addToTodoList(TaskList TodoList, List<Task> tasksToAdd) throws Exception{
            for(Task p: tasksToAdd){
                TodoList.addTask(p);
            }
        }

        /**
         * Adds auto-generated task objects to the given model
         * @param model The model to which the tasks will be added
         */
        void addToModel(Model model, int numGenerated) throws Exception{
            addToModel(model, generatetaskList(numGenerated));
        }

        /**
         * Adds the given list of tasks to the given model
         */
        void addToModel(Model model, List<Task> tasksToAdd) throws Exception{
            for(Task p: tasksToAdd){
                model.addTask(p);
            }
        }

        /**
         * Generates a list of tasks based on the flags.
         */
        List<Task> generatetaskList(int numGenerated) throws Exception{
            List<Task> tasks = new ArrayList<>();
            for(int i = 1; i <= numGenerated; i++){
                tasks.add(generatetask(i));
            }
            return tasks;
        }

        List<Task> generatetaskList(Task... tasks) {
            return Arrays.asList(tasks);
        }

        /**
         * Generates a task object with given name. Other fields will have some dummy values.
         */
        //@@ Author A0132157M
        Task generatetaskWithToDo(String name) throws Exception {
            return new Todo(
                    new Name(name),
                    new StartDate("01-11-2016"),
                    new EndDate("02-11-2016"),
                    new Priority("1"),
                    new Done("done")
                    
                    
                    //new UniqueTagList(new Tag("tag"))
            );
        }
    }
}
