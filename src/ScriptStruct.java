import java.util.ArrayList;
import java.util.Hashtable;
import java.lang.StringBuilder;
/* ScriptStruct
  Represents the main generation class for storing the flowchart and outputting
  to the selected out-lang. */

import prefabs.Command;

public class ScriptStruct{
  //variables-------------------------------------------------------------------
  /*'Flow' holds command representations of the GUI flowchart. */
  ArrayList<Command> flow;

  /*Set up the 'commands' hashtable.
    'commands' holds one instance of each command, set up at program start.
    It should be assembed with addCommandBase to prevent duplicates. */
  Hashtable<String, Command> commandBases;
  //constructors----------------------------------------------------------------
  public ScriptStruct(){
    flow = new ArrayList<Command>();
    commandBases = new Hashtable<String, Command>();
  }
  //subroutines-----------------------------------------------------------------
  /* addCommand
    A wrapper for Hashtable.put() that wards duplicates.
    Returns false on failure. */
  boolean addCommandBase(String id, Command c){
    boolean toReturn = false;
    String error = "ERROR: " + id + " is already a key in commands and " +
                        "was not added.";
    //if key already exists, fail
    if(commandBases.containsKey(id)){
      System.err.println(error); toReturn = false;
    }else{
      commandBases.put(id, c); toReturn = true;
    }

    return toReturn;
  }
  /* addCommandToFlow
    Duplicates the command from the hashtable and plugs it into flow.
    NYI: Then adds user adjustments (flags/text input) to the command.
  */
  void addCommandToFlow(){
    //NYI
  }
  /* generateScript
    Returns a (multi-line) string of the completed script, built from the items
    in 'flow'.
    Must be passed an interpreter that matches one of the interpreters in
      command.codeTable or the program will error.
    Uses StringBuilder to build the script efficiently.
    NYI: Uses helper function */
  String generateScript(String interp){
    StringBuilder script = new StringBuilder(100); //MAGIC NUMBER, arbitrary
    //add interpreter to top of script, plus newline
    script.append(interp + "\n\n");
    //iterate through every element in 'flow'
    for(int i = 0; i<flow.size(); i++){
      Command c = flow.get(i);

    }
    //ensure the script is end-capped by a newline
    script.append("\n");
    return script.toString();
  }
  //static subroutines----------------------------------------------------------
}
