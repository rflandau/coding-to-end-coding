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
import javafx.geometry.Point2D;
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
import customEvents.CorrectPosRequestEvent;
import customEvents.ReorderRequestEvent;

public class CommandBlock extends StackPane {
	//variables-----------------------------------------------------------------
    /*
      I'm assuming that every block is the same size. Rectangle has a native constructor
      for size and color, so it's trivial to create a constructor that takes
      size, position, and color
    */
    public static double    width = 100,
                            height = 100;
    static enum livesOn     {SIDEBAR, WORKSPACE}

    //instance variables
    Command     attachedCommand;
    Paint       commandColor;
    livesOn     home;    //where the CommandBlock originally came from
    double      homeX,
                homeY;
    boolean     draggable; //if the CommandBlock can be dragged or not
    //currently uninitialized
    int         listIndex;
    //I'm not sure if blocks will need to store their linked list connections, if they do
    //    I'll put them here
    ContextMenu contextMenu;
    MenuItem    deleteBlock;

    ScriptStruct commandList;
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
    
    //gets the name of the command the block is currently carrying
    public String getCommandName() {
        return this.attachedCommand.getName();
    }
    
    //toggles a command block's ability to move
    public void setDraggable(boolean wantsToMove) {
        if(wantsToMove) {
            this.setOnDragDetected(new onCommandBlockDrag(this));
            this.setOnMouseDragged(new onCommandBlockMove(this));
            this.setOnMouseReleased(new onCommandBlockDrop(this));
            this.setOnMouseDragEntered(new onCommandBlockHover(this));
        }
        else {
            this.setOnDragDetected(new onCommandBlockDrag(null));
            this.setOnMouseDragged(new onCommandBlockMove(null));
            this.setOnMouseReleased(new onCommandBlockDrop(null));
            this.setOnMouseDragEntered(new onCommandBlockHover(null));
        }
    }

    //returns a deep copy of the command block this method is called on
	public CommandBlock copy() {
		//we use localToScene(0, 0) as it translates the position of the block relative to itself (0, 0) to
	    //the position of the block relative to the sene
		return new CommandBlock(
		this.localToScene(0, 0).getX(),
		this.localToScene(0, 0).getY(),
		this.commandColor,
		this.attachedCommand,
		this.commandList);
	}
	public void delete(){
		System.out.println("delete Called");
		this.getChildren().remove(0,2);
		//There is currently no way to find the index so this is commented out
		//commandList.removeCommandFromFlow(listIndex);
	}
}

///CUSTOM EVENTS

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
        //expose other events to the mouse during the drag
        targetBlock.setMouseTransparent(true);
        
        //reset translates to match mouse position
        targetBlock.setTranslateX(0);
        targetBlock.setTranslateY(0);
        targetBlock.relocate(
                event.getSceneX() - CommandBlock.width/2, 
                event.getSceneY() - CommandBlock.height/2
        );
        
        targetBlock.startFullDrag();
        
        event.consume();
    }
}

/*
    onCommandBlockMove
    TODO: finish these comments
*/
class onCommandBlockMove implements EventHandler<MouseEvent>{
	CommandBlock targetBlock;

	onCommandBlockMove(CommandBlock block){
		super();
		this.targetBlock = block;
	}

	//what happens when the user continuously drags the command block
	@Override
	public void handle(MouseEvent event) {
	    //relocate needs parent-relative coordinates. The event gives scene-relative coordinates.
	    //We need to go from scene to local to parent, which is why the methods below are used
	    Point2D newPosition = targetBlock.localToParent(targetBlock.sceneToLocal(
	            event.getSceneX() - CommandBlock.width/2,
	            event.getSceneY() - CommandBlock.height/2
	    ));
		targetBlock.relocate(newPosition.getX(), newPosition.getY());

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
                        event.getSceneY() - CommandBlock.height/2
                );
                
                //ask its container to align it, if it can handle CorrectPosRequestEvent
                targetBlock.getParent().fireEvent(new CorrectPosRequestEvent(targetBlock));
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

	onCommandBlockHover(CommandBlock block){
		super();
		this.targetBlock = block;
	}
    
    //what happens when something (probably a Command Block) is dragged over this
    @Override
    public void handle(MouseEvent event) {
        //when dragged over, the command block passes itself and the point of contact(?) to its container,
        //  if the container can handle a ReorderRequest
        Point2D newPosition = targetBlock.localToParent(targetBlock.sceneToLocal(
                event.getSceneX() - CommandBlock.width/2,
                event.getSceneY() - CommandBlock.height/2
        ));
        targetBlock.getParent().fireEvent(
                new ReorderRequestEvent(targetBlock, newPosition.getX(), newPosition.getY())
        );
    }
    
}
