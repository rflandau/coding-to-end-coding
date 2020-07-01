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
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import prefabs.Command;

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
	livesOn home;	//where the CommandBlock originally came from
	//I'm not sure if blocks will need to store their linked list connections, if they do
	//	I'll put them here
	

	//Command Blocks take their position, a JFX color, and the Command object they represent
	public CommandBlock(double xPos, double yPos, Paint color, Command cmd) {
		//creating the jfx container
		super();
		
		//storing the represented Command object
		this.attachedCommand = cmd;
		
		//setting home (where the Command Block is on the sidebar)
		this.homeX = xPos;
		this.homeY = yPos;
		this.home = livesOn.SIDEBAR;
		
		//creating the visual shape and name label
		Rectangle rect = new Rectangle(CommandBlock.width, CommandBlock.height, color);
		Label text = new Label(cmd.getName());
		
		//adds visuals to the container
		this.getChildren().addAll(rect, text);

		//placing the Command Block in the correct spot
		this.relocate(xPos, yPos);
		
		//adding drag and drop events
		this.setOnDragDetected(new onCommandBlockDrag(this));
		this.setOnDragDropped(new onCommandBlockDrop(this));
	}
	
	//allows the changing of a command block's home location, if need be
	public void setHome(livesOn newHome) {
		this.home = newHome;
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
		//this warns as unused, but drag gets returned when the event is dispatched
		//it being returned starts the drag
        @SuppressWarnings("unused")
		Dragboard drag = targetBlock.startDragAndDrop(TransferMode.ANY);
        
        event.consume();
	}
}

class onCommandBlockDrop implements EventHandler<DragEvent>{
	CommandBlock targetBlock;
	
	onCommandBlockDrop(CommandBlock block){
		super();
		this.targetBlock = block;
	}
	
	//what happens when the user drops the command block
	@Override
	public void handle(DragEvent event) {
		//if this block was dragged from the sidebar...
		if(targetBlock.home == CommandBlock.livesOn.SIDEBAR) {
			if(/*The block landed on the workspace*/) {
				//create a copy of this command block
				//add it to the scene graph
			}
			
			//no matter what, blocks from the side bar will return to their original position
			targetBlock.relocate(targetBlock.homeX, targetBlock.homeY);
		}
		
		//if this block was dragged from the workspace...
		if(targetBlock.home == CommandBlock.livesOn.WORKSPACE) {
			if(/*The block landed on the sidebar*/) {
				//delete it
			}
			
			//the drag-drop event will set the new position automatically (i think)
		}
		
		event.setDropCompleted(true);
		
	    event.consume();
	}
}
