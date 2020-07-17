package prefabs;

import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import structure.Command;
import structure.Interpreter;
import structure.ScriptStruct;

/*  
    CommandFlowVSP
    VSPs that also order items in a stored ScriptStruct. They behave almost
    exactly the same. To reflect this, even in areas where it's slightly less
    efficient to do so, methods just call their super version with some small
    command flow tweaks
 */

public class CommandFlowVSP extends VerticalSortingPane {
    //variables----------------------------------------------------------------
    ScriptStruct commandStruct;     // ScriptStruct containing the flow
    
    //constructors-------------------------------------------------------------
    /*
        constructor that generates new CommandBlocks for the anchors
    */
    public CommandFlowVSP(ScriptStruct cmdStruct) {
        super();
        
        //setting attached structures
        this.commandStruct = cmdStruct;
        
    }
    
    //subroutines--------------------------------------------------------------
    //none of these methods actually effect the added/removed command block
    
    /*
        addCommandBlock()
        adds an item to the VSP and automatically sorts it in
        doesn't affect the added/removed command block
    */
    @Override
    public void addCommandBlock(CommandBlock newItem) {
        //add it to the VSP
        super.addCommandBlock(newItem);
        
        //add it to the actual command flow
        this.commandStruct.addCommandToFlow(
            this.getChildren().indexOf(newItem), newItem.getCommandName());
    }
    
    /*
        removes an item to the VSP and automatically resorts the list
        returns the command block, as it isn't actually destroyed
    */
    @Override
    public CommandBlock removeCommandBlock(CommandBlock oldItem) {
        int oldItemIndex = this.getChildren().indexOf(oldItem);
        //remove it from the command flow
        this.commandStruct.removeCommandFromFlow(oldItemIndex);
        
        //remove it from the VSP
        return super.removeCommandBlock(oldItem);
    }
    
    /*
        getCommandStruct()
        returns the ScriptStruct object assigned to the VSP
    */
    public ScriptStruct getCommandStruct() {
        return this.commandStruct;
    }
    
    /*
        reorderItem()
        NOTE: finish these comments
    */
    @Override
    void reorderItem(CommandBlock source, boolean reorderingUp) {
        //we just want the super method with commandflow trappings
        super.reorderItem(source, reorderingUp);
        
        //update index of moved item in the flow, if needed
        this.changeFlowIndex(this.getChildren().indexOf(source), source);
    }
    
    /*
        changeFlowIndex()
        changes a node's index in the command flow
    */
    void changeFlowIndex(int newIndex, CommandBlock movingItem) {
        ObservableList<Node> nodeList = this.getChildren();
        //remove from old places
        this.commandStruct.removeCommandFromFlow(nodeList.indexOf(movingItem));
        this.commandStruct.addCommandToFlow(newIndex,
            movingItem.getCommandName());
    }
    //static subroutines-------------------------------------------------------
}
