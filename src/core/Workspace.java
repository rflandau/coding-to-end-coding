package core;

// javafx
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.SplitPane;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;

import java.util.ArrayList;             //for backend ArrayList
import java.util.Hashtable;
import java.lang.System;
import java.io.IOException;

//Import Event Handling
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;


//import our other packages
import prefabs.ExportButton;
import prefabs.CommandBlock;
import structure.Command;
import structure.ScriptStruct;

public class Workspace extends Application {
    double              defaultWindowWidth = 800,   // default width in pixels
                        defaultWindowHeight = 600;  // default height in pixels
    ScriptStruct        structure;                  // holds Interpreters
    ArrayList<Command>  sidebarCommands;            // available commands
    AnchorPane          root;                       // holds all GUI elements

    /*
    init is called right before start, before the application comes into being
    Stuff for its parts shouldn't be made here, but anything that needs to be
    prepared for the starting of the app that isn't JavaFX can go here
    */
    @Override
    public void init() {
        structure = new ScriptStruct();
        sidebarCommands = structure.getTemplateCommands();
    }

    @Override
    public void start(Stage stage) throws Exception {
        //load the FXML
    try{
        root = (AnchorPane) FXMLLoader.load(getClass().getResource("main.fxml"));
    }catch(IOException ie){
        System.out.println("Exception on FXML load: "+ie);
    }
    
    //unpack all items
    SplitPane   scenePane = (SplitPane) root.getChildren().get(0);
    AnchorPane  sidebar = (AnchorPane) scenePane.getItems().get(0);
    AnchorPane  mainCanvas = (AnchorPane) scenePane.getItems().get(1);
    ScrollBar   sidebarScroll = (ScrollBar) sidebar.getChildren().get(0);
    VBox        sidebarVbox = (VBox) sidebar.getChildren().get(1);
    SplitPane   canvasSplit = (SplitPane) mainCanvas.getChildren().get(0);
    AnchorPane  canvasPane = (AnchorPane) canvasSplit.getItems().get(0);
    AnchorPane  bottomPanel = (AnchorPane) canvasSplit.getItems().get(1);
    Button      exportButton = (Button) bottomPanel.getChildren().get(0);
    VBox        canvasBox = (VBox) canvasPane.getChildren().get(0);
    ScrollBar   canvasScroll = (ScrollBar) canvasPane.getChildren().get(1);
    
    //------------------------------------------------------------
    //Beginning of Event Handlers
    exportButton.setOnAction(new EventHandler<ActionEvent>(){
        @Override
        public void handle(ActionEvent e){
            try{
                structure.export();
            }catch(IOException ex){
                System.out.println("Export button IOexception:"+ex);
            }
        }
    });
    
    EventHandler<MouseEvent> clickSideBarEvent = new EventHandler<MouseEvent>(){
        @Override public void handle(MouseEvent e){addCommandBlock(canvasBox);}
    };
    
    //End of Event Handlers
    //----------------------------------------------------------
    //Linking Event Handlers to items
    sidebar.addEventFilter(MouseEvent.MOUSE_CLICKED, clickSideBarEvent);
        // populating available commands
        for(int i = 0; i < sidebarCommands.size(); i ++){
            Command c = sidebarCommands.get(i);
            CommandBlock b = new CommandBlock(1,2,Color.WHITE,c,structure);
            sidebarVbox.getChildren().add(b);
        }

        stage.setScene(new Scene(root));
        stage.show();
    }

    public void addCommandBlock(VBox blockBox){
        int index = structure.getFlowSize();
        Command c = new Command("echo Hello World");
        CommandBlock block = new CommandBlock(1,2,Color.WHITE,c,structure);
        int len = blockBox.getChildren().size();
        Rectangle endBlock = (Rectangle) blockBox.getChildren().get(len-1);
        blockBox.getChildren().remove(len-1);
        blockBox.getChildren().add(block);
        blockBox.getChildren().add(endBlock);
        structure.addCommandToFlow(structure.getFlowSize(), "echo");

    }
}
