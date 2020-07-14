package prefabs;
//this might be more approprite to put in some other package, but it'll go here for right now

import javafx.scene.Node;
import javafx.scene.layout.Pane;

/*
    VerticalSortingPane
    A vertical sorting pane is like a VBox, but it doesn't lock elements in
    place. Instead, it reorders elements after their moved. It'll do the whole
    "boxes moving when things hover over them" deal.
    I just need to figure out how it will know that things are moving...
*/
public class VerticalSortingPane extends Pane {
    Node topAnchor;     // anchors top of pane
    Node bottomAnchor;  // anchors bottom of pane

    /*
        default constructor
    */
    public VerticalSortingPane() {
        super();
    }

    /*
        another constructor
    */
    public VerticalSortingPane(Node... arg0) {
        super(arg0);
    }
    
    /*
        another(nother) constructor
    */
    public VerticalSortingPane(CommandBlock topAnch, CommandBlock botAnch) {
        super(topAnch, botAnch);
        
        this.topAnchor = topAnch;
        this.bottomAnchor = botAnch;
    }

}
