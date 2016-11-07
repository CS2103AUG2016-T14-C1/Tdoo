package guitests;

import org.junit.Test;

import seedu.Tdoo.commons.exceptions.IllegalValueException;
import seedu.Tdoo.testutil.DeadlineBuilder;
import seedu.Tdoo.testutil.EventBuilder;
import seedu.Tdoo.testutil.TaskBuilder;
import seedu.Tdoo.testutil.TestDeadline;
import seedu.Tdoo.testutil.TestEvent;
import seedu.Tdoo.testutil.TestTask;
import static org.junit.Assert.assertTrue;

//@@author A0144061U
public class UndoCommandTest extends ListGuiTest {

	public static final String MESSAGE_UNDO_SUCCESS = "Undone the latest task.";

	@Test
	public void undo() throws IllegalValueException {

		TestTask[] currentTodoList = new TestTask[] {
				new TaskBuilder().withName("TODO123").withStartDate("28-11-2016").withEndDate("29-11-2016").withPriority("1").withDone("false").build(),
						new TaskBuilder().withName("TODO456").withStartDate("01-12-2016").withEndDate("02-12-2016").withPriority("1").withDone("false").build(),
								new TaskBuilder().withName("TODO789").withStartDate("03-12-2016").withEndDate("04-12-2016").withPriority("1").withDone("false").build(),
										new TaskBuilder().withName("TODO101112").withStartDate("03-12-2016").withEndDate("04-12-2016").withPriority("1").withDone("false").build()
		};
		addAllDummyTodoTasks(currentTodoList);
		assertUndoAddTodoSuccess(currentTodoList);
		assertUndoDeleteTodoSuccess(4, currentTodoList);
		assertUndoClearTodoSuccess(currentTodoList);
		assertUndoEditTodoSuccess(1, currentTodoList);

		TestEvent[] currentEventList = new TestEvent[] {
				new EventBuilder().withName("Event123").withStartDate("11-12-2016").withEndDate("12-12-2016").withStartTime("01:00").withEndTime("01:30").withDone("false").build(),
						new EventBuilder().withName("Event456").withStartDate("17-11-2016").withEndDate("18-11-2016").withStartTime("01:30").withEndTime("20:00").withDone("false").build(),
								new EventBuilder().withName("Eeambuilding3").withStartDate("19-11-2016").withEndDate("20-11-2016").withStartTime("01:30").withEndTime("02:00").withDone("false").build(),
										new EventBuilder().withName("Essignment4").withStartDate("11-12-2016").withEndDate("12-12-2016").withStartTime("01:00").withEndTime("01:30").withDone("false").build()
		};
		addAllDummyEventTasks(currentEventList);
		assertUndoAddEventSuccess(currentEventList);
		assertUndoDeleteEventSuccess(4, currentEventList);
		assertUndoClearEventSuccess(currentEventList);
		assertUndoEditEventSuccess(1, currentEventList);

		//delete deadlines in deadline list
		TestDeadline[] currentDeadlineList = new TestDeadline[] {
				new DeadlineBuilder().withName("d1").withStartDate("15-11-2017").withEndTime("10:00").withDone("false").build(),
						new DeadlineBuilder().withName("dd1").withStartDate("16-11-2017").withEndTime("12:00").withDone("false").build(),
								new DeadlineBuilder().withName("ddd3").withStartDate("17-11-2017").withEndTime("13:00").withDone("false").build(),
										new DeadlineBuilder().withName("dddd3").withStartDate("18-11-2017").withEndTime("13:00").withDone("false").build()
		};
		addAllDummyDeadlineTasks(currentDeadlineList);
		assertUndoAddDeadlineSuccess(currentDeadlineList);
		assertUndoDeleteDeadlineSuccess(4, currentDeadlineList);
		assertUndoClearDeadlineSuccess(currentDeadlineList);
		assertUndoEditDeadlineSuccess(1, currentDeadlineList);
	}

