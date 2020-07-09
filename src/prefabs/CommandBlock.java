/*
 * TO-DO
 *  switching from holding a Command object to a Command object's id
 *  functionality for replacing the Command Blocks in the side bar
 *  functionality for correcting command block position
 */


package prefabs;

//visuals
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
//import event handling
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.geometry.Pos;
import javafx.scene.input.MouseDragEvent;
//import our other packages
import structure.Command;
import structure.ScriptStruct;
import structure.Interpreter;

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
        StackPane.setAlignment(rect, Pos.CENTER);
        StackPane.setAlignment(text, Pos.CENTER);
        this.getChildren().addAll(rect, text);

        //placing the Command Block in the correct spot
        this.relocate(xPos, yPos);
	
        //adding drag and drop events
        this.setOnDragDetected(new onCommandBlockDrag(this));
        this.setOnMouseDragged(new onCommandBlockMove(this));
        this.setOnMouseReleased(new onCommandBlockDrop(this));
        this.setOnMouseDragEntered(new onCommandBlockHover(this));
        /*
         * Be careful, these events calculate position based on the top-left corner of the
         * command block. I hardcoded in an offset based off of the above static values, but
         * if we ever stop using those we'll have to come back to this.
         * It will probably involve CommandBlocks simply knowing their own size.
         */
    }
    
    //allows the changing of a command block's home location, if need be
    public void setHome(livesOn newHome) {
        this.home = newHome;
    }
    
    //returns a deep copy of the command block this method is called on
    //can we just steal java.lang.obj.clone for this?
    public CommandBlock Copy() {
        //we use translateX and translateY as these most accurately represent where the block should be
        return new CommandBlock(
                this.getTranslateX(), 
                this.getTranslateY(), 
                this.commandColor, 
                this.attachedCommand);
    }

}


class onCommandBlockDrag implements EventHandler<MouseEvent>{
    CommandBlock targetBlock;

    onCommandBlockDrag(CommandBlock block){
        super();
        this.targetBlock = block;
    }

    //what happens when the user starts dragging the command block
    @Override
    public void handle(MouseEvent event) {
        System.out.println("Starting drag");
        
        targetBlock.setMouseTransparent(true);
        
        targetBlock.startFullDrag();
        
        event.consume();
    }
}

class onCommandBlockMove implements EventHandler<MouseEvent>{
    CommandBlock targetBlock;

    onCommandBlockMove(CommandBlock block){
        super();
        this.targetBlock = block;
    }
    
    //what happens when the user continuously drags the command block
    @Override
    public void handle(MouseEvent event) {
        targetBlock.relocate(
                event.getSceneX() - CommandBlock.width/2, 
                event.getSceneY() - CommandBlock.height/2
        );
        
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
        //if this block was dragged from the sidebar...
        if(targetBlock.home == CommandBlock.livesOn.SIDEBAR) {
            //set following if to true for temp value
            if(true/*The block landed on the workspace*/) {
                //create a copy of this command block
                //add it to the scene graph
            }

            //no matter what, blocks from the side bar will return to their original position
            targetBlock.relocate(targetBlock.homeX, targetBlock.homeY);
        }

        //if this block was dragged from the workspace...
        if(targetBlock.home == CommandBlock.livesOn.WORKSPACE) {
            if(false/*The block landed on the sidebar*/) {
            //delete it
            }
            //else, move it to this new position
            else {
                targetBlock.relocate(
                        event.getSceneX() - CommandBlock.width/2, 
                        event.getSceneY() - CommandBlock.height/2
                );
            }
        }
        
        targetBlock.setMouseTransparent(false);

        event.consume();
    }
}

class onCommandBlockHover implements EventHandler<MouseEvent>{
    CommandBlock targetBlock;

    onCommandBlockHover(CommandBlock block){
        super();
        this.targetBlock = block;
    }
    
    //what happens when something (probably a Command Block) is dragged over this
    @Override
    public void handle(MouseEvent event) {
        
    }
    
}
