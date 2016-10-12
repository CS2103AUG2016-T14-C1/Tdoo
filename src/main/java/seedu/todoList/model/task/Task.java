package seedu.todoList.model.task;

public abstract class Task implements ReadOnlyTask {

	public String name;
	
	public Task(){
		
	}
	
	public Task(ReadOnlyTask source){
		name = source.getName().fullName;
	}
}
