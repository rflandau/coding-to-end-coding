package prefabs;

//visual/layout
import javafx.scene.Node;
import javafx.scene.layout.Pane;
//functionality
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
//our stuff
import customEvents.CorrectPosRequestEvent;
import customEvents.ReorderRequestEvent;
import customEvents.SelfRemoveRequestEvent;

/*
    A vertical sorting pane is like a VBox, but it doesn't lock elements in
    place. Instead, it reorders elements after their moved. It'll do the whole
    "boxes moving when things hover over them" deal. Be careful, VSPs assume
    they only contain CommandBlocks, since that's the only consistent object
    in this project that has a definite size
    I just need to figure out how it will know that things are moving...
*/
public class VerticalSortingPane extends Pane {
    //constructors-------------------------------------------------------------
    /*
        constructor that generates new CommandBlocks for the anchors
    */
    public VerticalSortingPane() {
        super();
        
        //defining custom event handlers
        this.addEventHandler(ReorderRequestEvent.VSPReorderEvent,
            new onReorderRequest(this));
        this.addEventHandler(CorrectPosRequestEvent.VSPPosEvent,
            new onCorrectPosRequest(this));
        this.addEventHandler(SelfRemoveRequestEvent.VSPSelfRemoveEvent,
            new onSelfRemoveRequest(this));
    }
    
    //subroutines--------------------------------------------------------------
    //NOTE: none of these methods actually 
    
    /*
        addCommmandBlock()
        adds an item to the VSP and automatically sorts it in
        doesn't affect the added/removed command block
    */
    public void addCommandBlock(CommandBlock newItem) {
        //guessedIndex is where the VSP thinks the new item should go
        int guessedIndex = (int)(newItem.localToParent(0, 0).getY() /
            CommandBlock.height);
        int maximumIndex = this.getChildren().size();
        
        //if it's above the list, put it at the top (where is index 0)
        if(guessedIndex < 0) {guessedIndex = 0;}
        //if it's below the list, put it at the bottom (index...size()-1)
        if(guessedIndex >= maximumIndex) {guessedIndex = maximumIndex - 1;}
        //if the list is empty, re-correct to the top (0) again
        if(maximumIndex == 0) {guessedIndex = 0;}
        
        //add newItem to the VSP
        this.getChildren().add(guessedIndex, newItem);
        //update newItem's home
        newItem.setHomeX(0);
        newItem.setHomeY(guessedIndex * CommandBlock.height);
        
        this.refreshPane();
    }
    
    /*
        removeCommandBlock()
        removes an item to the VSP and automatically resorts the list
        returns the command block, as it isn't actually destroyed
    */
    public CommandBlock removeCommandBlock(CommandBlock oldItem) {
        this.getChildren().remove(oldItem);
        this.refreshPane();
        
        System.out.println("old item " + oldItem.getCommandName() + " removed");
        
        return oldItem;
    }
    
    //NOTE: for when the VSP needs to mess with its own stuff
    /*
        correctPosition()
        corrects the argument node's position in the VSP, if it was placed
        out of alignment
    */
    void correctPosition(CommandBlock node) {
        //first, check if the node is actually in the VSP
        if(node.getParent().equals(this)) {
            double  misalignment,
                    newY;
            
            // Calculate the distance to where it should correct to via modulo
            misalignment = node.localToParent(0, 0).getY() %
                CommandBlock.height;
            //adjust the actual height;
            newY = node.localToParent(0, 0).getY() - misalignment;
            
            System.out.println("Correcting from " +
                node.localToParent(0, 0).getY() + " to " + newY);
            node.relocate(0, newY);
            //update node's home
            node.setHomeX(0);
            node.setHomeY(newY);
        }
    }
    
    /*
        reorderItem()
        uses modulo of both values to compare source position to guest's
        point of contact. By comparing the remainders, we can estimate which
        side it came from
    */
    void reorderItem(CommandBlock source, double guestY) {
        //0, 0 represents the location of source relative to itself
        double sourceY = source.localToParent(0, 0).getY();
        //Adjusting position value to compensate for size
        guestY += CommandBlock.height;
        
        //if the guest came from below the source...
        if(guestY > sourceY){
            //shift the source down to make room for the guest
            source.relocate(0, sourceY + CommandBlock.height);
        }
        //else the guest came from above the source
        else {
            //shift the source up to make room for the guest
            source.relocate(0, sourceY - CommandBlock.height);
            
        }
        double newY = source.localToParent(0, 0).getY();
        System.out.println("Relocated from " + sourceY + " to " + newY);
        
        ///TEMP CODE TO HANDLE MISPOSITIONINGS THAT SHOULDN'T HAPPEN
        //highestIndex is the biggest index an item can have right now
        int highestIndex = this.getChildren().size() - 1;
        //if the new position goes too high...
        if(newY > highestIndex * 100) {
            System.err.println("Command block overshot list. Relocating to " +
                highestIndex * 100);
            source.relocate(0, highestIndex * 100);
            newY = highestIndex * 100;
        }
        //or too low
        if(newY < 0) {
            System.err.println("Command block overshot list. Relocating to " +
                (double)0);
            source.relocate(0, highestIndex * 100);
            newY = 0;
        }
        ///END TEMP CODE
        
        //update source's home
        source.setHomeX(0);
        source.setHomeY(newY);
        
        //change it's index to be the new height divided by 100
        this.changeIndex((int)(newY / CommandBlock.height), source);
    }
    
