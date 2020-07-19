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
//Other
import java.lang.CharSequence;
//Our packages/classes
import prefabs.CommandBlock;

/*
    TextPanel
    NOTE: finish these comkments
*/
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
        commandName = new Label("Edit Block Contents Here:");
        textBox = new TextField();
        hboxContainer = new HBox();
        saveButton = new Button("Save to Block");

        //Set Children---------------------
        hboxContainer.setAlignment(Pos.CENTER);
        hboxContainer.setSpacing(12);
        hboxContainer.getChildren().addAll(commandName, textBox, saveButton);
        this.getChildren().add(hboxContainer);
        eventHandlingInit();
    }

    //Sets the command block for editing
    public void setEdit(CommandBlock block){
        if (currentBlock != null){currentBlock.closeEdit();}
        currentBlock = block;
        String newLabel = block.getCommandName();
        String arg = block.getArgument();
        textBox.setText(arg);
        commandName.setText(newLabel+":");
    }
    
    //Current Block deleted
    public void blockDeleted(){
	currentBlock = null;
	commandName.setText("Block Removed");
    }
    
    //Event Handling-----------------------
    private void eventHandlingInit(){
    saveButton.setOnAction(new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent e){
            CharSequence txtInput = textBox.getCharacters();
            String txtStr = txtInput.toString();
            //If no block has been set to edit
            if (currentBlock == null){return;}
            //If block has been set to edit
            else{currentBlock.newArgument(txtStr);}

            }
        });
    }
}
