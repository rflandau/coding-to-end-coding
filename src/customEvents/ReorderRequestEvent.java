package customEvents;

import javafx.event.Event;
import javafx.event.EventType;
import prefabs.CommandBlock;

public class ReorderRequestEvent extends Event{
    //publicly declaring this event's type
    public static EventType<ReorderRequestEvent> VSPReorderEvent = new EventType<ReorderRequestEvent>("VSPReorderEvent");  
    //idk what this is, but because events are serializable, Eclipse whined at me until I put it here
    private static final long serialVersionUID = 3397303991642114951L;
    
    //this event carries its source, 
    //and the x/y position of the object the source is being reordered relative to (the guest)
    CommandBlock source;
    double guestX;
    double guestY;

    public ReorderRequestEvent(CommandBlock requestee, double draggedX, double draggedY) {
        //VSPEvent is for events related to the VerticalSortingPane class
        super(VSPReorderEvent);
        
        this.source = requestee;
        this.guestX = draggedX - CommandBlock.width;
        this.guestY = draggedY - CommandBlock.height;
    }
    
    public CommandBlock getSource(){
        return source;
    }
    
    public double getGuestX() {
        return guestX;
    }
    
    public double getGuestY() {
        return guestY;
    }
}