package prefabs;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import structure.ScriptStruct;
import java.io.IOException;
//import javafx.scene.Node;    //only needed to give the button an image
import javafx.scene.control.Button;

/*
    ExportButton
    Defines the physical look of the button, and the events when clicked
*/
public class ExportButton extends Button {
    /*
        default constructor
    */
    public ExportButton(double width, double height, ScriptStruct ss) {
        // sets onExportClickEvent.handle to be called every time the button
        // is clicked or this.fire() is called
        this.setOnAction(new onExportClickEvent(ss));
        
        //style
        this.setText("Export");
        this.setMinSize(100, 50);
        this.setMaxSize(1000, 500);
        this.setPrefSize(width, height);
    }
}

/*
    onExportClick()
    events that take place whenthe export button is clicked
    calls ScriptStruct.export()
*/
class onExportClickEvent implements EventHandler<ActionEvent> {
    ScriptStruct ss; // ScriptStruct needed for click events
    
    /*
        TODO: expand these comments
    */
    public onExportClickEvent(ScriptStruct script){
        ss = script;
        return;
    }

    /*
        handle()
        what happens when the user clicks on the export button
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
