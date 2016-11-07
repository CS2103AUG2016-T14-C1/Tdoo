<!--@@author A0132157M-->
# Developer Guide

* [Setting Up](#setting-up)
* [Design](#design)
* [Implementation](#implementation)
* [Testing](#testing)
* [Dev Ops](#dev-ops)
* [Appendix A: User Stories](#appendix-a--user-stories)
* [Appendix B: Use Cases](#appendix-b--use-cases)
* [Appendix C: Non Functional Requirements](#appendix-c--non-functional-requirements)
* [Appendix D: Glossary](#appendix-d--glossary)
* [Appendix E : Product Survey](#appendix-e-product-survey)


## Setting up

#### Prerequisites

1. **JDK `1.8.0_60`**  or later<br>

    > Having any Java 8 version is not enough. <br>
    This app will not work with earlier versions of Java 8.

2. **Eclipse** IDE
3. **e(fx)clipse** plugin for Eclipse (Do the steps 2 onwards given in
   [this page](http://www.eclipse.org/efxclipse/install.html#for-the-ambitious))
4. **Buildship Gradle Integration** plugin from the Eclipse Marketplace


#### Importing the project into Eclipse

0. Fork this repo, and clone the fork to your computer
1. Open Eclipse (Note: Ensure you have installed the **e(fx)clipse** and **buildship** plugins as given
   in the prerequisites above)
2. Click `File` > `Import`
3. Click `Gradle` > `Gradle Project` > `Next` > `Next`
4. Click `Browse`, then locate the project's directory
5. Click `Finish`

  > * If you are asked whether to 'keep' or 'overwrite' config files, choose to 'keep'.
  > * Depending on your connection speed and server load, it can even take up to 30 minutes for the set up to finish
      (This is because Gradle downloads library files from servers during the project set up process)
  > * If Eclipse auto-changed any settings files during the import process, you can discard those changes.

## Design

### Architecture

<img src="images/Architecture.png" width="600"><br>
The **_Architecture Diagram_** given above explains the high-level design of the App.
Given below is a quick overview of each component.

`Main` has only one class called [`MainApp`](../src/main/java/seedu/Tdoo/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connect them up with each other.
* At shut down: Shuts down the components and invoke cleanup method where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.
Two of those classes play important roles at the architecture level.
* `EventsCentre` : This class (written using [Google's Event Bus library](https://github.com/google/guava/wiki/EventBusExplained))
  is used by components to communicate with other components using events (i.e. a form of _Event Driven_ design)
* `LogsCenter` : Used by many classes to write log messages to the App's log file.

The rest of the App consists four components.
* [**`UI`**](#ui-component) : The UI of tha App.
* [**`Logic`**](#logic-component) : The command executor.
* [**`Model`**](#model-component) : Holds the data of the App in-memory.
* [**`Storage`**](#storage-component) : Reads data from, and writes data to, the hard disk.

Each of the four components
* Defines its _API_ in an `interface` with the same name as the Component.
* Exposes its functionality using a `{Component Name}Manager` class.

For example, the `Logic` component (see the class diagram given below) defines it's API in the `Logic.java`
interface and exposes its functionality using the `LogicManager.java` class.<br>
<img src="images/LogicClassDiagram.jpg" width="800"><br>

The _Sequence Diagram_ below shows how the components interact for the scenario where the user issues the
command `done todo`.

<img src="images\SequenceClassDiagram.jpg" width="800">

>Note how the `Model` simply raises a `TdooChangedEvent` when tasks data is changed.

<!-- The diagram below shows how the `EventsCenter` reacts to that event, which eventually results in the updates
being saved to the hard disk and the status bar of the UI being updated to reflect the 'Last Updated' time. <br>
<img src="images\SDforDeletePersonEventHandling.png" width="800">

> Note how the event is propagated through the `EventsCenter` to the `Storage` and `UI` without `Model` having
  to be coupled to either of them. This is an example of how this Event Driven approach helps us reduce direct
  coupling between components. -->

The sections below give more details of each component.

### UI component

<img src="images/UiClassDiagram.jpg" width="800"><br>

**API** : [`Ui.java`](../src/main/java/seedu/Tdoo/ui/Ui.java)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`,
`StatusBarFooter`, `BrowserPanel` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class
and they can be loaded using the `UiPartLoader`.

The `UI` component uses JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files
 that are in the `src/main/resources/view` folder.<br>
 For example, the layout of the [`MainWindow`](../src/main/java/seedu/address/ui/MainWindow.java) is specified in
 [`MainWindow.fxml`](../src/main/resources/view/MainWindow.fxml)

The `UI` component,
* Executes user commands using the `Logic` component.
* Binds itself to some data in the `Model` so that the UI can auto-update when data in the `Model` change.
* Responds to events raised from various parts of the App and updates the UI accordingly.

### Logic component

<img src="images/LogicClassDiagram.jpg" width="800"><br>

**API** : [`Logic.java`](../src/main/java/seedu/Tdoo/logic/Logic.java)

1. `Logic` uses the `Parser` class to parse the user command.
2. This results in a `Command` object which is executed by the `LogicManager`.
3. The command execution can affect the `Model` (e.g. adding a person) and/or raise events.
4. The result of the command execution is encapsulated as a `CommandResult` object which is passed back to the `Ui`.

<!-- Given below is the Sequence Diagram for interactions within the `Logic` component for the `execute("delete 1")`
 API call.<br>
<img src="images/DeletePersonSdForLogic.png" width="800"><br> -->

### Model component

<img src="images/ModelClassDiagram.jpg" width="800"><br>

**API** : [`Model.java`](../src/main/java/seedu/Tdoo/model/Model.java)

The `Model`,
* stores a `UserPref` object that represents the user's preferences.
* stores the Tasks data.
* exposes a `UnmodifiableObservableList<ReadOnlyTask>` that can be 'observed' e.g. the UI can be bound to this list
  so that the UI automatically updates when the data in the list change.
* does not depend on any of the other three components.

### Storage component

<img src="images/StorageClassDiagram.jpg" width="800"><br>

**API** : [`Storage.java`](../src/main/java/Tdoo/address/storage/Storage.java)

The `Storage` component,
* can save `UserPref` objects in json format and read it back.
* can save the Tasks data in xml format and read it back.

### Common classes

Classes used by multiple components are in the `seedu.Tdoo.commons` package.

## Implementation

### Logging

We are using `java.util.logging` package for logging. The `LogsCenter` class is used to manage the logging levels
and logging destinations.

* The logging level can be controlled using the `logLevel` setting in the configuration file
  (See [Configuration](#configuration))
* The `Logger` for a class can be obtained using `LogsCenter.getLogger(Class)` which will log messages according to
  the specified logging level
* Currently log messages are output through: `Console` and to a `.log` file.

**Logging Levels**

* `SEVERE` : Critical problem detected which may possibly cause the termination of the application
* `WARNING` : Can continue, but with caution
* `INFO` : Information showing the noteworthy actions by the App
* `FINE` : Details that is not usually noteworthy but may be useful in debugging
  e.g. print the actual list instead of just its size

### Configuration

Certain properties of the application can be controlled (e.g App name, logging level) through the configuration file
(default: `config.json`):


## Testing

Tests can be found in the `./src/test/java` folder.

**In Eclipse**:
> If you are not using a recent Eclipse version (i.e. _Neon_ or later), enable assertions in JUnit tests
  as described [here](http://stackoverflow.com/questions/2522897/eclipse-junit-ea-vm-option).

* To run all tests, right-click on the `src/test/java` folder and choose
  `Run as` > `JUnit Test`
* To run a subset of tests, you can right-click on a test package, test class, or a test and choose
  to run as a JUnit test.

**Using Gradle**:
* See [UsingGradle.md](UsingGradle.md) for how to run tests using Gradle.

We have two types of tests:

1. **GUI Tests** - These are _System Tests_ that test the entire App by simulating user actions on the GUI.
   These are in the `guitests` package.

2. **Non-GUI Tests** - These are tests not involving the GUI. They include,
   1. _Unit tests_ targeting the lowest level methods/classes. <br>
      e.g. `seedu.Tdoo.commons.UrlUtilTest`
   2. _Integration tests_ that are checking the integration of multiple code units
     (those code units are assumed to be working).<br>
      e.g. `seedu.Tdoo.storage.StorageManagerTest`
   3. Hybrids of unit and integration tests. These test are checking multiple code units as well as
      how the are connected together.<br>
      e.g. `seedu.Tdoo.logic.LogicManagerTest`

**Headless GUI Testing** :
Thanks to the [TestFX](https://github.com/TestFX/TestFX) library we use,
 our GUI tests can be run in the _headless_ mode.
 In the headless mode, GUI tests do not show up on the screen.
 That means the developer can do other things on the Computer while the tests are running.<br>
 See [UsingGradle.md](UsingGradle.md#running-tests) to learn how to run tests in headless mode.

## Dev Ops

### Build Automation

See [UsingGradle.md](UsingGradle.md) to learn how to use Gradle for build automation.

### Continuous Integration

We use [Travis CI](https://travis-ci.org/) to perform _Continuous Integration_ on our projects.
See [UsingTravis.md](UsingTravis.md) for more details.

### Making a Release

Here are the steps to create a new release.

 1. Generate a JAR file [using Gradle](UsingGradle.md#creating-the-jar-file).
 2. Tag the repo with the version number. e.g. `v0.1`
 2. [Crete a new release using GitHub](https://help.github.com/articles/creating-releases/)
    and upload the JAR file your created.

### Managing Dependencies

A project often depends on third-party libraries. For example, Address Book depends on the
[Jackson library](http://wiki.fasterxml.com/JacksonHome) for XML parsing. Managing these _dependencies_
can be automated using Gradle. For example, Gradle can download the dependencies automatically, which
is better than these alternatives.<br>
a. Include those libraries in the repo (this bloats the repo size)<br>
b. Require developers to download those libraries manually (this creates extra work for developers)<br>

## Appendix A : User Stories

Priorities: High (must have) - `* * *`, Medium (nice to have)  - `* *`,  Low (unlikely to have) - `*`


Priority | As a ... | I want to ... | So that I can...
-------- | :-------- | :--------- | :-----------
 `* * *` | user	| add a Todo task into the task-list | Track my schedule
  `* * *` | user	| add a Event task into the task-list | Track my schedule
   `* * *` | user	| add a Deadline task into the task-list | Track my schedule
 `* * *` | user	| edit a task info when change is needed | Change my tasks
 `* * *` | user	| mark a task ‘done’ when it is completed | Ignore it
 `* * *` | user	| delete a task from a Tasks list when it cannot be completed | Only have the latest tasks
 `* * *` | user	| undo the latest command | revert my mistake
 `* * *` | user	| find a task with given name | locate that task in the list easily
 `* * *` | user	| get help window | know what to give as input
 `* * *` | user	| clear tasks list | delete all the tasks in the list easily
 `* * *` | user	| list all the tasks in the tasks-list | see what are the tasks in the list
  `* *`  | user	| set priority / difficulty of a task | strategy which task to do first
   `*`   | user	| make connections between tasks | relationship between tasks can be seen
   `*`   | user	| know what task has to be done right now | have a focus of what to do now


## Appendix B : Use Cases

(For all use cases below, the **System** is the `Tdoo` and the **Actor** is the `user`, unless specified otherwise)

#### Use case: Adds a Event task

**MSS**

1. User requests to ask a Todo task.
2. User enters the task description name (compulsory), start/end date, start/end time.
3. Tdoo saves the Event task details.
4. Tdoo displays tasks on Event panel. <br>


**Extensions**

2a. The task is completed

> Task in the respective panel will show green

2b. The task is incomplete

> Task in the respective panel will show incomplete

2c. The task is past the due date

> Task in the respective panel will show red

2d. The given start/end date is invalid

> Input validation for dates performed that checks whether the entered date is an upcoming date, start date is before the end date and there are no more than 2 dates in the user input. Tdoo provides feedback to the user if the entered date is found to be an invalid date.

2e. The given start/end time is invalid

> Input validation for dates performed that checks whether the entered time is an upcoming time, start time is before the end time. Tdoo provides feedback to the user if the entered date is found to be an invalid date.<br>


#### Use case: Delete a Event task

**MSS**

1. User requests to delete a Event task(s).
2. User enters the indexes of the task that are shown in the Event list panel
3. Todo performs delete operations on the selected index.
4. Tdoo removes the Event task from the Event list panel. <br>


**Extensions**

2a. User entered incorrect index.

> Tdoo feedback that the index is invalid.<br>

#### Use case: Edit a Event task

**MSS**

1. User requests to Edit a Event task(s).
2. User enters the index of the Event task to be edited and the parameters(name, date, time).
3. Todo performs edit operations on the selected index.
4. Tdoo display edited task. <br>


**Extensions**

2a. User entered incorrect index.

> Tdoo feedback that the index is invalid.

2b. User entered invalid date or time.

> Input validation for dates performed that checks whether the entered date is an upcoming date, start date is before the end date and there are no more than 2 dates in the user input. Tdoo provides feedback to the user if the entered date is found to be an invalid date.

2c. User did not key in name.

> Tdoo feedback that event name is compulsory.

2a. User entered duplicate name.

> Tdoo feedback that event name is already in the Event list.<br>

#### Use case: Find Event tasks

**MSS**

1. User requests to Find a Event task(s).
2. User enters keyword or start date.
3. Todo performs find operations on the keywords.
4. Tdoo displays all event tasks. <br>


**Extensions**

2a. User entered Find all.

> Tdoo displays all Event tasks as well as Todo tasks and deadline tasks.<br>

#### Use case: Undo previous operation

**MSS**

1. User requests to undo previous operation.
2. Tdoo reverts back to previous state.<br>

**Extensions**

2a. User entered incorrect command.

> Tdoo feedback that the command is invalid.

2b. No previous state

> Tdoo does nothing.<br>

#### Use case: Mark Event task as done

**MSS**

1. User requests to mark Event task as done.
2. User enters the Event index.
3. Tdoo displays the selected Event index in green.

**Extensions**

2a. User entered incorrect command.

> Tdoo feedback that the command is invalid.<br>


## Appendix C : Non Functional Requirements

1. Should work on any [mainstream OS](#mainstream-os) as long as it has Java `1.8.0_60` or higher installed.
2. Should be able to hold up to 1000 tasks.
3. Should come with automated unit tests and open source code.
4. Should favor DOS style commands over Unix-style commands.
5. Should be easy for new users to.
6. Keyboard is the primary input source
7. Not color blind to see different color status.
8. Should be able to work without Internet connection.


## Appendix D : Glossary

##### Mainstream OS

> Windows, Linux, Unix, OS-X

<!-- ##### Private contact detail

> A contact detail that is not meant to be shared with others -->

## Appendix E : Product Survey

The team surveyed 4 Todo apps in the market.

Wunderlist

a. Tasks can be marked as done which would be automatically moved to completed to-do list. There’s no priority setting however one could mark a to-do as starred.<br><br>
b. Wunderlist is a to do list and does not incorporate function to block off slots/timing.

Task Coach

a. It’s task creation process is quite complicated with different tabs and sections, and requires several clicks.<br><br>
b. Task Coach does not have a function that automates process between tasks or time blocks.

Tick Tick

a. Entering an event in the calendar takes several clicks, that is because it is very customisable.

Evernote

a. Evernote cannot block or reserve a slot/schedule.
