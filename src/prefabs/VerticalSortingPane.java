package prefabs;

//visual/layout
import javafx.scene.Node;
import javafx.scene.layout.Pane;
//functionality
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
//our stuff
import customevents.CorrectPosRequestEvent;
import customevents.SelfRemoveRequestEvent;

/*
    VerticalSortingPane
    like a VBox, but it doesn't lock elements in place. Instead, it reorders
    elements after their moved. It'll do the whole "boxes moving when things
    hover over them" deal. Be careful, VSPs assume they only contain
    CommandBlocks, since that's the only consistent object in this project
    that has a definite size I just need to figure out how it will know that
    things are moving...
*/
public class VerticalSortingPane extends Pane {
    int height;
    //constructors-------------------------------------------------------------
    /*
        constructor that generates new CommandBlocks for the anchors
    */
    public VerticalSortingPane() {
        super();

        //defining custom event handlers
        this.addEventHandler(CorrectPosRequestEvent.VSPPosEvent,
            new onCorrectPosRequest(this));
        this.addEventHandler(SelfRemoveRequestEvent.VSPSelfRemoveEvent,
            new onSelfRemoveRequest(this));

        //init values
        this.height = 0;
    }

    //subroutines--------------------------------------------------------------

    /*
        addCommmandBlock()
        adds an item to the VSP and automatically sorts it in
        doesn't affect the added/removed command block
    */
    public void addCommandBlock(CommandBlock newItem) {
    int lastIndex = this.getChildren().size();
        //add newItem to the VSP
        this.getChildren().add(lastIndex, newItem);
        //update newItem's home
        newItem.setHomeX(0);
        newItem.setHomeY(lastIndex * CommandBlock.height);

    //update height
    this.height += newItem.height;

        this.refreshPane();
    }

    /*
        removeCommandBlock()
        removes an item to the VSP and automatically resorts the list
        returns the command block, as it isn't actually destroyed
        doesn't affect the added/removed command block
    */
    public CommandBlock removeCommandBlock(CommandBlock oldItem) {
        this.getChildren().remove(oldItem);
        this.refreshPane();
          
        this.height -= oldItem.height;

        return oldItem;
    }

    //Get height of the VSP
    public int getVSPHeight(){
        return this.height;
    }

    /*
        correctPosition()
        corrects the argument node's position in the VSP, if it was placed
        out of alignment
        doesn't affect the added/removed command block
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

            //change its index, if needed
            this.changeIndex(
                    (int)(node.getHomeY() / CommandBlock.height), node
            );
        }
    }

    /*
        reorderItem()
        moves command blocks up or down the list
    */

    void reorderItem(CommandBlock source, boolean reorderingUp) {
        //0, 0 represents the location of source relative to itself
        double sourceY = source.localToParent(0, 0).getY();

        //if the guest came from below the source shift the source up
        if(reorderingUp){source.relocate(0, sourceY - CommandBlock.height);}

        //else the guest came from above the source
        else {
            //shift the source down to make room for the guest
            source.relocate(0, sourceY + CommandBlock.height);
        }
        double newY = source.localToParent(0, 0).getY();
        System.out.println("Relocated from " + sourceY + " to " + newY);

        ///TEMP CODE TO HANDLE MISPOSITIONINGS THAT SHOULDN'T HAPPEN
        //highestIndex is the biggest index an item can have right now
        int highestIndex = this.getChildren().size() - 1;
        //if the new position goes too high...
        if(newY > highestIndex * CommandBlock.height) {
            System.err.println("Command block overshot list. Relocating to " +
                highestIndex * CommandBlock.height);
            source.relocate(0, highestIndex * CommandBlock.height);
            newY = highestIndex * CommandBlock.height;
        }
        //or too low
        if(newY < 0) {
            System.err.println("Command block overshot list. Relocating to " +
                (double)0);
            source.relocate(0, highestIndex * CommandBlock.height);
            newY = 0;
        }

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
        doesn't affect the added/removed command block
    */
    void refreshPane() {
        //contains every visible child of this object in an indexed list
        ObservableList<Node> nodeList = this.getChildren();

        //for each node in nodeList...
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
        doesn't affect the added/removed command block
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
        this.VSP.refreshPane();
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
