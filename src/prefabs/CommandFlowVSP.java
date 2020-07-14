package prefabs;

//import customEvents.CorrectPosRequestEvent;
//import customEvents.ReorderRequestEvent;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
//import javafx.event.EventHandler;
import javafx.scene.Node;
//import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import structure.Command;
import structure.Interpreter;
import structure.ScriptStruct;

/* CommandFlowVSPs are just VSPs that also order items in a stored ScriptStruct. They behave almost exactly the
 * same. To reflect this, even in areas where it's slightly less efficient to do so, methods just call their
 * super version with some small command flow tweaks
 */

public class CommandFlowVSP extends VerticalSortingPane {
    ScriptStruct commandStruct;
    
    //constructor that generates new CommandBlocks for the anchors
    public CommandFlowVSP(ScriptStruct cmdStruct) {
        super();
        
        //setting attached structures
        this.commandStruct = cmdStruct;
        
    }
    
    ///PUBLIC MANIPULATION METHODS
    //none of these methods actually effect the added/removed command block
    
    //adds an item to the VSP and automatically sorts it in
    @Override
    public void addCommandBlock(CommandBlock newItem) {
        //add it to the VSP
        super.addCommandBlock(newItem);
        
        //add it to the actual command flow
        this.commandStruct.addCommandToFlow(this.getChildren().indexOf(newItem), newItem.getCommandName());
    }
    
    //removes an item to the VSP and automatically resorts the list
    //returns the command block, as it isn't actually destroyed
    @Override
    public CommandBlock removeCommandBlock(CommandBlock oldItem) {
        int oldItemIndex = this.getChildren().indexOf(oldItem);
        //remove it from the command flow
        this.commandStruct.removeCommandFromFlow(oldItemIndex);
        
        //remove it from the VSP
        return super.removeCommandBlock(oldItem);
    }
    
    //returns the ScriptStruct object assigned to the VSP
    public ScriptStruct getCommandStruct() {
        return this.commandStruct;
    }
    
    ///PRIVATE MANIPULATION METHODS
    //for when the VSP needs to mess with its own stuff
    
    @Override
    void reorderItem(CommandBlock source, double guestY) {
        //we just want the super method with commandflow trappings
        super.reorderItem(source, guestY);
        
        //update index of moved item in the flow, if needed
        this.changeFlowIndex(this.getChildren().indexOf(source), source);
    }
    
    /*//refreshes the layout of the list. I expect this to be computationally expensive so use sparingly
    @Override
    void refreshPane() {
        //The ObservableList contains every visible child of this object in an indexed list
        ObservableList<Node> nodeList = this.getChildren();
        
        //if the anchors aren't in the right spot, put them there
        //don't worry, the list will fix the indices for us
        if(nodeList.indexOf(this.topAnchor) != 0) {
            this.changeIndex(0, this.topAnchor);
        }
        
        if(nodeList.indexOf(this.bottomAnchor) != nodeList.size() - 1) {
            //we don't use changeIndex because bottomAnchor always needs a new, non-existing index value
            this.commandStruct.removeCommandFromFlow(nodeList.indexOf(this.bottomAnchor));
            nodeList.remove(this.bottomAnchor);
            nodeList.add(this.bottomAnchor);
            this.commandStruct.addCommandToFlow(
                    nodeList.indexOf(this.bottomAnchor), 
                    this.bottomAnchor.getCommandName()
            );
        }
        
        //we want the super version, just with the anchors figured out first
        super.refreshPane();
    }*/
    
    ///PRIVATE HELPER METHODS
    
    //changes a node's index in the command flow
    void changeFlowIndex(int newIndex, CommandBlock movingItem) {
        ObservableList<Node> nodeList = this.getChildren();
        //remove from old places
        this.commandStruct.removeCommandFromFlow(nodeList.indexOf(movingItem));
        this.commandStruct.addCommandToFlow(newIndex, movingItem.getCommandName());
    }

}
