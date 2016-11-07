package seedu.Tdoo;

import javafx.stage.Screen;
import javafx.stage.Stage;
import seedu.Tdoo.MainApp;
import seedu.Tdoo.commons.core.Config;
import seedu.Tdoo.commons.core.GuiSettings;
import seedu.Tdoo.model.ReadOnlyTaskList;
import seedu.Tdoo.model.UserPrefs;
import seedu.Tdoo.storage.XmlSerializableTodoList;
import seedu.Tdoo.testutil.TestUtil;

import java.util.function.Supplier;

/**
 * This class is meant to override some properties of MainApp so that it will be
 * suited for testing
 */
//@@author A0132157M reused 
public class TestApp extends MainApp {

	public static final String SAVE_LOCATION_FOR_TESTING = TestUtil.getFilePathInSandboxFolder("sampleData.xml");
	public static final String SAVE_LOCATION_FOR_EVENT_TESTING = TestUtil.getFilePathInSandboxFolder("sampleEventData.xml");
	public static final String SAVE_LOCATION_FOR_DEADLINE_TESTING = TestUtil.getFilePathInSandboxFolder("sampleDeadlineData.xml");
	protected static final String DEFAULT_PREF_FILE_LOCATION_FOR_TESTING = TestUtil
			.getFilePathInSandboxFolder("pref_testing.json");
	public static final String APP_TITLE = "Test App";
	protected static final String Todo_BOOK_NAME = "Test";
	protected static final String Event_BOOK_NAME = "TestEvent";
	protected static final String Deadline_BOOK_NAME = "TestDeadline";
	protected Supplier<ReadOnlyTaskList> initialDataSupplier = () -> null;
	protected String saveFileLocation = SAVE_LOCATION_FOR_TESTING;
	protected String saveEventFileLocation = SAVE_LOCATION_FOR_EVENT_TESTING;
	protected String saveDeadlineFileLocation = SAVE_LOCATION_FOR_DEADLINE_TESTING;

	public TestApp() {
	}

	public TestApp(Supplier<ReadOnlyTaskList> initialDataSupplier, String saveFileLocation) {
		super();
		this.initialDataSupplier = initialDataSupplier;
		this.saveFileLocation = saveFileLocation;

		// If some initial local data has been provided, write those to the file
		if (initialDataSupplier.get() != null) {
			TestUtil.createDataFileWithData(new XmlSerializableTodoList(this.initialDataSupplier.get()),
					this.saveFileLocation);
		}
	}

	@Override
	protected Config initConfig(String configFilePath) {
		Config config = super.initConfig(configFilePath);
		config.setAppTitle(APP_TITLE);
		config.setTodoListFilePath(saveFileLocation);
		config.setEventListFilePath(saveEventFileLocation);
	    config.setDeadlineListFilePath(saveDeadlineFileLocation);
		config.setUserPrefsFilePath(DEFAULT_PREF_FILE_LOCATION_FOR_TESTING);
		config.setTodoListName(Todo_BOOK_NAME);
		config.setEventListName(Event_BOOK_NAME);
		config.setDeadlineListName(Deadline_BOOK_NAME);
		return config;
	}

	@Override
	protected UserPrefs initPrefs(Config config) {
		UserPrefs userPrefs = super.initPrefs(config);
		double x = Screen.getPrimary().getVisualBounds().getMinX();
		double y = Screen.getPrimary().getVisualBounds().getMinY();
		userPrefs.updateLastUsedGuiSetting(new GuiSettings(600.0, 600.0, (int) x, (int) y));
		return userPrefs;
	}

	@Override
	public void start(Stage primaryStage) {
		ui.start(primaryStage);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
