//I know this class doesn't follow the style guide but these fields are long af

package customEvents;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import prefabs.CommandBlock;

public class SelfRemoveRequestEvent extends Event {
//fields-----------------------------------------------------------------------    
    public static EventType<SelfRemoveRequestEvent>     
                                VSPSelfRemoveEvent  =   new EventType
                                                        <SelfRemoveRequestEvent>
                                                        ("VSPSelfRemoveEvent");
                                                        /*publicly declaring
                                                        this event's type*/
    private static final long   serialVersionUID    =   -5601291677092066594L;
                                /*needed because events are serializable. 
                                It needs to have this specific casing as well*/
    
    CommandBlock                source; //the block that asked for the removal

//constructors-----------------------------------------------------------------    
    public SelfRemoveRequestEvent(CommandBlock requestee) {
        super(VSPSelfRemoveEvent);
        
        this.source = requestee;
    }
    
//subroutines------------------------------------------------------------------
    //getters/setters
    public CommandBlock getCommandBlock() {return this.source;}
    
//static subroutines-----------------------------------------------------------
}
