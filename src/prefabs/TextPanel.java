package prefabs;

//visuals
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.geometry.Pos;
//Control input
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
//Event handling
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
//Our packages/classes
import prefabs.CommandBlock;

public class TextPanel extends StackPane{
    //variables----------------------------
    //Scene components
    HBox hboxContainer;
    TextField textBox;
    Label commandName;
    Button saveButton;

    //Object references
    CommandBlock currentBlock;
    
    //constructor--------------------------
    public TextPanel(){
	//Variable Init--------------------
	commandName = new Label("Box");
	textBox = new TextField();
	hboxContainer = new HBox();
	saveButton = new Button("Button");

	//Set Children---------------------
	hboxContainer.setAlignment(Pos.CENTER);
	hboxContainer.getChildren().addAll(commandName, textBox, saveButton);
	this.getChildren().add(hboxContainer);
	eventHandlingInit();
    }
    public void setEdit(CommandBlock block){
	currentBlock = block;
    }

    //Event Handling-----------------------
    private void eventHandlingInit(){
	saveButton.setOnAction(new EventHandler<ActionEvent>(){
	    @Override
	    public void handle(ActionEvent e){
		System.out.println("Button");
	    }
        });
    }
}
