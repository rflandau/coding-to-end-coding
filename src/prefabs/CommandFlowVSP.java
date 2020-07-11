package prefabs;

//import customEvents.CorrectPosRequestEvent;
//import customEvents.ReorderRequestEvent;
import javafx.collections.ObservableList;
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
    CommandBlock topAnchor;
    CommandBlock bottomAnchor;
    ScriptStruct commandStruct;
    //Interpreter commandInterp;
    
    //constructor that generates new CommandBlocks for the anchors
    public CommandFlowVSP(ScriptStruct cmdStruct) {
        super();
        
        //setting attached structures
        this.commandStruct = cmdStruct;
        
        //creating anchors
        this.topAnchor = new CommandBlock(0, 0, Color.GREY, new Command("start"), this.commandStruct);
        this.bottomAnchor = new CommandBlock(0, 0, Color.GREY, new Command("end"), this.commandStruct);
        //anchoring them
        this.topAnchor.setDraggable(false);
        this.bottomAnchor.setDraggable(false);
        //making them visible
        this.getChildren().add(0, this.topAnchor);
        this.getChildren().add(this.bottomAnchor);
        //adding to command flow
        commandStruct.addCommandToFlow(0, this.topAnchor.getCommandName());
        commandStruct.addCommandToFlow(1, this.bottomAnchor.getCommandName());
        
        //order the VSP
        this.refreshPane();
    }
    
    //constructor that uses pre-generated CommandBlocks
    public CommandFlowVSP(CommandBlock topAnch, CommandBlock botAnch, 
            ScriptStruct cmdStruct, Interpreter cmdInterp) {
        super();
        
        //setting attached ScriptStruct
        this.commandStruct = cmdStruct;
        
        //setting anchors
        this.topAnchor = topAnch;
        this.bottomAnchor = botAnch;
        //anchoring them
        this.topAnchor.setDraggable(false);
        this.bottomAnchor.setDraggable(false);
        //making them visible
        this.getChildren().add(0, this.topAnchor);
        this.getChildren().add(this.bottomAnchor);
        //it is assumed that these anchors are already in the command flow
        
        //order the VSP
        this.refreshPane();
    }
    
    ///PUBLIC MANIPULATION METHODS
    //none of these methods actually effect the added/removed command block
    
    //adds an item to the VSP and automatically sorts it in
    @Override
    public void addCommandBlock(CommandBlock newItem) {
        //guessedIndex is where the VSP thinks the new item should go. I'm not promising accuracy.
        double guessedIndex = (newItem.getLayoutY() + newItem.getTranslateY()) / CommandBlock.height;
        
        //if it's above the list, put it below the top anchor (which is index 0)
        if(guessedIndex <= 0) {guessedIndex = 1;}
        //if it's below the list, put it above the bottom anchor (which is index ...size() - 1)
        if(guessedIndex >= this.getChildren().size()) {guessedIndex = this.getChildren().size() - 2;}
        
        //I cast gussedIndex to int here instead of making it an int because PEMDAS is a stinker
        this.getChildren().add((int)guessedIndex, newItem);
        this.refreshPane();
        
        //add it to the actual command flow
        this.commandStruct.addCommandToFlow((int)guessedIndex, newItem.getCommandName());
    }
    
    //removes an item to the VSP and automatically resorts the list
    //returns the command block, as it isn't actually destroyed
    @Override
    public CommandBlock removeCommandBlock(CommandBlock oldItem) {
        int oldItemIndex = this.getChildren().indexOf(oldItem);
        
        //we just want the super method with CommandFlow trimmings
        CommandBlock removedItem = super.removeCommandBlock(oldItem);
        
        //remove it from the command flow
        this.commandStruct.removeCommandFromFlow(oldItemIndex);
        
        return removedItem;
    }
    
    //returns the topAnchor command block
    public CommandBlock getTopAnchor() {
        return this.topAnchor;
    }
    
    //returns the bottomAnchor command block
    public CommandBlock getBottomAnchor() {
        return this.bottomAnchor;
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
        //update index of moved item, if needed
        this.changeIndex((int)(source.getLayoutY() / CommandBlock.height), source);
    }
    
    //refreshes the layout of the list. I expect this to be computationally expensive so use sparingly
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
    }
    
    ///PRIVATE HELPER METHODS
    
    //changes a node's index in the observable list
    @Override
    void changeIndex(int newIndex, CommandBlock movingItem) {
        ObservableList<Node> nodeList = this.getChildren();
        //remove from old places
        this.commandStruct.removeCommandFromFlow(nodeList.indexOf(movingItem));
        super.changeIndex(newIndex, movingItem);
        this.commandStruct.addCommandToFlow(newIndex, movingItem.getCommandName());
    }

}