package customevents;

import javafx.event.Event;
import javafx.event.EventType;
import prefabs.CommandBlock;

/*
    ReorderRequestEvent
    NOTE: finish these comments
*/
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

    /*
        getSource()
        NOTE: finish these comments
    */
    public CommandBlock getSource(){
        return source;
    }

    /*
        getGuestX()
        NOTE: finish these comments
    */
    public double getGuestX() {
        return guestX;
    }

    /*
        getGuestY()
        NOTE: finish these comments
    */
    public double getGuestY() {
        return guestY;
    }

    //static subroutines-------------------------------------------------------
}
