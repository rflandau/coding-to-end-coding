/*
    TODO
    switching from holding a Command object to a Command object's id
    functionality for replacing the Command Blocks in the side bar
    functionality for correcting command block position
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
//Context Menu(separated so if it gets moved its clear what can go)
import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
//import our other packages
import structure.Command;
import structure.ScriptStruct;
import structure.Interpreter;

public class CommandBlock extends StackPane {
    //variables-----------------------------------------------------------------
    static double   width = 100,                // width in pixels
                    height = 100;               // height in pixels
    static enum     livesOn{SIDEBAR,WORKSPACE}
    Command         attachedCommand;            // Command associated with block
    double          homeX,                      // X position
                    homeY;                      // Y position
    Paint           commandColor;               // color of the command block
    livesOn         home;                       // where the block came from
    ContextMenu     contextMenu;                // context menu
    MenuItem        deleteBlock;                // context menu item
    ScriptStruct    commandList;                // ScriptStruct containing flow
    
    //constructors--------------------------------------------------------------
    /*
        Argument Desciptors:
        xPos, the x coordinate of the command block
        yPos, the y coordinate of the command block
        color, the color of the command block
        cmd, the command that the command block contains and represents
        cmdL, the ScriptStruct reference
    */
    public CommandBlock(double xPos, double yPos, Paint color,
        Command cmd, ScriptStruct cmdL) {
        
        //creating the jfx container
        super();

        //storing the represented Command object
        this.attachedCommand = cmd;
        this.commandList = cmdL;

        //setting home (where the Command Block is on the sidebar)
        this.homeX = xPos;
        this.homeY = yPos;
        this.home = livesOn.WORKSPACE;

        //creating the visual shape and name label (and saving the block's color)
        this.commandColor = color;
        Rectangle rect = new Rectangle(CommandBlock.width,
            CommandBlock.height, color);
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
        
        //Init ContextMenu
        /*
            Be careful, these events calculate position based on the top-left
            corner of the command block. I hardcoded in an offset based off of
            the above static values, but if we ever stop using those we'll
            have to come back to this.
            It will probably involve CommandBlocks simply knowing their own
            size.
        */
        contextMenu = new ContextMenu();
        deleteBlock = new MenuItem("Delete Command");

        //ContextMenu Behavior
        deleteBlock.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){delete();}
        });

        contextMenu.getItems().addAll(deleteBlock);
        rect.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>(){
            @Override
            public void handle(ContextMenuEvent event){
                contextMenu.show(rect, event.getScreenX(), event.getScreenY());
            }
        });
    }
    //subroutines---------------------------------------------------------------

    /*
        setHome()
        allows the changing of a command block's home location, if need be
    */
    public void setHome(livesOn newHome) {
        this.home = newHome;
    }

    /*
        copy()
        returns a deep copy of the command block this method is called on
    */
    public CommandBlock copy() {
        // translateX and translateY accurately represent location
        return new CommandBlock(
        this.getTranslateX(),
        this.getTranslateY(),
        this.commandColor,
        this.attachedCommand,
        this.commandList);
    }
    public void delete(){
        System.out.println("delete Called");
        this.getChildren().remove(0,2);
    }
    //static subroutines--------------------------------------------------------
}

//event handlers classes--------------------------------------------------------
/*
    onCommandBlockDrag
    TODO: finish these comments
*/
class onCommandBlockDrag implements EventHandler<MouseEvent>{
    CommandBlock targetBlock;  // the block being dragged

    /*
        onCommandBlockDrag()
        TODO: finish these comments
    */
    onCommandBlockDrag(CommandBlock block){
        super();
        this.targetBlock = block;
    }

    /*
        handle()
        what happens when the user starts dragging the command block
    */
    @Override
    public void handle(MouseEvent event) {
        System.out.println("Starting drag");

        targetBlock.setMouseTransparent(true);

        targetBlock.startFullDrag();

        event.consume();
    }
}

/*
    onCommandBlockMove
    TODO: finish these comments
*/
class onCommandBlockMove implements EventHandler<MouseEvent>{
    CommandBlock targetBlock;  // the block being moved
    
    /*
        onCommandBlockMove()
        TODO: finish these comments
    */
    onCommandBlockMove(CommandBlock block){
        super();
        this.targetBlock = block;
    }

    /*
        handle()
        what happens when the user continuously drags the command block
    */
    @Override
    public void handle(MouseEvent event) {
        targetBlock.relocate(
        event.getSceneX() - CommandBlock.width/2,
        event.getSceneY() - CommandBlock.height/2
        );

        event.consume();
    }
}

/*
    onCommandBlockDrop
    TODO: finish these comments
*/
class onCommandBlockDrop implements EventHandler<MouseEvent>{
    CommandBlock targetBlock;  // the block being dropped

    /*
        onCommandBlockDrop()
        TODO: finish these comments
    */
    onCommandBlockDrop(CommandBlock block){
        super();
        this.targetBlock = block;
    }

    /*
        handle()
        what happens when the user drops the command block
    */
    @Override
    public void handle(MouseEvent event) {
        //if this block was dragged from the sidebar...
        if(targetBlock.home == CommandBlock.livesOn.SIDEBAR) {
            //set following if to true for temp value
            if(true/*The block landed on the workspace*/) {
                //create a copy of this command block
                //add it to the scene graph
            }

            // blocks from the side bar will return to their original position
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
                    event.getSceneY() - CommandBlock.height/2);
            }
        }

        targetBlock.setMouseTransparent(false);

        event.consume();
    }
}

/*
    onCommandBlockHover
    TODO: finish these comments
*/
class onCommandBlockHover implements EventHandler<MouseEvent>{
    CommandBlock targetBlock;  // the block being hovered over

    /*
        onCommandBlockHover()
        TODO: finish these comments
    */
    onCommandBlockHover(CommandBlock block){
        super();
        this.targetBlock = block;
    }

    /*
        handle()
        what happens when something (probably a Command Block) is
        dragged over this
    */
    @Override
    public void handle(MouseEvent event) {

    }

}
