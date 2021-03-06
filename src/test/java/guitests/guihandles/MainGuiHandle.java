package guitests.guihandles;

import guitests.GuiRobot;
import javafx.stage.Stage;
import seedu.Tdoo.TestApp;

/**
 * Provides a handle for the main GUI.
 */
public class MainGuiHandle extends GuiHandle {

	public MainGuiHandle(GuiRobot guiRobot, Stage primaryStage) {
		super(guiRobot, primaryStage, TestApp.APP_TITLE);
	}

	public TaskListPanelHandle getTaskListPanel() {
		return new TaskListPanelHandle(guiRobot, primaryStage);
	}

	public EventListPanelHandle getEventListPanel() {
		return new EventListPanelHandle(guiRobot, primaryStage);
	}

	public DeadlineListPanelHandle getDeadlineListPanel() {
		return new DeadlineListPanelHandle(guiRobot, primaryStage);
	}

	public ResultDisplayHandle getResultDisplay() {
		return new ResultDisplayHandle(guiRobot, primaryStage);
	}

	public CommandBoxHandle getCommandBox() {
		return new CommandBoxHandle(guiRobot, primaryStage, TestApp.APP_TITLE);
	}

	public MainMenuHandle getMainMenu() {
		return new MainMenuHandle(guiRobot, primaryStage);
	}

}
