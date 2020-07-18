package prefabs;

//visuals
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
//import event handling
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
//Context Menu(separated so if it gets moved its clear what can go)
import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
//import our other packages
import structure.Command;
import structure.ScriptStruct;
import customevents.CorrectPosRequestEvent;
import customevents.SelfRemoveRequestEvent;
import prefabs.TextPanel;

/*
    CommandBlock
    TODO: finish these comments
*/
public class CommandBlock extends StackPane {
    //nord theme, frost colors
    final String            nord7   = "#8FBCBB",
                            nord8   = "#88C0D0",
                            nord9   = "#81A1C1",
                            nord10  = "#5E81AC";
    //variables-----------------------------------------------------------------
    /*
        I'm assuming that every block is the same size. Rectangle has a native
        constructor for size and color, so it's trivial to create a constructor
        that takes size, position, and color
    */
    public static double    width = 100,    // width in pixels
                            height = 100;   // height in pixels
    static enum livesOn     {SIDEBAR, WORKSPACE} // possible original locations
    Command                 attachedCommand;// command this block represents
    Paint                   commandColor;   // block's color
    livesOn                 home;           // where the block came from
    double                  homeX,          // x position when not moving
                            homeY;          // y position when not moving
    boolean                 draggable;      // if the block can be dragged
    int                     listIndex;      // TODO: finish these comments
    ContextMenu             contextMenu;    // TODO: finish these comments
    MenuItem                deleteBlock,    // TODO: finish these comments
                            editBlock;      // TODO: finish these comments
    ScriptStruct            commandList;    // TODO: finish these comments
    TextPanel               txtBox;         // TODO: finish these comments
    Rectangle               rect;           // block's physical body
    Label                   text;           // name of command on block body
    String                  argument,       // TODO: finish these comments
	                        edited;         // TODO: finish these comments

    //constructors--------------------------------------------------------------
    /*
        Argument Desciptors:
        xPos, the x coordinate of the command block
        yPos, the y coordinate of the command block
        color, the color of the command block
        cmd, the command that the command block contains and represents
        cmdL, the ScriptStruct reference
    */
    public CommandBlock(double xPos, double yPos,
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
        this.commandColor = Color.web(nord10);
        this.rect = new Rectangle(CommandBlock.width,
				  CommandBlock.height,
				  commandColor);
        this.text = new Label(cmd.getName());

        //adds visuals to the container
        StackPane.setAlignment(rect, Pos.CENTER);
        StackPane.setAlignment(text, Pos.CENTER);
        this.getChildren().addAll(rect, text);

        //placing the Command Block in the correct spot
        this.relocate(xPos, yPos);
        
        /*
            adding drag and drop events
            Be careful, these events calculate position based on the top-left
            corner of the command block. I hardcoded in an offset based off of
            the above static values, but if we ever stop using those we'll
            have to come back to this.
            It will probably involve CommandBlocks simply knowing their own
            size.
         */
        this.setOnDragDetected(new OnCommandBlockDrag(this));
        this.setOnMouseDragged(new OnCommandBlockMove(this));
        this.setOnMouseReleased(new OnCommandBlockDrop(this));
        
        //Init ContextMenu
        
        contextMenu = new ContextMenu();
        deleteBlock = new MenuItem("Delete Command");

        //ContextMenu Behavior
        deleteBlock.setOnAction(new OnContextDelete(this));

        contextMenu.getItems().addAll(deleteBlock);
        rect.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>(){
            @Override
            public void handle(ContextMenuEvent event){
                contextMenu.show(rect, event.getScreenX(), event.getScreenY());
            }
        });
        text.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>(){
            @Override
            public void handle(ContextMenuEvent event){
                contextMenu.show(rect, event.getScreenX(), event.getScreenY());
            }
        });
    }
    //subroutines--------------------------------------------------------------

    //argument Getter
    public String getArgument(){
        return this.argument;
    }
    
    //attachedCommand's name Getter
    public String getCommandName() {
        return this.attachedCommand.getName();
    }

    //attachedCommand Getter
    public Command getCommand(){
        return attachedCommand;
    }
    
    //toggles a command block's ability to move
    public void setDraggable(boolean wantsToMove) {
        if(wantsToMove) {
            this.setOnDragDetected(new OnCommandBlockDrag(this));
            this.setOnMouseDragged(new OnCommandBlockMove(this));
            this.setOnMouseReleased(new OnCommandBlockDrop(this));
        }
        else {
            this.setOnDragDetected(new OnCommandBlockDrag(null));
            this.setOnMouseDragged(new OnCommandBlockMove(null));
            this.setOnMouseReleased(new OnCommandBlockDrop(null));
        }
    }
    
    //homeX getter
    public double getHomeX() {return this.homeX;}

    //homeY getter
    public double getHomeY() {return this.homeY;}

    //homeX setter
    public void setHomeX(double newHomeX) {this.homeX = newHomeX;}

    //homeY setter
    public void setHomeY(double newHomeY) {this.homeY = newHomeY;}

    //txtBox setter
    public void setEditBox(TextPanel txtPanel){this.txtBox = txtPanel;}
    
    /*
        newArgument()
        Sets the argument line of the command block and duplicates the changes
        backwards to ScriptStruct using the adapter CFVSP.
    */
    public void newArgument(String inStr){
	    this.argument = inStr;
	    if (inStr.length() > 0){
	        edited = " ...";
	        this.text.setText(attachedCommand.getName()+edited);

            CommandFlowVSP cfvsp = (CommandFlowVSP) this.getParent();
            cfvsp.setCommandSyntax(this);
	    }
    }


    public void onSidebar(boolean val) {
	    if(val) this.home = livesOn.SIDEBAR;
    }

    //returns a deep copy of the command block this method is called on
    public CommandBlock copy() {
	//we use localToScene(0, 0) to translate the block's position relative to itself to the scene
	return new CommandBlock(
				this.localToScene(0, 0).getX(),
				this.localToScene(0, 0).getY(),
				this.attachedCommand,
				this.commandList);
    }

    /*
        delete()
        removes a CommandBlock from its parent.
        Protected to give access to CommandBlock's private event
        class without making it public
    */
    protected void delete(){
        /*
        firing this event calls removeCommandBlock on the parent
        without needing to know the type of the parent
        This of course means that if this's parent isn't a VSP,
        this method does nothing, but it would've crashed the program
        in that situation in its previous incarnation
        */
        this.getParent().fireEvent(new SelfRemoveRequestEvent(this));
    }

    private void setAsEdit(){
	    txtBox.setEdit(this);
	    this.rect.setStroke(Color.GREEN);
    }

    public void closeEdit(){
	    this.rect.setStroke(Color.LIGHTBLUE);
    }




    public void setContextMenu(){
        contextMenu = new ContextMenu();
        deleteBlock = new MenuItem("Delete Command");
	    editBlock = new MenuItem("Edit Command");

        //ContextMenu Behavior
        deleteBlock.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
		        delete();
	        }
        });

	editBlock.setOnAction(new EventHandler<ActionEvent>(){
	    @Override
	    public void handle(ActionEvent event){
		    setAsEdit();
	    }
	});

	//Add blocks to context menu
        contextMenu.getItems().addAll(deleteBlock, editBlock);

	this.rect.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>(){
            @Override
            public void handle(ContextMenuEvent event){
                contextMenu.show(rect, event.getScreenX(), event.getScreenY());
            }
        });
	this.text.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>(){
            @Override
            public void handle(ContextMenuEvent event){
                contextMenu.show(rect, event.getScreenX(), event.getScreenY());
            }
        });
    }
}

