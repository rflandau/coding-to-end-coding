package core;

// javafx 
import javafx.scene.layout.AnchorPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.SplitPane;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

// import javafx.scene.Group;
// import javafx.scene.shape.Rectangle;    //for sidebar shape
// import javafx.scene.layout.BorderPane;  //for vertical ordering of sidebar objects
// import javafx.scene.layout.StackPane;   //for vertical ordering of sidebar objects
// import javafx.geometry.Insets;          //for sidebar spacing

import java.util.ArrayList;             //for backend ArrayList

//Import Event Handling
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

//import javafx.scene.image.Image;
//import javafx.scene.layout.*;


//import our other packages
import prefabs.ExportButton;
import prefabs.CommandBlock;
import structure.Command;
import structure.ScriptStruct;
import structure.Interpreter;



public class Workspace extends Application {
    //hard-coded window sizes, can be changed later
    double defaultWindowWidth = 800;
    double defaultWindowHeight = 600;
    ScriptStruct commandList;
    ArrayList<Interpreter> interpreterList;
    ArrayList<Command> sidebarCommands;
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
        sidebarCommands = interp.getCommands();
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        // local variables
        AnchorPane root = new AnchorPane(),             // contains all GUI elements
                   sidebarPane = new AnchorPane(),      // contains sidebar with available commands
                   rightPane = new AnchorPane(),        // contains flowchart and export panes
                   flowchartPane = new AnchorPane(),    // contains flowchart
                   exportPane = new AnchorPane();       // contains export button
        Scene      scene = new Scene(root, defaultWindowWidth, defaultWindowHeight);
        SplitPane  vertical = new SplitPane(),          // vertical window divider
                   horizontal = new SplitPane();        // horizontal window divider
        ScrollBar  sidebarScroll = new ScrollBar(),     // scrollbar for left sidebar
                   flowchartScroll = new ScrollBar();   // scrollbar for flowchart
        VBox       sidebar = new VBox(),                // contains available commands
                   flowchart = new VBox();              // contains flowchart
        
        // left and right panes
        vertical.getItems().addAll(sidebarPane, rightPane);
        vertical.setOrientation(Orientation.HORIZONTAL);
        vertical.setDividerPositions(0.5f, 0.5f);
        AnchorPane.setTopAnchor(vertical, 15.0);
        AnchorPane.setRightAnchor(vertical, 15.0);
        AnchorPane.setBottomAnchor(vertical, 15.0);
        AnchorPane.setLeftAnchor(vertical, 15.0);
        root.getChildren().addAll(vertical);
        
        // top and bottom panes on right panel
        horizontal.getItems().addAll(flowchartPane, exportPane);
        horizontal.setOrientation(Orientation.VERTICAL);
        horizontal.setDividerPositions(0.9f, 0.1f);
        AnchorPane.setTopAnchor(horizontal, 0.0);
        AnchorPane.setRightAnchor(horizontal, 0.0);
        AnchorPane.setBottomAnchor(horizontal, 0.0);
        AnchorPane.setLeftAnchor(horizontal, 0.0);
        rightPane.getChildren().addAll(horizontal);
        
        // setting up sidebar
        sidebar.setSpacing(10);
        sidebarPane.setTopAnchor(sidebar, 10.0);
        sidebarPane.setLeftAnchor(sidebar, 145.0);
        sidebarScroll.setOrientation(Orientation.VERTICAL);
        sidebarPane.getChildren().add(sidebar);
        sidebar.setAlignment(Pos.CENTER);
        
        // populating available commands
        for(int i = 0; i < sidebarCommands.size(); i ++){
            Command c = sidebarCommands.get(i);
            CommandBlock b = new CommandBlock(1,2,Color.WHITE,c);
            sidebar.getChildren().add(b);
        }
        
        // setting up flowchart
        flowchartPane.setTopAnchor(flowchart, 10.0);
        flowchartPane.setRightAnchor(flowchart, 145.0);
        flowchartScroll.setOrientation(Orientation.VERTICAL);
        flowchartPane.getChildren().add(flowchart);
        flowchart.setAlignment(Pos.CENTER);
        
        // creating start block
        Command s = new Command("start", "");
        CommandBlock start = new CommandBlock(1,2,Color.GREY,s);
        flowchart.getChildren().add(start);
        
