package structure;
import java.util.ArrayList;
import java.util.Hashtable;

/* Interpreter
Represents a specific interpreter and its associated command blocks' codes.
NYI: Each interp's commands is populated on load from the command library.
The hashtable of an interp pairs name of a specific command block to a Command
instance. The command instance is the basic info of a command, as loaded from
its file. The command can then be duplicated and added to flow. */

public class Interpreter{
    //variables-----------------------------------------------------------------
    String name; //name of the interpreter
    String path; //execution path to the interpreter (probably with #!)
    Hashtable<String, Command> commands; //all available commands; name -> Command
    //constructors--------------------------------------------------------------
    public Interpreter(String n, String p, Hashtable<String, Command> h){
        name = n;
        path = p;
        commands = h;
    }
    //subroutines---------------------------------------------------------------
    //getters/setters
    String getName()            {return name;}
    void   setName(String n)    {name = n;}
    String getPath()            {return path;}
	void   setPath(String p)    {path = p;}

    /* getCommand
    Returns a command from the commands ht, searching by ID. */
    public Command getCommand(String name){
        return commands.get(name);
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
        if(commands.containsKey(name)){
            System.err.println(error); toReturn = false;
        }else{
            commands.put(name, c); toReturn = true;
        }

        return toReturn;
    }

    /* getCommands
    used to return an ArrayList of Command objects to the GUI */
    public ArrayList<Command> getCommands(){
        ArrayList<Command> commandList = new ArrayList<Command>(commands.values());
        return commandList;
    }

    //static subroutines--------------------------------------------------------
    /* generateInterpreters
    Used to populate the ArrayList of interpreter objects (as well as fill their
    fields).
    NOTE: Currently just creates the test interpreter. */
    public static ArrayList<Interpreter> generateInterpreters(){
        //variables
        Hashtable<String, Command> ht = new Hashtable<String, Command>();
        Interpreter bash;
        ArrayList<Interpreter> toReturn = new ArrayList<Interpreter>();

        //create bash Interpreter object
        bash = new Interpreter("bash", "#!/bin/bash", ht);

        //generate test bash command
        String name = "Hello World";
        bash.addCommand(name, new Command(name, "echo \"Hello World\"", bash.getName()));

        //add bash to AL
        toReturn.add(bash);
        return toReturn;
    }
}