//event handlers classes--------------------------------------------------------
class OnCommandBlockDrag implements EventHandler<MouseEvent>{
    //variables----------------------------------------------------------------
    CommandBlock targetBlock;  // the block being dragged

    //constructors-------------------------------------------------------------
    /*
        constructor
    */
    OnCommandBlockDrag(CommandBlock block){
        super();
        this.targetBlock = block;
    }

    //subroutines--------------------------------------------------------------
    /*
        handle()
        what happens when the user starts dragging the command block
    */
    @Override
    public void handle(MouseEvent event) {
        //expose other events to the mouse during the drag
        targetBlock.setMouseTransparent(true);

        targetBlock.startFullDrag();

        event.consume();
        return;
    }
    //static subroutines-------------------------------------------------------
}

class OnCommandBlockMove implements EventHandler<MouseEvent>{
    //variables----------------------------------------------------------------
    CommandBlock targetBlock;   //the block being dragged

    //constructors-------------------------------------------------------------
    /*
        constructor
    */
    OnCommandBlockMove(CommandBlock block){
        super();
        this.targetBlock = block;
    }

    //subroutines--------------------------------------------------------------
    /*
        handle()
        what happens when the user continuously drags the command block
    */
    @Override
    public void handle(MouseEvent event) {
        /*
            relocate needs parent-relative coordinates. The event gives
            scene-relative coordinates.We need to go from scene to local to
            parent, which is why the methods below are used
        */
        Point2D newPosition = targetBlock.localToParent(
            targetBlock.sceneToLocal(
                event.getSceneX() - CommandBlock.width/2,
                event.getSceneY() - CommandBlock.height/2
            )
        );
        targetBlock.relocate(newPosition.getX(), newPosition.getY());

        event.consume();
    }
    //static subroutines-------------------------------------------------------
}

class OnCommandBlockDrop implements EventHandler<MouseEvent>{
    //variables----------------------------------------------------------------
    CommandBlock targetBlock;  // the block being dropped

    //constructors-------------------------------------------------------------
    /*
        constructor
    */
    OnCommandBlockDrop(CommandBlock block){
        super();
        this.targetBlock = block;
    }

    //subroutines--------------------------------------------------------------
    /*
        handle()
        what happens when the user drops the command block
    */
    @Override
    public void handle(MouseEvent event) {
        //correct block's position, if parent is a VSP
        targetBlock.getParent().fireEvent(
                new CorrectPosRequestEvent(targetBlock));

        targetBlock.setMouseTransparent(false);

        event.consume();
    }
    //static subroutines-------------------------------------------------------
}

class OnContextDelete implements EventHandler<ActionEvent>{
    //fields-------------------------------------------------------------------
      CommandBlock targetBlock;    //the VSP that handles this event

    //constructors-------------------------------------------------------------
      OnContextDelete(CommandBlock targetBlock){
          super();
          this.targetBlock = targetBlock;
      }

    //subroutines--------------------------------------------------------------
      //what happens when an event wants to remove itself
      @Override
      public void handle(ActionEvent event) {
          this.targetBlock.getParent().fireEvent(
                  new SelfRemoveRequestEvent(this.targetBlock)
          );
          this.targetBlock.delete();

          return;
      }
    //static subroutines-------------------------------------------------------
  }