        // creating end block
        Command e = new Command("end", "");
        CommandBlock end = new CommandBlock(1,2,Color.GREY,e);
        flowchart.getChildren().add(end);
        
        // setting up button pane
        ExportButton button = new ExportButton(100, 50, commandList, interp);
        exportPane.setRightAnchor(button, 145.0);
        exportPane.getChildren().add(button);
        
        // event handler
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e){
                // removing end block from GUI
                flowchart.getChildren().remove(end);
                
                // appending new command block to GUI and to commandList
                commandList.addCommandToFlow(commandList.getFlowSize(), "echo", interp);
                Command c = commandList.getCommand(commandList.getFlowSize()-1);
                CommandBlock block = new CommandBlock(1,2,Color.WHITE,c);
                flowchart.getChildren().add(block);
                
                // appending end block to GUI
                flowchart.getChildren().add(end);
            }
        };
        sidebar.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        
        stage.setTitle("Coding to End Coding");
        stage.setScene(scene);
        stage.show();
    }
}

//         //scene (window) is created here
//         stage.setTitle("JCoding to End Coding");
//         //I changed root to a BorderPane to allow for better layout of the application
//         BorderPane root = new BorderPane();
//         Scene scene = new Scene(root, defaultWindowWidth, defaultWindowHeight);
// 
//         //making all the containers for visual elements
//         //VBoxes vertically order things. sidebar is for the sidebar, but NOT its contents
//         VBox rightSidebar = new VBox(10);    //this number spaces the elements
//         StackPane mainCanvas = new StackPane();  // making another Container for main canvas
//         VBox flowChart = new VBox();
//         flowChart.setPadding(new Insets(10));
//         rightSidebar.setPadding(new Insets(10));    //this number puts a buffer around the box
//         mainCanvas.setPadding(new Insets(10));
//         ExportButton button = new ExportButton(150, 100);
//         Rectangle sidebarRect = new Rectangle(
//                                 150,
//                                 defaultWindowHeight - button.getHeight(),
//                                 Color.LIGHTGREY
//                                 );
//         rightSidebar.getChildren().addAll(button, sidebarRect);
// 
//         //adding objects to the scene
//         root.setRight(rightSidebar);
// 
//         // Creating the background of the canvas for the command blocks to be placed
//         Rectangle canvasRect = new Rectangle(
//                                500,
//                                defaultWindowHeight,
//                                Color.LIGHTGREY
//                                );
// 
//         //Add the rectangle to the canvas node
//         mainCanvas.getChildren().add(canvasRect);
//         //Add the blocks to the canvas over the rect
//         //root.setLeft(blocks);
//         //Add canvas to the scene
//         root.setCenter(mainCanvas);
//         mainCanvas.getChildren().add(flowChart);
//         //Creating Event handler for click to add block
// 
//         // creating start block
//         Command s = new Command("start", "");
//         CommandBlock start = new CommandBlock(1,2,Color.GREY,s);
//         flowChart.getChildren().add(start);
//         
//         // creating end block
//         Command e = new Command("end", "");
//         CommandBlock end = new CommandBlock(1,2,Color.GREY,e);
//         flowChart.getChildren().add(end);
//         
//         EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
//             @Override
//             public void handle(MouseEvent e){
//                 // removing end block from GUI
//                 flowChart.getChildren().remove(end);
//                 
//                 // appending new command block to GUI and to commandList
//                 commandList.addCommandToFlow(commandList.getFlowSize(), "echo", interp);
//                 Command c = commandList.getCommand(commandList.getFlowSize()-1);
//                 CommandBlock block = new CommandBlock(1,2,Color.WHITE,c);
//                 flowChart.getChildren().add(block);
//                 
//                 // appending end block to GUI
//                 flowChart.getChildren().add(end);
//             }
//         };
//         //Linking the eventhandler to the canvas rectangle
//         flowChart.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
//         //add scene to stage
//         stage.setScene(scene);
//         //show scene
//         stage.show();
//     }
    
    /*
    stop is like init, but it goes right after the application ends
    The superclass definition also does nothing, so I commented this out
    @Override
    public void stop() {

    }
    
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
