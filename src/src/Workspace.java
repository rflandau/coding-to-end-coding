package src;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;	//for sidebar shape
import javafx.scene.paint.Color;		//for sidebar color
import javafx.scene.layout.VBox;		//for vertical ordering of sidebar objects
import javafx.geometry.Insets;			//for sidebar spacing
//import javafx.scene.image.Image;
//I know import .* is gross, but I had to import, like, 6 things to get the background
//import javafx.scene.layout.*;
import javafx.stage.Stage;

import prefabs.ExportButton;

public class Workspace extends Application {
	//hard-coded window sizes, can be changed later
	double defaultWindowWidth = 800;
	double defaultWindowHeight = 600;
	
	//Applications do not need constructors
	//However, the program arguments from launch can be accessed with getParameters()
	
	/*
	init is called right before start, before the application comes into being
	Stuff for its parts shouldn't be made here, but anything that needs to be
	prepared for the starting of the app that isn't JavaFX can go here
	The superclass definition also does nothing, so I commented this out
	@Override
	public void init() {
		
	}
	*/
	//"/resources/images/WorkspaceBackgroundTile.png"
	@Override
	public void start(Stage stage) throws Exception {
		
		//scene (window) is created here
	    stage.setTitle("My JavaFX Application");
	    Group root = new Group();
	    Scene scene = new Scene(root, defaultWindowWidth, defaultWindowHeight);
	    stage.setScene(scene);
        
	    //making all the containers for visual elements
	    //VBoxes vertically order things. sidebar is for the sidebar, but NOT its contents
	    VBox sidebar = new VBox(10);	//this number spaces the elements
	    sidebar.setPadding(new Insets(10));	//this number puts a buffer around the box
	    ExportButton button = new ExportButton(150, 100);
	    Rectangle sidebarRect = new Rectangle(
        		150, 
        		defaultWindowHeight - button.getHeight(), 
        		Color.LIGHTGREY
						  );
	    sidebar.getChildren().addAll(button, sidebarRect);
        
	    //adding objects to the scene
	    root.getChildren().add(sidebar);
		
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
