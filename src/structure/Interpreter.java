import java.util.Hashtable;

/* Interpreter
  Represents a specific interpreter and its associated command blocks' codes.
  NYI: Each interp's commands is populated on load from the command library.
  The hashtable of an interp pairs id of a specific command block to a Command
  instance. The command instance is the basic info of a command, as loaded from
  its file. The command can then be duplicated and added to flow. */

class Interpreter{
  //variables-------------------------------------------------------------------
  String name; //name of the interpreter
  String path; //execution path to the interpreter (probably with #!)
  Hashtable<String, Command> commands; //all available commands; id -> Command
  //constructors----------------------------------------------------------------
  public Interpreter(String n, String p, Hashtable<String, Command> h){
    name = n;
    path = p;
    commands = h;
  }
  //subroutines-----------------------------------------------------------------
  /* addCommand
    A wrapper for Hashtable.put() that wards duplicates.
    Returns false on failure. */
  boolean addCommand(String id, Command c){
    boolean toReturn = false;
    String error = "ERROR: " + id + " is already a key in commands and " +
                        "was not added.";
    //if key already exists, fail
    if(commands.containsKey(id)){
      System.err.println(error); toReturn = false;
    }else{
      commands.put(id, c); toReturn = true;
    }

    return toReturn;
  }
  //static subroutines----------------------------------------------------------
}
