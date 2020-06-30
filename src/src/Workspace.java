package src;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import prefabs.ExportButton;

public class Workspace extends Application {
	
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

	@Override
	public void start(Stage stage) throws Exception {
		Circle circ = new Circle(40, 40, 30);
		ExportButton button = new ExportButton();
        Group root = new Group(circ, button);
        Scene scene = new Scene(root, 800, 600);

        stage.setTitle("My JavaFX Application");
        stage.setScene(scene);
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