	private void addAllDummyTodoTasks(TestTask... currentList) {
		for(TestTask t:currentList ) {
			commandBox.runCommand(t.getAddCommand());
		}
	}
	private void addAllDummyEventTasks(TestEvent... currentList) {
		for(TestEvent t:currentList ) {
			commandBox.runCommand(t.getAddCommand());
		}
	}
	private void addAllDummyDeadlineTasks(TestDeadline... currentList) {
		for(TestDeadline t:currentList ) {
			commandBox.runCommand(t.getAddCommand());
		}
	}

	/**
	 * Runs the undo command to undo the latest add command and confirms the
 result is correct.
	 * @param currentList A copy of the current list of task (before undo).
	 */
	private void assertUndoAddTodoSuccess(final TestTask[] currentList) throws
	IllegalValueException {
		TestTask taskToAdd = new TaskBuilder().withName("TODO1234").withStartDate("28-11-2016").withEndDate("29-11-2016").withPriority("1").withDone("false").build();
				TestTask[] expectedRemainder = currentList;
				commandBox.runCommand(taskToAdd.getAddCommand());
				commandBox.runCommand("undo");

				//confirm the list reverted back to the original list after undo
				assertTrue(taskListPanel.isListMatching(expectedRemainder));

				//confirm the result message is correct
				assertResultMessage(MESSAGE_UNDO_SUCCESS);
	}

	private void assertUndoAddEventSuccess(final TestEvent[] currentList) throws
	IllegalValueException {
		TestEvent eventToAdd = new EventBuilder().withName("EVENT123").withStartDate("01-01-2017").withEndDate("02-01-2017").withStartTime("01:30").withEndTime("02:00").withDone("false").build();
				TestEvent[] expectedRemainder = currentList;
				commandBox.runCommand(eventToAdd.getAddCommand());
				commandBox.runCommand("undo");

				//confirm the list reverted back to the original list after undo
				assertTrue(eventListPanel.isListMatching(expectedRemainder));

				//confirm the result message is correct
				assertResultMessage(MESSAGE_UNDO_SUCCESS);
	}

	private void assertUndoAddDeadlineSuccess(final TestDeadline[] currentList)
			throws IllegalValueException {
		TestDeadline deadlineToAdd = new DeadlineBuilder().withName("DEADLINE1").withStartDate("30-11-2017").withEndTime("10:00").withDone("false").build();
				TestDeadline[] expectedRemainder = currentList;
		commandBox.runCommand(deadlineToAdd.getAddCommand());
		commandBox.runCommand("undo");

		//confirm the list reverted back to the original list after undo
		assertTrue(deadlineListPanel.isListMatching(expectedRemainder));

		//confirm the result message is correct
		assertResultMessage(MESSAGE_UNDO_SUCCESS);
	}

	/**
	 * Runs the undo command to undo the latest delete command and confirms the
 result is correct.
	 * @param currentList A copy of the current list of task (before undo).
	 */
	private void assertUndoDeleteTodoSuccess(int targetIndexOneIndexed, final
			TestTask[] currentList) throws IllegalValueException {
		TestTask[] expectedRemainder = currentList;
		commandBox.runCommand("delete todo " + targetIndexOneIndexed);
		commandBox.runCommand("undo");

		//confirm the list reverted back to the original list after undo
		assertTrue(taskListPanel.isListMatching(expectedRemainder));

		//confirm the result message is correct
		assertResultMessage(MESSAGE_UNDO_SUCCESS);
	}

	private void assertUndoDeleteEventSuccess(int targetIndexOneIndexed, final
			TestEvent[] currentList) throws IllegalValueException {
		TestEvent[] expectedRemainder = currentList;
		commandBox.runCommand("delete event " + targetIndexOneIndexed);
		commandBox.runCommand("undo");

		//confirm the list reverted back to the original list after undo
		assertTrue(eventListPanel.isListMatching(expectedRemainder));

		//confirm the result message is correct
		assertResultMessage(MESSAGE_UNDO_SUCCESS);
	}

