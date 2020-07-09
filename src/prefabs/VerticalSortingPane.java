package prefabs;
//this might be more appropriate to put in some other package, but it'll go here for right now

//visual/layout stuff
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
//functionality stuff
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
//our stuff
import structure.Command;
import structure.Interpreter;
import structure.ScriptStruct;
import customEvents.CorrectPosRequestEvent;
import customEvents.ReorderRequestEvent;
//A CommandBlock import isn't needed here, as it's currently in the same package

/*
 * A vertical sorting pane is like a VBox, but it doesn't lock elements in place. Instead, it reorders
 * elements after their moved. It'll do the whole "boxes moving when things hover over them" deal.
 * Be careful, VSPs assume they only contain CommandBlocks, since that's the only consistent object
 * in this project that has a definite size
 * I just need to figure out how it will know that things are moving...
 */
public class VerticalSortingPane extends Pane {
    CommandBlock topAnchor;
    CommandBlock bottomAnchor;
    ScriptStruct commandStruct;
    Interpreter commandInterp;
    
    //constructor that generates new CommandBlocks for the anchors
    public VerticalSortingPane(ScriptStruct cmdStruct, Interpreter cmdInterp) {
        super();
        
        //defining custom event handlers
        this.addEventHandler(ReorderRequestEvent.VSPReorderEvent, new onReorderRequest(this));
        this.addEventHandler(CorrectPosRequestEvent.VSPPosEvent, new onCorrectPosRequest(this));
        
        //setting attached structures
        this.commandStruct = cmdStruct;
        this.commandInterp = cmdInterp;
        
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
        commandStruct.addCommandToFlow(0, this.topAnchor.getCommandName(), this.commandInterp);
        commandStruct.addCommandToFlow(1, this.bottomAnchor.getCommandName(), this.commandInterp);
        
        //order the VSP
        this.refreshPane();
    }
    
    //constructor that uses pre-generated CommandBlocks
    public VerticalSortingPane(CommandBlock topAnch, CommandBlock botAnch, 
            ScriptStruct cmdStruct, Interpreter cmdInterp) {
        super();
        
        //defining custom event handlers
        this.addEventHandler(ReorderRequestEvent.VSPReorderEvent, new onReorderRequest(this));
        this.addEventHandler(CorrectPosRequestEvent.VSPPosEvent, new onCorrectPosRequest(this));
        
        //setting attached ScriptStruct
        this.commandStruct = cmdStruct;
        this.commandInterp = cmdInterp;
        
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
        
        //adding additional elements
        //this.addCommandBlock();
        
        //order the VSP
        this.refreshPane();
    }
    
    ///PUBLIC MANIPULATION METHODS
    //none of these methods actually effect the added/removed command block
    
    //adds an item to the VSP and automatically sorts it in
    public void addCommandBlock(CommandBlock newItem) {
        //guessedIndex is where the VSP thinks the new item should go. I'm not promising accuracy.
        double guessedIndex = (newItem.getLayoutY() + newItem.getTranslateY()) / CommandBlock.height;
        
        //if it's above the list, put it below the top anchor (which is index 0)
        if(guessedIndex < 0) {guessedIndex = 1;}
        //if it's below the list, put it above the bottom anchor (which is index ...size() - 1)
        if(guessedIndex >= this.getChildren().size()) {guessedIndex = this.getChildren().size() - 2;}
        
        //I cast gussedIndex to int here instead of making it an int because PEMDAS is a stinker
        this.getChildren().add((int)guessedIndex, newItem);
        this.refreshPane();
        
        //add it to the actual command flow
        this.commandStruct.addCommandToFlow((int)guessedIndex, newItem.getCommandName(), this.commandInterp);
    }
    