    /*
        refreshPane()
        refreshes the layout of the list. I expect this to be computationally
        expensive so use sparingly
    */
    void refreshPane() {
        //contains every visible child of this object in an indexed list
        ObservableList<Node> nodeList = this.getChildren();
        
        for(Node node : nodeList) {
            //correct position based on index
            node.relocate(0, nodeList.indexOf(node) * CommandBlock.height);
            
            /*
                I know instanceof is sketchy, but I know everything in a VSP is
                a CommandBlock. It's only handling nodes because that's the
                only type getChildren can return, as far as I understand. I'm
                using instanceof to wrap the following typecast (even worse, I
                know) so this function has at least slight reasonablity
            */
            if(node instanceof CommandBlock) {
                CommandBlock nodeNeedsHome = (CommandBlock) node;
                nodeNeedsHome.setHomeX(0);
                nodeNeedsHome.setHomeY(nodeList.indexOf(node) *
                    CommandBlock.height);
            }
        }
    }
    
    /*
        changeIndex()
        changes a node's index in the observable list
        this is done manually because list.set() replaces anything at the
        destination index
    */
    
    void changeIndex(int newIndex, CommandBlock movingItem) {
        ObservableList<Node> nodeList = this.getChildren();
        
        /*
            if removing the item from the list would move the destination
            position forward 1, move the index
            forward 1
        */
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
    //static subroutines-------------------------------------------------------
}

/*
    onReorderRequest
    NOTE: finish these comments
*/
class onReorderRequest implements EventHandler<ReorderRequestEvent>{
    //variables----------------------------------------------------------------
    VerticalSortingPane VSP;    // instantiation of VerticalSortingPane
    
    //constructors-------------------------------------------------------------
    /*
        constructor
    */
    onReorderRequest(VerticalSortingPane VSP){
        super();
        this.VSP = VSP;
    }

    //subroutines--------------------------------------------------------------
    /*
        handle()
        what happens when a CommandBlock wants to be reordered
        the event receives the block that wants to move and the position of the
        mouse. We can infer the moving 'block' size from CommandBlock's static
        size, and position from the mouse Source is the object asking to be
        reordered, and Guest is the object it is making room for
        because when you have a guest you make room for them get it?
    */
    @Override
    public void handle(ReorderRequestEvent event) {
        VSP.reorderItem(event.getSource(), event.getGuestY());
    }
    //static subroutines-------------------------------------------------------
}

/*
    onCorrectPosRequest
    NOTE: finish these comments
*/
class onCorrectPosRequest implements EventHandler<CorrectPosRequestEvent>{
    //variables----------------------------------------------------------------
    VerticalSortingPane VSP;    // instantiation of VerticalSortingPane
    
    //constructors-------------------------------------------------------------
    /*
        constructor
    */
    onCorrectPosRequest(VerticalSortingPane VSP){
        super();
        this.VSP = VSP;
    }

    //subroutines--------------------------------------------------------------
    /*
        handle()
        what happens when a CommandBlock asks the VSP to align it with the
        other blocks
    */
    @Override
    public void handle(CorrectPosRequestEvent event) {
        //correct position
        this.VSP.correctPosition(event.getSource());
    }
    //static subroutines-------------------------------------------------------
}

class onSelfRemoveRequest implements EventHandler<SelfRemoveRequestEvent>{
    //variables----------------------------------------------------------------
    VerticalSortingPane     VSP;    //the VSP that handles this event

    //constructors-------------------------------------------------------------
    /*
        constructor
    */
    onSelfRemoveRequest(VerticalSortingPane VSP){
        super();
        this.VSP = VSP;
    }
    
    //subroutines--------------------------------------------------------------
    /*
        handle()
        what happens when an event wants to remove itself
    */
    @Override
    public void handle(SelfRemoveRequestEvent event) {
        this.VSP.removeCommandBlock(event.getCommandBlock());
        
        return;
    }

    //static subroutines-------------------------------------------------------
}
