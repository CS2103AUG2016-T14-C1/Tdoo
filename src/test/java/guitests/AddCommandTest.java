package guitests;

import guitests.guihandles.DeadlineCardHandle;
import guitests.guihandles.EventCardHandle;
import guitests.guihandles.TaskCardHandle;

import seedu.Tdoo.commons.core.Messages;
import seedu.Tdoo.commons.exceptions.IllegalValueException;
import seedu.Tdoo.logic.commands.*;
import seedu.Tdoo.testutil.TestUtil;

import seedu.Tdoo.testutil.DeadlineBuilder;
import seedu.Tdoo.testutil.EventBuilder;
import seedu.Tdoo.testutil.TaskBuilder;
import seedu.Tdoo.testutil.TestDeadline;
import seedu.Tdoo.testutil.TestEvent;
import seedu.Tdoo.testutil.TestTask;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AddCommandTest extends ListGuiTest {

	@Test
	// @@author A0132157M reused
	public void add() throws IllegalValueException {
		// add one task
		TestTask[] currentList = td.getTypicaltasks();
		TestTask taskToAdd = new TaskBuilder().withName("TODO 123").withStartDate("28-11-2016")
				.withEndDate("29-11-2016").withPriority("1").withDone("false").build();
		assertAddSuccess(taskToAdd, currentList);
		currentList = TestUtil.addTasksToList(currentList, taskToAdd);

		// add one event
		TestEvent[] currentEventList = ed.getTypicalEvent();
		TestEvent eventToAdd = new EventBuilder().withName("EVENT 123").withStartDate("01-01-2017")
				.withEndDate("02-01-2017").withStartTime("01:30").withEndTime("02:00").withDone("false").build();
		assertAddEventSuccess(eventToAdd, currentEventList);
		currentEventList = TestUtil.addEventsToList(currentEventList, eventToAdd);

		// add one deadline
		TestDeadline[] currentDeadlineList = dd.getTypicalDeadline();
		TestDeadline ddToAdd = new DeadlineBuilder().withName("DEADLINE 1").withStartDate("30-11-2017")
				.withEndTime("10:00").withDone("false").build();
		assertAddDeadlineSuccess(ddToAdd, currentDeadlineList);
		currentDeadlineList = TestUtil.addDeadlinesToList(currentDeadlineList, ddToAdd);

		// add another task
		taskToAdd = new TaskBuilder().withName("AnotherTODO 123").withStartDate("29-11-2017").withEndDate("30-11-2017")
				.withPriority("3").withDone("false").build();
		assertAddSuccess(taskToAdd, currentList);
		currentList = TestUtil.addTasksToList(currentList, taskToAdd);

		// add another event
		eventToAdd = new EventBuilder().withName("AnotherEVENT 123").withStartDate("01-03-2017")
				.withEndDate("02-03-2017").withStartTime("01:30").withEndTime("02:00").withDone("false").build();
		assertAddEventSuccess(eventToAdd, currentEventList);
		currentEventList = TestUtil.addEventsToList(currentEventList, eventToAdd);

		// add another deadline
		ddToAdd = new DeadlineBuilder().withName("ANOTHEREADLINE 1").withStartDate("19-11-2017").withEndTime("10:00")
				.withDone("false").build();
		assertAddDeadlineSuccess(ddToAdd, currentDeadlineList);
		currentDeadlineList = TestUtil.addDeadlinesToList(currentDeadlineList, ddToAdd);

		// add duplicate task
		taskToAdd = new TaskBuilder().withName("TODO 123").withStartDate("28-11-2016").withEndDate("29-11-2016")
				.withPriority("1").withDone("false").build();
		commandBox.runCommand(taskToAdd.getAddCommand());
		assertResultMessage(AddCommand.MESSAGE_DUPLICATE_TASK);
		assertTrue(taskListPanel.isListMatching(currentList));

		// add duplicate event
		eventToAdd = new EventBuilder().withName("EVENT 123").withStartDate("01-01-2017").withEndDate("02-01-2017")
				.withStartTime("01:30").withEndTime("02:00").withDone("false").build();
		commandBox.runCommand(eventToAdd.getAddCommand());
		assertResultMessage(AddCommand.MESSAGE_DUPLICATE_TASK);
		assertTrue(eventListPanel.isListMatching(currentEventList));

		// add duplicate deadline
		ddToAdd = new DeadlineBuilder().withName("DEADLINE 1").withStartDate("30-11-2017").withEndTime("10:00")
				.withDone("false").build();
		commandBox.runCommand(ddToAdd.getAddCommand());
		assertResultMessage(AddCommand.MESSAGE_DUPLICATE_TASK);
		assertTrue(deadlineListPanel.isListMatching(currentDeadlineList));

		// invalid command
		commandBox.runCommand("adds assignment 66");
		assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
	}
	// =========================================================================================================================
	
	//@@author A0132157M
	private void addAllDummyTodoTasks(TestTask... currentList) {
		for (TestTask t : currentList) {
			commandBox.runCommand(t.getAddCommand());
		}
	}
	
	//@@author A0132157M
	private void addAllDummyEventTasks(TestEvent... currentList) {
		for (TestEvent t : currentList) {
			commandBox.runCommand(t.getAddCommand());
		}
	}
	//@@author A0132157M reused
	private void assertAddSuccess(TestTask taskToAdd, TestTask... currentList) {
		addAllDummyTodoTasks(currentList);
		commandBox.runCommand(taskToAdd.getAddCommand());
		// confirm the new card contains the right data
		TaskCardHandle addedCard = taskListPanel.navigateTotask(taskToAdd.getName().name);
		assertMatching(taskToAdd, addedCard);
		// confirm the list now contains all previous tasks plus the new task
		TestTask[] expectedList = TestUtil.addTasksToList(currentList, taskToAdd);
		assertTrue(taskListPanel.isListMatching(expectedList));
	}

	// @@author A0132157M
	private void assertAddEventSuccess(TestEvent eventsToAdd, TestEvent... currentList) {
		addAllDummyEventTasks(currentList);
		commandBox.runCommand(eventsToAdd.getAddCommand());
		// confirm the new card contains the right data
		EventCardHandle addedCard = eventListPanel.navigateToevent(eventsToAdd.getName().name);
		assertEventMatching(eventsToAdd, addedCard);

		// confirm the list now contains all previous tasks plus the new task
		TestEvent[] expectedList = TestUtil.addEventsToList(currentList, eventsToAdd);
		assertTrue(eventListPanel.isListMatching(expectedList));
	}

	// @@author A0132157M
	private void assertAddDeadlineSuccess(TestDeadline eventsToAdd, TestDeadline... currentList) {
		commandBox.runCommand(eventsToAdd.getAddCommand());

		// confirm the new card contains the right data
		DeadlineCardHandle addedCard = deadlineListPanel.navigateToDeadline(eventsToAdd.getName().name);
		assertDeadlineMatching(eventsToAdd, addedCard);

		// confirm the list now contains all previous tasks plus the new task
		TestDeadline[] expectedList = TestUtil.addDeadlinesToList(currentList, eventsToAdd);
		assertTrue(deadlineListPanel.isListMatching(expectedList));
	}

}