    //removes an item to the VSP and automatically resorts the list
    //returns the command block, as it isn't actually destroyed
    public CommandBlock removeCommandBlock(CommandBlock oldItem) {
        int oldItemIndex = this.getChildren().indexOf(oldItem);
        this.getChildren().remove(oldItem);
        this.refreshPane();
        
        //remove it from the command flow
        this.commandStruct.removeCommandFromFlow(oldItemIndex);
        
        return oldItem;
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
    
    //returns the Interpreter object assigned to the VSP
    public Interpreter getCommandInterp() {
        return this.commandInterp;
    }
    
    ///PRIVATE MANIPULATION METHODS
    //for when the VSP needs to mess with its own stuff
    
    //corrects the argument node's position in the VSP, if it was placed out of alignment
    void correctPosition(CommandBlock node) {
        //first, check if the node is actually in the VSP
        if(node.getParent().equals(this)) {
            //horizontally center the node to the top anchor
            node.setLayoutX(this.topAnchor.getLayoutX());
            /*
             * Adjust the vertical by assessing position. If nodeY % CommandBlock.height <= 50 (misalignment), 
             * we raise it by 50. If not, we lower it by 1 - that value. This is off of the 'low y = up high' 
             * verticality principal most graphical things use. But why'd I write it as a ternary?
             * Because every time I write a ternary, I get one step closer to my dream of becoming a
             * professional wrestler
             */
            double misalignment = node.getLayoutY() % CommandBlock.height;
            double adjustmentValue = misalignment <= CommandBlock.height/2 ? misalignment : -(CommandBlock.height - misalignment);
            //adjust the actual height
            //I don't know why, but it's always off by 1...
            node.setLayoutY(node.getLayoutY() - adjustmentValue + 1);
            //update index, if needed
            this.changeIndex((int)(node.getLayoutY() / CommandBlock.height), node);
        }
    }
    
    void reorderItem(CommandBlock source, double guestY) {
        //A node's scene position is the below values added together, I think
        //I modulo both values because I'm comparing source position to guest's point of contact.
        //By comparing the remainders, I can estimate which side it came from
        double sourceY = source.getLayoutY() + source.getTranslateY();
        System.out.println("source: " + sourceY);
        System.out.println("guest: " + guestY);
        
        //if the guest came from above the source...
        if(guestY > sourceY){   //I bet 1 shiny nickel that low y = up high, like in every other visual thing
            //shift the source down to make room for the guest
            source.setLayoutY(source.getLayoutY() + CommandBlock.height);
        }
        //else the guest came from above the source
        else {
            //shift the source up to make room for the guest
            source.setLayoutY(source.getLayoutY() - CommandBlock.height);
        }
        
        //update index of moved item, if needed
        this.changeIndex((int)(source.getLayoutY() / CommandBlock.height), source);
    }
    
    //refreshes the layout of the list. I expect this to be computationally expensive so use sparingly
    void refreshPane() {
        //The ObservableList contains every visible child of this object in an indexed list
        ObservableList<Node> nodeList = this.getChildren();
        
        //if the anchors aren't in the right spot, put them there
        //don't worry, the list will fix the indices for us
        if(nodeList.indexOf(this.topAnchor) != 0) {
            this.changeIndex(0, this.topAnchor);
        }
        
        if(nodeList.indexOf(this.bottomAnchor) != nodeList.size() - 1) {
            //we don't use changeIndex because bottomAnchor always needs a new, non-existing one
            this.commandStruct.removeCommandFromFlow(nodeList.indexOf(this.bottomAnchor));
            nodeList.remove(this.bottomAnchor);
            nodeList.add(this.bottomAnchor);
            this.commandStruct.addCommandToFlow(
                    nodeList.indexOf(this.bottomAnchor), 
                    this.bottomAnchor.getCommandName(), 
                    this.commandInterp
            );
        }
        
        //for each node in nodeList...
        for(Node node : nodeList) {
            //correct its position based on index
            node.setTranslateX(0);
            node.setLayoutX(0);
            //careful, another command block dependency
            node.setTranslateY(nodeList.indexOf(node) * CommandBlock.height);
            node.setLayoutY(0);
        }
    }
    
    ///PRIVATE HELPER METHODS
    
    //changes a node's index in the observable list
    void changeIndex(int newIndex, CommandBlock movingItem) {
        ObservableList<Node> nodeList = this.getChildren();
        //remove from old places
        this.commandStruct.removeCommandFromFlow(nodeList.indexOf(movingItem));
        nodeList.remove(movingItem);
        //placing into new places
        nodeList.add(newIndex, movingItem);
        this.commandStruct.addCommandToFlow(newIndex, movingItem.getCommandName(), this.commandInterp);
    }

}

class onReorderRequest implements EventHandler<ReorderRequestEvent>{
    VerticalSortingPane VSP;
    
    onReorderRequest(VerticalSortingPane VSP){
        super();
        this.VSP = VSP;
    }

    /*
     * what happens when a CommandBlock wants to be reordered
     * the event receives the block that wants to move and the position of the mouse.
     * We can infer the moving 'block' size from CommandBlock's static size, and position from the mouse
     * Source is the object asking to be reordered, and Guest is the object it is making room for
     *  (because when you have a guest you make room for them get it?
     */
    @Override
    public void handle(ReorderRequestEvent event) {
        VSP.reorderItem(event.getSource(), event.getGuestY());
    }
    
}

class onCorrectPosRequest implements EventHandler<CorrectPosRequestEvent>{
    VerticalSortingPane VSP;
    
    onCorrectPosRequest(VerticalSortingPane VSP){
        super();
        this.VSP = VSP;
    }

    //what happens when a CommandBlock asks the VSP to align it with the other blocks
    @Override
    public void handle(CorrectPosRequestEvent event) {
        //correct position
        this.VSP.correctPosition(event.getSource());
    }
    
}