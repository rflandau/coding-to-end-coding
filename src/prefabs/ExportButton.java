package prefabs;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import structure.ScriptStruct;
import java.io.IOException;
//import javafx.scene.Node;    //only needed if you want to give the button an image
import javafx.scene.control.Button;

public class ExportButton extends Button {
    //Only one constructor is used, as there's only ever 1 kinds of ExportButton
    //the functionality/style split is for readability only
    public ExportButton(double width, double height, ScriptStruct ss) {
        //functionality
        //sets onExportClickEvent.handle to be called every time the button is clicked or
        //    this.fire() is called
        this.setOnAction(new onExportClickEvent(ss));
        //style
        this.setText("Export");
        this.setMinSize(100, 50);
        this.setMaxSize(1000, 500);
        this.setPrefSize(width, height);
    }
}

class onExportClickEvent implements EventHandler<ActionEvent> {
    ScriptStruct ss; // ScriptStruct needed for click events
    
    public onExportClickEvent(ScriptStruct script){
        ss = script;
    }

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
