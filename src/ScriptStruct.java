import java.util.ArrayList;
import java.util.Hashtable;
import java.lang.StringBuilder;
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
  ArrayList<Command> flow;
  /*Set up the 'commands' hashtable.
    'commands' holds one instance of each command, set up at program start */
  Hashtable<String, Command> commands;
  //constructors----------------------------------------------------------------
  public ScriptStruct(){
    flow = new ArrayList<Command>();
    commands = new Hashtable<String, Command>();
  }
  //subroutines-----------------------------------------------------------------
  /* addCommand
    A wrapper for Hashtable.put that wards duplicates.
    Returns false on failure. */
  boolean addCommand(String id, Command c){
    boolean toReturn = false;
    //if key already exists, fail
    if(commands.containsKey(id)){
      System.err.println("ERROR: " + id + " is already a key in commands and " +
                          "was not added.");
      toReturn = false;
    }else{
      commands.put(id, c);
      toReturn = true;
    }
    return toReturn;
  }
  /* generateScript
    Returns a (multi-line) string of the completed script, built from the items
    in 'flow'.
    Must be passed an interpreter that matches one of the interpreters in
      command.codeTable or the program will error.
    Uses StringBuilder to build the script efficiently. */
  String generateScript(String interp){
    StringBuilder script = new StringBuilder(100); //MAGIC NUMBER, arbitrary
    //print interp
    script.append(interp);
    //iterate through every element in 'flow'
    for(int i = 0; i<flow.size(); i++){

    }
    return script.toString();
  }
  //static subroutines----------------------------------------------------------
}
