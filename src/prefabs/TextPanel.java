package prefabs;

//visuals
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
//Control input
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
//Event handling

//Our packages/classes

public class TextPanel extends StackPane{
    //variables----------------------------
    //Scene components
    HBox hboxContainer;
    TextField textBox;
    Label commandName;
    Button saveButton;
    
    //constructor--------------------------
    public TextPanel(){
	//Variable Init--------------------
	commandName = new Label("Box");
	textBox = new TextField();
	hboxContainer = new HBox();
	saveButton = new Button("Button");

	//Set Children---------------------
	hboxContainer.getChildren().addAll(commandName, textBox, saveButton);
	this.getChildren().add(hboxContainer);
    }
}
