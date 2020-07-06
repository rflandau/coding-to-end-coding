/*
 * TO-DO
 *  switching the Drag from a MouseEvent to a MouseDragEvent
 *  testing drag-and-drop functionality of blocks
 *  switching from holding a Command object to a Command object's id
 *  functionality for replacing the Command Blocks in the side bar
 *  functionality for correcting command block position
 */


package prefabs;

//visuals
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
//container
import javafx.scene.layout.StackPane;
//event handling
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
//our stuff
import structure.Command;
import structure.ScriptStruct;
//import core.Main;

public class CommandBlock extends StackPane {
    /*
      I'm assuming that every block is the same size. Rectangle has a native constructor
      for size and color, so it's trivial to create a constructor that takes
      size, position, and color
    */
    static double width = 100;
    static double height = 100;
    static enum livesOn{
        SIDEBAR,
        WORKSPACE
    }

    //instance variables
    Command attachedCommand;
    double homeX;
    double homeY;
    Paint commandColor;
    livesOn home;    //where the CommandBlock originally came from
    //I'm not sure if blocks will need to store their linked list connections, if they do
    //    I'll put them here
    

    //Command Blocks take their position, a JFX color, and the Command object they represent
    public CommandBlock(double xPos, double yPos, Paint color, Command cmd) {
        //creating the jfx container
        super();

        //storing the represented Command object
        this.attachedCommand = cmd;

        //setting home (where the Command Block is on the sidebar)
        this.homeX = xPos;
        this.homeY = yPos;
        this.home = livesOn.WORKSPACE;

        //creating the visual shape and name label (and saving the block's color)
        this.commandColor = color;
        Rectangle rect = new Rectangle(CommandBlock.width, CommandBlock.height, color);
        Label text = new Label(cmd.getName());

        //adds visuals to the container
        this.getChildren().addAll(rect, text);

        //placing the Command Block in the correct spot
        this.relocate(xPos, yPos);
            
        //adding drag and drop events
        //may not be necessary?
        this.setOnDragDetected(new onCommandBlockDrag(this));
        this.setOnMouseReleased(new onCommandBlockDrop(this));
    }
    
    
      // function to add command to the command list
    public void addToFlow(ScriptStruct cmdList){
        //cmdList.addCommandToFlow(attachedCommand);
    }
    
    // function to remove command from the command list
    public void removeFromFlow(ScriptStruct cmdList) {
        //cmdList.removeCommandFromFlow(attachedCommand);
    }
    
    //allows the changing of a command block's home location, if need be
    public void setHome(livesOn newHome) {
        this.home = newHome;
    }
    
    //returns a deep copy of the command block this method is called on
    public CommandBlock Copy() {
        //we use translateX and translateY as these most accurately represent where the block should be
        return new CommandBlock(
                this.getTranslateX(), 
                this.getTranslateY(), 
                this.commandColor, 
                this.attachedCommand);
    }

}


//These might not actually achieve what I want them to do, so I'm gonna ignore them for right now.
class onCommandBlockDrag implements EventHandler<MouseEvent>{
    CommandBlock targetBlock;

    onCommandBlockDrag(CommandBlock block){
        super();
        this.targetBlock = block;
    }

    //what happens when the user starts dragging the command block
    @Override
    public void handle(MouseEvent event) {
        System.out.println("starting drag");

        targetBlock.startFullDrag();
        
        event.consume();
    }
}

class onCommandBlockDrop implements EventHandler<MouseEvent>{
    CommandBlock targetBlock;

    onCommandBlockDrop(CommandBlock block){
        super();
        this.targetBlock = block;
    }

    //what happens when the user drops the command block
    @Override
    public void handle(MouseEvent event) {
        System.out.println("I am done dragging");
        
        //if this block was dragged from the sidebar...
        if(targetBlock.home == CommandBlock.livesOn.SIDEBAR) {
            //set following if to true for temp value
            if(true/*The block landed on the workspace*/) {
                //create a copy of this command block
                //add it to the scene graph
            }

            //no matter what, blocks from the side bar will return to their original position
            targetBlock.setTranslateX(targetBlock.homeX);
            targetBlock.setTranslateY(targetBlock.homeY);
        }

        //if this block was dragged from the workspace...
        if(targetBlock.home == CommandBlock.livesOn.WORKSPACE) {
            if(false/*The block landed on the sidebar*/) {
            //delete it
            }
            //else, move it to this new position
            else {
                targetBlock.setTranslateX(event.getSceneX());
                targetBlock.setTranslateY(event.getSceneY());
            }
        }

        event.consume();
    }
}
