package prefabs;
//this might be more appropriate to put in some other package, but it'll go here for right now

//visual/layout stuff
import javafx.scene.Node;
import javafx.scene.layout.Pane;
//import javafx.scene.paint.Color;
//functionality stuff
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
//our stuff
//import structure.Command;
//import structure.Interpreter;
//import structure.ScriptStruct;
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

    int height;
    
    //constructor that generates new CommandBlocks for the anchors
    public VerticalSortingPane() {
        super();
        
        //defining custom event handlers
        this.addEventHandler(ReorderRequestEvent.VSPReorderEvent, new onReorderRequest(this));
        this.addEventHandler(CorrectPosRequestEvent.VSPPosEvent, new onCorrectPosRequest(this));

	//init values
	height = 0;
    }
    
    ///PUBLIC MANIPULATION METHODS
    //none of these methods actually effect the added/removed command block
    
    //adds an item to the VSP and automatically sorts it in
    public void addCommandBlock(CommandBlock newItem) {
        //guessedIndex is where the VSP thinks the new item should go. I'm not promising accuracy.
        double guessedIndex = (newItem.getLayoutY() + newItem.getTranslateY()) / CommandBlock.height;
        int maximumIndex = this.getChildren().size();
        
        //if it's above the list, put it at the top (where is index 0)
        if(guessedIndex < 0) {guessedIndex = 0;}
        //if it's below the list, put it at the bottom (which is index ...size() - 1)
        if(guessedIndex >= maximumIndex) {guessedIndex = maximumIndex - 1;}
        //if the list is empty, re-correct to the top (0) again
        if(maximumIndex == 0) {guessedIndex = 0;}
        
        //I cast gussedIndex to int here instead of making it an int variable because PEMDAS is a stinker
        this.getChildren().add((int)guessedIndex, newItem);
        this.refreshPane();
    }
    
    //removes an item to the VSP and automatically resorts the list
    //returns the command block, as it isn't actually destroyed
    public CommandBlock removeCommandBlock(CommandBlock oldItem) {
        this.getChildren().remove(oldItem);
        this.refreshPane();
        
        return oldItem;
    }
    
    //Get height of the VSP
    public int getVSPHeight(){
	return height;
    }
    
    ///PRIVATE MANIPULATION METHODS
    //for when the VSP needs to mess with its own stuff
    
    //corrects the argument node's position in the VSP, if it was placed out of alignment
    void correctPosition(CommandBlock node) {
        //first, check if the node is actually in the VSP
        if(node.getParent().equals(this)) {
            //horizontally center the node to the top element
            //node.setLayoutX(this.getChildren().get(0).getLayoutX());
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
            //node.setLayoutY(node.getLayoutY() - adjustmentValue + 1);
            double newY = node.localToParent(0, 0).getY() - adjustmentValue;
            node.relocate(0, newY);
        }
    }
    
    void reorderItem(CommandBlock source, double guestY) {
        //I modulo both values because I'm comparing source position to guest's point of contact.
        //By comparing the remainders, I can estimate which side it came from
        
        //0, 0 represents the location of source relative to itself
        double sourceY = source.localToParent(0, 0).getY();
        System.out.println("source: " + sourceY);
        System.out.println("guest: " + guestY);
        
        //if the guest came from above the source...
        if(guestY > sourceY){   //I bet 1 shiny nickel that low y = up high, like in every other visual thing
            //shift the source down to make room for the guest
            System.out.println("Attempting to relocate to " + (sourceY + CommandBlock.height));
            source.relocate(0, sourceY + CommandBlock.height);
        }
        //else the guest came from above the source
        else {
            //shift the source up to make room for the guest
            System.out.println("Attempting to relocate to " + (sourceY - CommandBlock.height));
            source.relocate(0, sourceY - CommandBlock.height);
            
        }
        double newY = source.localToParent(0, 0).getY();
        System.out.println("Relocated from " + sourceY + " to " + newY);
        
        ///TEMP CODE TO HANDLE MISPOSITIONINGS THAT SHOULDN'T HAPPEN
        //highestIndex is the biggest index an item can have right now
        int highestIndex = this.getChildren().size() - 1;
        //if the new position goes too high...
        if(newY > highestIndex * 100) {
            System.err.println("Command block overshot list. Relocating to " + highestIndex * 100);
            source.relocate(0, highestIndex * 100);
            newY = highestIndex * 100;
        }
        //or too low
        if(newY < 0) {
            System.err.println("Command block overshot list. Relocating to " + (double)0);
            source.relocate(0, highestIndex * 100);
            newY = 0;
        }
        ///END TEMP CODE
        
        //change it's index to be the new height divided by 100
        this.changeIndex((int)(newY / CommandBlock.height), source);
    }
    
    //refreshes the layout of the list. I expect this to be computationally expensive so use sparingly
    void refreshPane() {
        //The ObservableList contains every visible child of this object in an indexed list
        ObservableList<Node> nodeList = this.getChildren();
        
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
    //this is done manually because list.set() replaces anything at the destination index
    void changeIndex(int newIndex, CommandBlock movingItem) {
        ObservableList<Node> nodeList = this.getChildren();
        
        //if removing the item from the list would move the destination position forward 1, move the index
        //      forward 1
        if(nodeList.indexOf(movingItem) > newIndex) {--newIndex;}
        ///TEMP CODE TO DEAL WITH ITEMS OVERSHOOTING THE LIST
        //check to see if something overshot where it should go
        if(newIndex > nodeList.size() - 1) {
            newIndex = nodeList.size() - 1;
            System.out.println("an item overshot the list");
        }
        if(newIndex < 0) {
            newIndex = 0;
            System.out.println("an item undershot the list");
        }
        ///END TEMP CODE
        //remove from old places
        nodeList.remove(movingItem);
        //placing into new places
        nodeList.add(newIndex, movingItem);
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
