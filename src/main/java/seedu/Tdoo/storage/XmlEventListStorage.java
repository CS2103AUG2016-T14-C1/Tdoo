package seedu.Tdoo.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.Tdoo.commons.core.LogsCenter;
import seedu.Tdoo.commons.exceptions.DataConversionException;
import seedu.Tdoo.commons.util.FileUtil;
import seedu.Tdoo.model.ReadOnlyTaskList;

//@@author A0144061U-reused
/**
 * A class to access TodoList data stored as an xml file on the hard disk.
 */
public class XmlEventListStorage implements TaskListStorage {

	private static final Logger logger = LogsCenter.getLogger(XmlEventListStorage.class);

	private String filePath;

	public XmlEventListStorage(String filePath) {
		this.filePath = filePath;
	}

	public String getTaskListFilePath() {
		return filePath;
	}

	public void setTaskListFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Similar to {@link #readTodoList()}
	 * 
	 * @param filePath
	 *            location of the data. Cannot be null
	 * @throws DataConversionException
	 *             if the file is not in the correct format.
	 */
	public Optional<ReadOnlyTaskList> readTaskList(String filePath)
			throws DataConversionException, FileNotFoundException {
		assert filePath != null;

		File TaskListFile = new File(filePath);

		if (!TaskListFile.exists()) {
			logger.info("TaskList file " + TaskListFile + " not found");
			return Optional.empty();
		}

		ReadOnlyTaskList TaskListOptional = XmlFileStorage.loadDataFromSaveFile(new File(filePath));

		return Optional.of(TaskListOptional);
	}

	/**
	 * Similar to {@link #saveTodoList(ReadOnlyTodoList)}
	 * 
	 * @param filePath
	 *            location of the data. Cannot be null
	 */
	public void saveTaskList(ReadOnlyTaskList taskList, String filePath) throws IOException {
		assert taskList != null;
		assert filePath != null;

		File file = new File(filePath);
		FileUtil.createIfMissing(file);
		XmlFileStorage.saveDataToFile(file, new XmlSerializableEventList(taskList));
	}

	@Override
	public Optional<ReadOnlyTaskList> readTaskList() throws DataConversionException, IOException {
		return readTaskList(filePath);
	}

	@Override
	public void saveTaskList(ReadOnlyTaskList taskList) throws IOException {
		saveTaskList(taskList, filePath);
	}
}
