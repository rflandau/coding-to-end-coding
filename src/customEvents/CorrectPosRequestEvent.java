package customEvents;

import javafx.event.Event;
import javafx.event.EventType;
import prefabs.CommandBlock;

/*
    CorrectPosRequestEvent
    NOTE: finish these commands
*/
public class CorrectPosRequestEvent extends Event {
    //variables----------------------------------------------------------------
    public static EventType<CorrectPosRequestEvent> VSPPosEvent =
        new EventType<CorrectPosRequestEvent>("VSPPosEvent");           // type
    private static final long serialVersionUID = 251645018293029384L;   // ???
    CommandBlock source;    // this event only carries its source

    //constructors-------------------------------------------------------------
    /*
        constructor that generates new CommandBlocks for the anchors
    */
    public CorrectPosRequestEvent(CommandBlock source) {
        super(VSPPosEvent);
        
        this.source = source;
    }
    
    //subroutines--------------------------------------------------------------
    public CommandBlock getSource() {
        return this.source;
    }
    
    //static subroutines-------------------------------------------------------
}
