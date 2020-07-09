package test;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import prefabs.CommandBlock;
import prefabs.VerticalSortingPane;
import structure.Command;
import structure.Interpreter;
import structure.ScriptStruct;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class TestApp extends Application {
    ScriptStruct commandList;
    Interpreter commandInterp;
    CommandBlock start;
    CommandBlock end;
    
  //new CommandBlock(1,2,Color.WHITE, new Command(),commandList);
    
    @Override
    public void init() {
        commandList = new ScriptStruct();
        commandInterp = Interpreter.generateInterpreters().get(0);
        //start = new CommandBlock(1,2,Color.GREY, new Command("start"), commandList);
        //end = new CommandBlock(1,2,Color.GREY, new Command("end"), commandList);
    }

    @Override
    public void start(Stage stage) throws Exception {
        VerticalSortingPane VSP = new VerticalSortingPane(commandList, commandInterp);
        
        VSP.addCommandBlock(new CommandBlock(1,2,Color.BLUE, new Command("start"), commandList));
        VSP.addCommandBlock(new CommandBlock(5433,4234,Color.GREEN, new Command("end"), commandList));
        VSP.addCommandBlock(new CommandBlock(0,0,Color.YELLOW, new Command("end"), commandList));
        VSP.addCommandBlock(new CommandBlock(9,200,Color.RED, new Command("end"), commandList));
        
        Scene scene = new Scene(VSP, 800, 600);
        
        //add scene to stage
        stage.setScene(scene);
        //show scene
        stage.show();
    }

}