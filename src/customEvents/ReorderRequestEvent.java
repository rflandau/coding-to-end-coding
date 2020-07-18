package customEvents;

import javafx.event.Event;
import javafx.event.EventType;
import prefabs.CommandBlock;

public class ReorderRequestEvent extends Event{
    //variables----------------------------------------------------------------
    public static EventType<ReorderRequestEvent> VSPReorderEvent =
        new EventType<ReorderRequestEvent>("VSPReorderEvent");  // event type
    private static final long serialVersionUID = 3397303991642114951L; // ???
    CommandBlock    source;     // event source
    double          guestX;     // relative X position of element reordering to
    double          guestY;     // relative Y position of element reordering to

    //constructors-------------------------------------------------------------
    /*
        constructor that generates new CommandBlocks for the anchors
    */
    public ReorderRequestEvent(CommandBlock requestee, double draggedX,
        double draggedY) {
        //VSPEvent is for events related to the VerticalSortingPane class
        super(VSPReorderEvent);
        
        this.source = requestee;
        this.guestX = draggedX - CommandBlock.width;
        this.guestY = draggedY - CommandBlock.height;
    }
    
    //subroutines--------------------------------------------------------------
    
    /*
        getSource()
        Getter for source. overrides original getSource to return CommandBlock
        objects rather than nodes.
    */
    @Override
    public CommandBlock getSource(){
        return source;
    }
    
    //GuestX Getter
    public double getGuestX() {
        return guestX;
    }
    
    //GuestY Getter
    public double getGuestY() {
        return guestY;
    }
    
    //static subroutines-------------------------------------------------------
}
