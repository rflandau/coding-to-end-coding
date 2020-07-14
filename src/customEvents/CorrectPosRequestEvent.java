package customEvents;

import javafx.event.Event;
import javafx.event.EventType;
import prefabs.CommandBlock;

public class CorrectPosRequestEvent extends Event {
    //publicly declaring this event's type
    public static EventType<CorrectPosRequestEvent> VSPPosEvent= new EventType<CorrectPosRequestEvent>("VSPPosEvent");
    //idk what this is, but because events are serializable, Eclipse whined at me until I put it here
    private static final long serialVersionUID = 251645018293029384L;
    
    //this event only carries its source
    CommandBlock source;

    public CorrectPosRequestEvent(CommandBlock source) {
        super(VSPPosEvent);
        
        this.source = source;
    }
    
    public CommandBlock getSource() {
        return this.source;
    }
}
