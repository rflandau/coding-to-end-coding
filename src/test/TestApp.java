package test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import prefabs.CommandBlock;
import structure.Command;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class TestApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Circle circ = new Circle(10, Color.GREY);
        circ.setOnDragDetected(new circDrag(circ));
        circ.setOnMouseReleased(new circDrop(circ));
        Group root = new Group(new CommandBlock(
                100, 
                200, 
                Color.YELLOW, 
                new Command(
                        "echo HelloWorld", "baseBlock"
                )
        ),new CommandBlock(
                700, 
                500, 
                Color.GREEN, 
                new Command(
                        "echo HelloWorld", "baseBlock"
                )
        ), circ);
        Pane testBox = new Pane(new CommandBlock(
                100, 
                200, 
                Color.YELLOW, 
                new Command(
                        "echo HelloWorld", "baseBlock"
                )
        ),new CommandBlock(
                700, 
                500, 
                Color.GREEN, 
                new Command(
                        "echo HelloWorld", "baseBlock"
                )
        ));
        Scene scene = new Scene(testBox, 800, 600);
        
        //add scene to stage
        stage.setScene(scene);
        //show scene
        stage.show();
    }

}


class circDrag implements EventHandler<MouseEvent>{
    Circle target;
    
    circDrag(Circle circ){
        super();
        target = circ;
    }
    
    public void handle(MouseEvent event) {
        System.out.println("starting circ drag");
        
        target.startFullDrag();
        
        event.consume();
    }
}


class circDrop implements EventHandler<MouseEvent>{
    Circle target;
    
    circDrop(Circle circ){
        super();
        this.target = circ;
    }
    
    public void handle(MouseEvent event) {
        System.out.println("circ dropped");
        
        //this.target.relocate(event.getX(), event.getY());
        this.target.setTranslateX(event.getSceneX());
        this.target.setTranslateY(event.getSceneY());
        System.out.println("now at " + this.target.getTranslateX() + ", " + this.target.getTranslateY());

        event.consume();
    }
}
