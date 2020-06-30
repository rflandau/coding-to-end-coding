import java.util.ArrayList;
/* ScriptStruct
  Represents the main generation class for storing the flowchart and outputting
  to the selected out-lang.
  It is mostly a wrapper for the ArrayList 'flow', but is useful for organizing
    subroutines.
  'Flow' holds references to the commands stored in the hashtable in main.
  Declaration ("new" tag)should NOT be used with relation to flow or we will add
    a significant amount of overhead. */

import prefabs.Command;

public class ScriptStruct{
  //variables-------------------------------------------------------------------
  ArrayList<Command> flow;
  /*Set up the 'commands' hashtable.
    'commands' holds one instance of each command, set up at program start */
  Hashtable<String, Command> commands = new Hashtable<String, Command>();
  //constructors----------------------------------------------------------------
  public ScriptStruct(){
    flow = new ArrayList<Command>();
  }
  //subroutines-----------------------------------------------------------------
  /* generateScript
    Returns a (multi-line) string
  */
  boolean generateScript(Hashtable<String, Command> commands){

  }
  //static subroutines----------------------------------------------------------
}
