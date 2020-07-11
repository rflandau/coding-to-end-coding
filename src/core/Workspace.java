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

// import javafx.scene.Group;
// import javafx.scene.shape.Rectangle;    //for sidebar shape
// import javafx.scene.layout.BorderPane;  //for vertical ordering of sidebar objects
// import javafx.scene.layout.StackPane;   //for vertical ordering of sidebar objects
// import javafx.geometry.Insets;          //for sidebar spacing

import java.util.ArrayList;             //for backend ArrayList
import java.util.Hashtable;
import java.io.IOException;

//Import Event Handling
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

//import javafx.scene.image.Image;
//import javafx.scene.layout.*;


//import our other packages
import prefabs.ExportButton;
import prefabs.VerticalSortingPane;
import prefabs.CommandBlock;
import prefabs.CommandFlowVSP;
import structure.Command;
import structure.ScriptStruct;

public class Workspace extends Application {
    //hard-coded window sizes, can be changed later
    double              defaultWindowWidth = 800,
                        defaultWindowHeight = 600;
    ScriptStruct        structure;
    ArrayList<Command>  sidebarCommands;
    AnchorPane root;

    //Applications do not need constructors
    //However, the program arguments from launch can be accessed with getParameters()

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
    //create non-fxml items
    VerticalSortingPane sidebarVSP = new VerticalSortingPane();  // contains available commands
    CommandFlowVSP canvasBoxVSP = new CommandFlowVSP(structure); // contains flowchart
    sidebarVbox.getChildren().add(sidebarVSP);
    canvasBox.getChildren().add(canvasBoxVSP);
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
		@Override public void handle(MouseEvent e){addCommandBlock(canvasBoxVSP);}
    };
	//End of Event Handlers
	//----------------------------------------------------------
	//Linking Event Handlers to items
	sidebar.addEventFilter(MouseEvent.MOUSE_CLICKED, clickSideBarEvent);

	// populating available commands
        for(int i = 0; i < sidebarCommands.size(); i ++){
            Command c = sidebarCommands.get(i);
            CommandBlock b = new CommandBlock(1,2,Color.WHITE,c,structure);
            sidebarVSP.addCommandBlock(b);
        }
	
        stage.setScene(new Scene(root));
        stage.show();

        /*
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
        //I'm sorry the class names are so long
        
        
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
        //sidebar.setSpacing(10);   //VSPs don't have spacing (but maybe the should...)
        sidebarPane.setTopAnchor(sidebar, 10.0);
        sidebarPane.setLeftAnchor(sidebar, 145.0);
        sidebarScroll.setOrientation(Orientation.VERTICAL);
        sidebarPane.getChildren().add(sidebar);
        //sidebar.setAlignment(Pos.CENTER); //VSPs auto-align to center
        
        // populating available commands
        for(int i = 0; i < sidebarCommands.size(); i ++){
            Command c = sidebarCommands.get(i);
            CommandBlock b = new CommandBlock(1,2,Color.WHITE,c,commandList);
            sidebar.addCommandBlock(b);
        }

        // setting up flowchart
        flowchartPane.setTopAnchor(flowchart, 10.0);
        flowchartPane.setRightAnchor(flowchart, 145.0);
        flowchartScroll.setOrientation(Orientation.VERTICAL);
        flowchartPane.getChildren().add(flowchart);
        //flowchart.setAlignment(Pos.CENTER);   VSPs automatically align to their centers
        
        //CommandFlowVSP constructor creates its own anchors
        
        // setting up button pane
        ExportButton button = new ExportButton(100, 50, structure);
        exportPane.setRightAnchor(button, 145.0);
        exportPane.getChildren().add(button);

        // event handler
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e){
                //adding new command block
                //VSP handles the reordering the list and command flow automatically
                flowchart.addCommandBlock(new CommandBlock(
                        0, //VSP cares not for block position
                        0, 
                        Color.WHITE,
                        interp.getCommand("Hello World"), 
                        commandList)
                );
            }
        };
        sidebar.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);

        stage.setTitle("Coding to End Coding");
        stage.setScene(scene);
        stage.show(); */
    }


	/*
	*/
    public void addCommandBlock(CommandFlowVSP blockBox){
		int index = structure.getFlowSize();
		Command c = new Command("echo Hello World");
		CommandBlock block = new CommandBlock(1,2,Color.WHITE,c,structure);
                blockBox.addCommandBlock(block);
		/*int len = blockBox.getChildren().size();
		Rectangle endBlock = (Rectangle) blockBox.getChildren().get(len-1);
		blockBox.getChildren().remove(len-1);
		blockBox.getChildren().add(block);
		blockBox.getChildren().add(endBlock);*/
    }
}
