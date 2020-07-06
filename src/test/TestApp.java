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

public class TestApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Circle circ = new Circle(10, Color.GREY);
        circ.setOnDragDetected(new circDrag(circ));
        circ.setOnMouseReleased(new circDrop(circ));
        Group root = new Group(new CommandBlock(
                1, 
                2, 
                Color.YELLOW, 
                new Command(
                        "echo HelloWorld", "baseBlock"
                )
        ), circ);
        Scene scene = new Scene(root, 800, 600);
        
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

class dragDropMouseFollow extends AnimationTimer{
    Node target;
    
    dragDropMouseFollow(Node node){
        this.target = node;
    }

    @Override
    public void handle(long now) {
        //'now' is the current time on the timer. It goes unused here, but abstracts be like this so...
        this.target.relocate();
    }
    
}
