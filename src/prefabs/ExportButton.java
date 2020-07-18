package prefabs;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import structure.ScriptStruct;
import java.io.IOException;
import javafx.scene.control.Button;

/*
    ExportButton
    Defines the physical look of the button, and the action events when clicked.
*/
public class ExportButton extends Button {
    /*
        default constructor
        Sets the below event handler to be called when clicked.
        Also defines sizes.
    */
    public ExportButton(double width, double height, ScriptStruct ss) {
        this.setOnAction(new onExportClickEvent(ss));

        //style
        this.setText("Export");
        this.setMinSize(100, 50);
        this.setMaxSize(1000, 500);
        this.setPrefSize(width, height);
    }
}

/*
    onExportClick
    Class that defines the events that take place when the button is clicked.
    Calls ScriptStruct.export().
*/
class onExportClickEvent implements EventHandler<ActionEvent> {
    ScriptStruct ss; // ScriptStruct needed for click events

    /*
        constructor()
        Binds the script struct in use.
    */
    public onExportClickEvent(ScriptStruct script){
        ss = script;
        return;
    }

    /*
        handle()
        When clicked, the button starts scriptstruct's export process.
    */
    @Override
    public void handle(ActionEvent actionEvent) {
        System.out.println("Starting Export...");
        //temp implementation of the export button
        try{
            ss.export();
        }catch(IOException ex){
            System.out.println("IOexception");
        }
        actionEvent.consume();
    }
}
