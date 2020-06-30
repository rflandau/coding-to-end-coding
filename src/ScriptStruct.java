import java.util.ArrayList;
/* ScriptStruct
  Represents the main generation class for storing the flowchart and outputting
  to the selected out-lang.
  It is mostly a wrapper for the ArrayList 'flow', but is useful for organizing
    subroutines.
  'Flow' holds references to the commands stored in the hashtable in main.
  Declaration ("new" tag)should NOT be used with relation to flow or we will add
    a significant amount of overhead. */

public class ScriptStruct{
  //variables-------------------------------------------------------------------
  private ArrayList<Command> flow;
  //constructors----------------------------------------------------------------
  public ScriptStruct(){
    flow = new ArrayList<Command>();
  }
  //subroutines-----------------------------------------------------------------
  //static subroutines----------------------------------------------------------
}
