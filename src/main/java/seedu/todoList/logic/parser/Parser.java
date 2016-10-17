package seedu.todoList.logic.parser;

import seedu.todoList.logic.commands.*;
import seedu.todoList.commons.exceptions.IllegalValueException;
import seedu.todoList.commons.util.StringUtil;

import static seedu.todoList.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.todoList.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses user input.
 */
public class Parser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private static final Pattern task_INDEX_ARGS_FORMAT = Pattern.compile("(?<targetIndex>.+)");
    private static final Pattern task_DATATYPE_ARGS_FORMAT = Pattern.compile("(?<dataType>.+)");
    
    private static final Pattern KEYWORDS_ARGS_FORMAT =
            Pattern.compile("(?<keywords>\\S+(?:\\s+\\S+)*)"); // one or more keywords separated by whitespace

    private static final Pattern task_DATA_ARGS_FORMAT = // '/' forward slashes are reserved for delimiter prefixes
            Pattern.compile("(?<name>[^/]+)"
                    + " d/(?<date>[^/]+)"
                    + " p/(?<priority>[^/]+)");
    
    private static final Pattern event_DATA_ARGS_FORMAT = // '/' forward slashes are reserved for delimiter prefixes
            Pattern.compile("(?<name>[^/]+)"
                    + " d/(?<date>[^/]+)"
                    + " s/(?<startTime>[^/]+)"
                    + " e/(?<endTime>[^/]+)");
    
    private static final Pattern deadline_DATA_ARGS_FORMAT = // '/' forward slashes are reserved for delimiter prefixes
            Pattern.compile("(?<name>[^/]+)"
                    + " d/(?<date>[^/]+)"
                    + " e/(?<endTime>[^/]+)");

    public Parser() {}

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     */
    public Command parseCommand(String userInput) {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {

        case AddCommand.COMMAND_WORD:
            return prepareAdd(arguments);   

        case SelectCommand.COMMAND_WORD:
            return prepareSelect(arguments);

        case DeleteCommand.COMMAND_WORD:
            return prepareDelete(arguments);

//        case EditCommand.COMMAND_WORD:
//            return prepareEdit(arguments);
        
        case ClearCommand.COMMAND_WORD:
            return new ClearCommand(arguments);

        case FindCommand.COMMAND_WORD:
            return prepareFind(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommand(arguments);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        default:
            return new IncorrectCommand(MESSAGE_UNKNOWN_COMMAND);
        }
    }

    /**
     * Parses arguments in the context of the add task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareAdd(String args){
        final Matcher matcher_task = task_DATA_ARGS_FORMAT.matcher(args.trim());
        final Matcher matcher_event = event_DATA_ARGS_FORMAT.matcher(args.trim());
        final Matcher matcher_deadline = deadline_DATA_ARGS_FORMAT.matcher(args.trim());
        // Validate arg string format
        /*
         *  Check if input matches task, event or deadline
         */
        if (matcher_task.matches()) {
            try {
                return new AddCommand(
                        matcher_task.group("name"),
                        matcher_task.group("date"),
                        Integer.parseInt(matcher_task.group("priority"))
                );
            } catch (IllegalValueException ive) {
                return new IncorrectCommand(ive.getMessage());
            }           
        }else if(matcher_event.matches()){
            try {
                return new AddCommand(
                        matcher_event.group("name"),
                        matcher_event.group("date"),
                        matcher_event.group("startTime"),
                        matcher_event.group("endTime")
                );
            } catch (IllegalValueException ive) {
                return new IncorrectCommand(ive.getMessage());
            }
        }else if(matcher_deadline.matches()){
            try {
                return new AddCommand(
                        matcher_deadline.group("name"),
                        matcher_deadline.group("date"),
                        matcher_deadline.group("endTime")
                );
            } catch (IllegalValueException ive) {
                return new IncorrectCommand(ive.getMessage());
            }
        }else{
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE)); 
        }
       
    }

    /**
     * Extracts the new task's tags from the add command's tag arguments string.
     * Merges duplicate tag strings.
     */
    private static Set<String> getTagsFromArgs(String tagArguments) throws IllegalValueException {
        // no tags
        if (tagArguments.isEmpty()) {
            return Collections.emptySet();
        }
        // replace first delimiter prefix, then split
        final Collection<String> tagStrings = Arrays.asList(tagArguments.replaceFirst(" t/", "").split(" t/"));
        return new HashSet<>(tagStrings);
    }

    /**
     * Parses arguments in the context of the delete task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareDelete(String args) {
        final Matcher matcher_dataType = task_DATATYPE_ARGS_FORMAT.matcher(args.trim());
        Optional<String> dataType = parseDataType(args);
        Optional<Integer> index = parseIndex(args);
        if(!matcher_dataType.matches() && !dataType.isPresent() || !index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        return new DeleteCommand(dataType.get(), index.get());
    }

    /**
     * Parses arguments in the context of the select task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareSelect(String args) {
        Optional<Integer> index = parseIndex(args);
        if(!index.isPresent()){
            return new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectCommand.MESSAGE_USAGE));
        }

        return new SelectCommand(index.get());
    }

    /**
     * Returns the specified index in the {@code command} IF a positive unsigned integer is given as the index.
     *   Returns an {@code Optional.empty()} otherwise.
     */
    private Optional<Integer> parseIndex(String command) {
        final Matcher matcher = task_INDEX_ARGS_FORMAT.matcher(command.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String index = matcher.group("targetIndex");
        if(!StringUtil.isUnsignedInteger(index)){
            return Optional.empty();
        }
        return Optional.of(Integer.parseInt(index));

    }
    
    /**
     * Returns the specified dataType in the {@code command}
     *   Returns an {@code Optional.empty()} otherwise.
     */
    private Optional<String> parseDataType(String command) {
        final Matcher matcher = task_DATATYPE_ARGS_FORMAT.matcher(command.trim());
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String dataType = matcher.group("dataType");
        if(!StringUtil.isUnsignedString(dataType)){
            return Optional.empty();
        }
        return Optional.of(dataType);

    }

    /**
     * Parses arguments in the context of the find task command.
     *
     * @param args full command args string
     * @return the prepared command
     */
    private Command prepareFind(String args) {
        final Matcher matcher_keywords = KEYWORDS_ARGS_FORMAT.matcher(args.trim());
        final Matcher matcher_dataType = task_DATATYPE_ARGS_FORMAT.matcher(args.trim());
        Optional<String> dataType_Present = parseDataType(args);
        if (!matcher_keywords.matches() || !matcher_dataType.matches() && !dataType_Present.isPresent()) {
            return new IncorrectCommand(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FindCommand.MESSAGE_USAGE));
        }

        // keywords delimited by whitespace
        final String[] keywords = matcher_keywords.group("keywords").split("\\s+");
        final Set<String> keywordSet = new HashSet<>(Arrays.asList(keywords));
        
        String dataType = matcher_dataType.group("dataType");
        
        return new FindCommand(keywordSet, dataType);
    }

//    private Command prepareEdit(String args) {
//        
//    }
}