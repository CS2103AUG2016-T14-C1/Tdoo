package seedu.todoList.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.todoList.model.task.*;

public class TodoCard extends UiPart{

    private static final String FXML = "TodoCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label startDate;
    @FXML
    private Label endDate;
    @FXML
    private Label priority;

    private Todo task;	
    private int displayedIndex;

    public TodoCard(){

    }

    public static TodoCard load(ReadOnlyTask task, int displayedIndex){
        TodoCard card = new TodoCard();
        card.task = (Todo) task;
        card.displayedIndex = displayedIndex;
        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
        name.setText(task.getName().name);
        id.setText(displayedIndex + ". ");
        startDate.setText("Start Date: " + task.getStartDate().date);
        endDate.setText("End Date: " + task.getEndDate().endDate);
        priority.setText("Priority: " + task.getPriority().toString());
    }

    public HBox getLayout() {
        return cardPane;
    }

    @Override
    public void setNode(Node node) {
        cardPane = (HBox)node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}
