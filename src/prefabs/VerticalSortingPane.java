package prefabs;
//this might be more appropriate to put in some other package, but it'll go here for right now

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.event.EventHandler;
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
    Node topAnchor;
    Node bottomAnchor;

    //default constructor
    public VerticalSortingPane() {
        super();
    }

    //default constructor
    public VerticalSortingPane(Node... arg0) {
        super(arg0);
    }
    
    public VerticalSortingPane(CommandBlock topAnch, CommandBlock botAnch) {
        super(topAnch, botAnch);
        
        //defining custom event handlers
        this.addEventHandler(ReorderRequestEvent.VSPEvent, new onReorderRequest());
        this.addEventHandler(CorrectPosRequestEvent.VSPEvent, new onCorrectPosRequest(this));
        
        //setting anchors
        this.topAnchor = topAnch;
        this.bottomAnchor = botAnch;
    }
    
    //corrects the argument node's position in the VSP, if it was placed out of alignment
    void correctPosition(CommandBlock node) {
        //first, check if the node is actually in the VSP
        if(node.getParent().equals(this)) {
            //horizontally center the node
            node.setTranslateX(0);
            /*
             * Adjust the vertical by assessing position. If nodeY % CommandBlock.height <= 50 (misalignment), 
             * we raise it by 50. If not, we lower it by 1 - that value. This is off of the 'low y = up high' 
             * verticality principal most graphical things use. But why'd I write it as a ternary?
             * Because every time I write a ternary, I get one step closer to my dream of becoming a
             * professional wrestler
             */
            double misalignment = node.getTranslateY() % CommandBlock.height;
            double adjustmentValue = misalignment <= 50 ? misalignment : -(CommandBlock.height - misalignment);
            node.setTranslateY(node.getTranslateY() - adjustmentValue);
        }
    }

}

class onReorderRequest implements EventHandler<ReorderRequestEvent>{

    /*
     * what happens when a CommandBlock wants to be reordered
     * the event receives the block that wants to move and the position of the mouse.
     * We can infer the moving 'block' size from CommandBlock's static size, and position from the mouse
     * Source is the object asking to be reordered, and Guest is the object it is making room for
     *  (because when you have a guest you make room for them get it?
     */
    @Override
    public void handle(ReorderRequestEvent event) {
        CommandBlock source = event.getSource();
        //A node's scene position is the below values added together, I think
        double sourceY = source.getLayoutY() + source.getTranslateY();
        double guestY = event.getGuestY();
        
        //if the guest came from above the source...
        if(guestY > sourceY){   //I bet 1 shiny nickel that low y = up high, like in every other visual thing
            //shift the source up to make room for the guest
            source.setTranslateY(source.getTranslateY() - CommandBlock.height);
        }
        //else the guest came from below the source
        else {
            //shift the source down to make room for the guest
            source.setTranslateY(source.getTranslateY() + CommandBlock.height);
        }
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