package seedu.todoList.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.todoList.model.task.*;

public class DeadlineCard extends UiPart{

    private static final String FXML = "DeadlineCard.fxml";

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label date;
    @FXML
    private Label endTime;
    @FXML
    private Label done;

    private Deadline task;
    private int displayedIndex;

    public DeadlineCard(){

    }

    public static DeadlineCard load(ReadOnlyTask task, int displayedIndex){
        DeadlineCard card = new DeadlineCard();
        card.task = (Deadline) task;
        card.displayedIndex = displayedIndex;
        return UiPartLoader.loadUiPart(card);
    }

    @FXML
    public void initialize() {
        name.setText(task.getName().name);
        id.setText(displayedIndex + ". ");
        date.setText(task.getDate().date);
        endTime.setText("End Time: " + task.getEndTime().endTime);
        done.setText("Completed: " + task.getDone().isDone);
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
