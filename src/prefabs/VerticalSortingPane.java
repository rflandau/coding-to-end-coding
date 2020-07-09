package prefabs;
//this might be more approprite to put in some other package, but it'll go here for right now

import javafx.scene.Node;
import javafx.scene.layout.Pane;
//A CommandBlock import isn't needed here, as it's currently in the same package

/*
 * A vertical sorting pane is like a VBox, but it doesn't lock elements in place. Instead, it reorders
 * elements after their moved. It'll do the whole "boxes moving when things hover over them" deal.
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
        
        this.topAnchor = topAnch;
        this.bottomAnchor = botAnch;
    }

}
