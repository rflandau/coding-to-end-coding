package test;

//Old code, but thomas is a hoarder so he's deleting it later
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import prefabs.CommandBlock;
import prefabs.CommandFlowVSP;
import prefabs.VerticalSortingPane;
import structure.Command;
import structure.Interpreter;
import structure.ScriptStruct;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class TestApp extends Application {
    ScriptStruct structure;
    Interpreter commandInterp;
    CommandBlock start;
    CommandBlock end;
    ArrayList<Command> commands;
    
  //new CommandBlock(1,2,Color.WHITE, new Command(),commandList);
    
    @Override
    public void init() {
        structure = new ScriptStruct();
        commands = structure.getTemplateCommands();
    }

    @Override
    public void start(Stage stage) throws Exception {
        CommandFlowVSP VSP = new CommandFlowVSP(structure);
        
        /*
            TestApp is almost never functional, only being brought up when small parts need
            to be guess and checked. The test package should be removed once we're done.
        */
        /*VSP.addCommandBlock(new CommandBlock(1,2,Color.BLUE, commands.get(0), structure));
        VSP.addCommandBlock(new CommandBlock(5433,4234,Color.GREEN, commands.get(0), structure));
        VSP.addCommandBlock(new CommandBlock(0,0,Color.YELLOW, commands.get(0), structure));
        VSP.addCommandBlock(new CommandBlock(9,200,Color.RED, commands.get(0), structure));*/
        
        Scene scene = new Scene(VSP, 800, 600);
        
        //add scene to stage
        stage.setScene(scene);
        //show scene
        stage.show();
    }

}