package core;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;    //for sidebar shape
import javafx.scene.paint.Color;        //for sidebar color
import javafx.scene.layout.VBox;        //for vertical ordering of sidebar objects
import javafx.scene.layout.BorderPane;        //for vertical ordering of sidebar objects
import javafx.scene.layout.StackPane;        //for vertical ordering of sidebar objects
import javafx.geometry.Insets;            //for sidebar spacing

//Import Event Handling
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

//import javafx.scene.image.Image;
//I know import .* is gross, but I had to import, like, 6 things to get the background
//import javafx.scene.layout.*;
import javafx.stage.Stage;

import prefabs.ExportButton;
import prefabs.CommandBlock;
import structure.Command;
import structure.ScriptStruct;
import structure.Interpreter;

import java.util.ArrayList;

public class Workspace extends Application {
    //hard-coded window sizes, can be changed later
    double defaultWindowWidth = 800;
    double defaultWindowHeight = 600;
    ScriptStruct commandList;
    ArrayList<Interpreter> interpreterList;
    Interpreter interp;

    //Applications do not need constructors
    //However, the program arguments from launch can be accessed with getParameters()

    /*
    init is called right before start, before the application comes into being
    Stuff for its parts shouldn't be made here, but anything that needs to be
    prepared for the starting of the app that isn't JavaFX can go here
    The superclass definition also does nothing, so I commented this out
    */
    @Override
    public void init() {
        commandList = new ScriptStruct();
        interpreterList = Interpreter.generateInterpreters();
        interp = interpreterList.get(0);
    }
    //"/resources/images/WorkspaceBackgroundTile.png"
    @Override
    public void start(Stage stage) throws Exception {

        //scene (window) is created here
        stage.setTitle("JCoding to End Coding");
        //I changed root to a BorderPane to allow for better layout of the application
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, defaultWindowWidth, defaultWindowHeight);

        //making all the containers for visual elements
        //VBoxes vertically order things. sidebar is for the sidebar, but NOT its contents
        VBox rightSidebar = new VBox(10);    //this number spaces the elements
        StackPane mainCanvas = new StackPane();  // making another Container for main canvas
        VBox blocks = new VBox();
        blocks.setPadding(new Insets(10));
        rightSidebar.setPadding(new Insets(10));    //this number puts a buffer around the box
        mainCanvas.setPadding(new Insets(10));
        ExportButton button = new ExportButton(150, 100);
        Rectangle sidebarRect = new Rectangle(
                                150,
                                defaultWindowHeight - button.getHeight(),
                                Color.LIGHTGREY
                                );
        rightSidebar.getChildren().addAll(button, sidebarRect);

        //adding objects to the scene
        root.setRight(rightSidebar);

        // Creating the background of the canvas for the command blocks to be placed
        Rectangle canvasRect = new Rectangle(
                               500,
                               defaultWindowHeight,
                               Color.LIGHTGREY
                               );

        //Add the rectangle to the canvas node
        mainCanvas.getChildren().add(canvasRect);
        //Add the blocks to the canvas over the rect
        //root.setLeft(blocks);
        //Add canvas to the scene
        root.setCenter(mainCanvas);
        mainCanvas.getChildren().add(blocks);
        //Creating Event handler for click to add block

        System.out.println("Creating START Block");
        commandList.addCommandToFlow(commandList.getFlowSize(), "start", interp);
        Command s = commandList.getCommand(commandList.getFlowSize()-1);
        CommandBlock start = new CommandBlock(1,2,Color.BLACK,s);
        blocks.getChildren().add(start);
        
        System.out.println("Creating END Block");
        commandList.addCommandToFlow(commandList.getFlowSize(), "end", interp);
        Command e = commandList.getCommand(commandList.getFlowSize()-1);
        CommandBlock end = new CommandBlock(1,2,Color.BLACK,e);
        blocks.getChildren().add(end);
        
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e){
                System.out.println("Mouse click handled");
                
                // removing end block
                                // removing end block
                commandList.removeCommandFromFlow(commandList.getFlowSize()-1);
                blocks.getChildren().remove(end);
                
                // appending new command block
                commandList.addCommandToFlow(commandList.getFlowSize(), "helloworld", interp);
                Command c = commandList.getCommand(commandList.getFlowSize()-1);
                CommandBlock block = new CommandBlock(1,2,Color.WHITE,c);
                blocks.getChildren().add(block);
                
                // appending end block
                commandList.addCommandToFlow(commandList.getFlowSize(), "end", interp);
                blocks.getChildren().add(end);
                
                // print statements for debugging (NOTE remove this)
                int size = commandList.getFlowSize();
                for(int i = 0; i < size; i ++) {
                    Command cmd = commandList.getCommand(i);
                    System.out.println(cmd.getName());
                }
            }
        };
        //Linking the eventhandler to the canvas rectangle
	blocks.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        //add scene to stage
        stage.setScene(scene);
        //show scene
        stage.show();
    }
    
    /*
    stop is like init, but it goes right after the application ends
    The superclass definition also does nothing, so I commented this out
    @Override
    public void stop() {

    }
    */

}


//code dump because thomas is a hoarder
//this stuff's for background, but I can't get the image to appear right now so I'll do it later
        /*Background bg = new Background(new BackgroundImage(
                //I want to source from a resources folder, but idk how filepathing in java works
                new Image("file:WorkspaceBackgroundTile.png"),
                BackgroundRepeat.REPEAT, //how often the image repeats on x axis (repeat = as much as needed)
                BackgroundRepeat.REPEAT, //how often the image repeats on y axis (repeat = as much as needed)
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        ));
        //root region should be the parent of static visual elements. It doesn't need to be tho
        Region rootRegion = new Region();
        rootRegion.setBackground(bg);*/
