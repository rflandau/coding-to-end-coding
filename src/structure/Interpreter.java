package structure;
import java.util.Hashtable;

/* Interpreter
Represents a specific interpreter and its associated command blocks' codes.
NYI: Each interp's commands is populated on load from the command library.
The hashtable of an interp pairs name of a specific command block to a Command
instance. The command instance is the basic info of a command, as loaded from
its file. The command can then be duplicated and added to flow. */

public class Interpreter{
    //variables-----------------------------------------------------------------
    String name,                            // Interpreter name
           path,                            // path to the interpreter shell
           tooltip;                         // tool tip for tollover events
    Hashtable<String, Command> commands;    // all available commands
    
    //constructors--------------------------------------------------------------
    public Interpreter(String n, String p){
        name = n;
        path = p;
        commands = new Hashtable<String, Command>();
    }

    public Interpreter(String n, String p, String t){
        name = n;
        path = p;
        tooltip = t;
        commands = new Hashtable<String, Command>();
    }

    //subroutines---------------------------------------------------------------
    //getters/setters
    String getName()            {return name;}
    void   setName(String n)    {name = n;}
    String getPath()            {return path;}
    void   setPath(String p)    {path = p;}

    /* getCommand
    Returns a command from the commands ht, searching by ID. */
    public Command getCommand(String cid){
        return commands.get(cid);
    }

    /* addCommand
    A wrapper for Hashtable.put() that wards duplicates.
    Protected because commands should only ever be added by an internal,
    initialize subroutine.
    Returns false on failure. */
    boolean addCommand(String name, Command c){
        boolean toReturn = false;
        String error = "ERROR@Interpreter.addCommand()\n" +
            "---" + name + " is already a key in commands and was not added.";
        //if key already exists, fail
        if(commands.containsKey(name))
            System.err.println(error);
        else{
            commands.put(name, c); toReturn = true;
        }
        return toReturn;
    }

    /* toString
    */
    public String toString(){
        return name + " " + path;
    }
    //static subroutines--------------------------------------------------------
}