	private void assertUndoDeleteDeadlineSuccess(int targetIndexOneIndexed, final
			TestDeadline[] currentList) throws IllegalValueException {
		TestDeadline[] expectedRemainder = currentList;
		commandBox.runCommand("delete deadline " + targetIndexOneIndexed);
		commandBox.runCommand("undo");

		//confirm the list reverted back to the original list after undo
		assertTrue(deadlineListPanel.isListMatching(expectedRemainder));

		//confirm the result message is correct
		assertResultMessage(MESSAGE_UNDO_SUCCESS);
	}

	/**
	 * Runs the undo command to undo the latest clear command and confirms the
 result is correct.
	 * @param currentList A copy of the current list of task (before undo).
	 */
	private void assertUndoClearTodoSuccess(final TestTask[] currentList) throws
	IllegalValueException {
		TestTask[] expectedRemainder = currentList;
		commandBox.runCommand("clear todo");
		commandBox.runCommand("undo");

		//confirm the list reverted back to the original list after undo
		assertTrue(taskListPanel.isListMatching(expectedRemainder));

		//confirm the result message is correct
		assertResultMessage(MESSAGE_UNDO_SUCCESS);
	}

	private void assertUndoClearEventSuccess(final TestEvent[] currentList)
			throws IllegalValueException {
		TestEvent[] expectedRemainder = currentList;
		commandBox.runCommand("clear event");
		commandBox.runCommand("undo");

		//confirm the list reverted back to the original list after undo
		assertTrue(eventListPanel.isListMatching(expectedRemainder));

		//confirm the result message is correct
		assertResultMessage(MESSAGE_UNDO_SUCCESS);
	}

	private void assertUndoClearDeadlineSuccess(final TestDeadline[] currentList)
			throws IllegalValueException {
		TestDeadline[] expectedRemainder = currentList;
		commandBox.runCommand("clear deadline");
		commandBox.runCommand("undo");

		//confirm the list reverted back to the original list after undo
		assertTrue(deadlineListPanel.isListMatching(expectedRemainder));

		//confirm the result message is correct
		assertResultMessage(MESSAGE_UNDO_SUCCESS);
	}

	/**
	 * Runs the undo command to undo the latest edit command and confirms the
 result is correct.
	 * @param currentList A copy of the current list of task (before undo).
	 */
	private void assertUndoEditTodoSuccess(int targetIndexOneIndexed, final
			TestTask[] currentList) throws IllegalValueException {
		TestTask[] expectedRemainder = currentList;
		String index = Integer.toString(targetIndexOneIndexed);
		commandBox.runCommand("edit todo " + index + " name/DummyEditTodo p/1");
		commandBox.runCommand("undo");

		//confirm the list reverted back to the original list after undo
		assertTrue(taskListPanel.isListMatching(expectedRemainder));

		//confirm the result message is correct
		assertResultMessage(MESSAGE_UNDO_SUCCESS);
	}

	private void assertUndoEditEventSuccess(int targetIndexOneIndexed, final
			TestEvent[] currentList) throws IllegalValueException {
		TestEvent[] expectedRemainder = currentList;
		String index = Integer.toString(targetIndexOneIndexed);
		commandBox.runCommand("edit event " + index + " name/DummyEditEvent from/25-12-2016 to/26-12-2016 at/12:00 to/16:00");
				commandBox.runCommand("undo");

		//confirm the list reverted back to the original list after undo
		assertTrue(eventListPanel.isListMatching(expectedRemainder));

		//confirm the result message is correct
		assertResultMessage(MESSAGE_UNDO_SUCCESS);
	}

	private void assertUndoEditDeadlineSuccess(int targetIndexOneIndexed, final
			TestDeadline[] currentList) throws IllegalValueException {
		TestDeadline[] expectedRemainder = currentList;
		String index = Integer.toString(targetIndexOneIndexed);
		commandBox.runCommand("edit deadline " + index + " name/DummyEditDeadilne on/25-12-2016 at/14:00");
				commandBox.runCommand("undo");

		//confirm the list reverted back to the original list after undo
		assertTrue(deadlineListPanel.isListMatching(expectedRemainder));

		//confirm the result message is correct
		assertResultMessage(MESSAGE_UNDO_SUCCESS);
	}
}
