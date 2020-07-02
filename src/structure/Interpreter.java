package structure;
import java.util.ArrayList;
import java.util.Hashtable;

/* Interpreter
Represents a specific interpreter and its associated command blocks' codes.
NYI: Each interp's commands is populated on load from the command library.
The hashtable of an interp pairs id of a specific command block to a Command
instance. The command instance is the basic info of a command, as loaded from
its file. The command can then be duplicated and added to flow. */

class Interpreter{
    //variables-----------------------------------------------------------------
    String name; //name of the interpreter
    String path; //execution path to the interpreter (probably with #!)
    Hashtable<String, Command> commands; //all available commands; id -> Command
    //constructors--------------------------------------------------------------
    public Interpreter(String n, String p, Hashtable<String, Command> h){
        name = n;
        path = p;
        commands = h;
    }
    //subroutines---------------------------------------------------------------
	//getters/setters
	public String getName()			{return name;}
		   void   setName(String n)	{name = n;}
	public String getPath()			{return path;}
		   void   setPath(String p)	{path = p;}

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
    /* generateInterpreters
    Used to populate the ArrayList of interpreter objects (as well as fill their
    fields).
    NOTE: Currently just creates the test interpreter. */
    static ArrayList<Interpreter> generateInterpreters(){
        //variables
        Hashtable<String, Command> ht = new Hashtable<String, Command>();
        Interpreter bash;
        ArrayList<Interpreter> toReturn = new ArrayList<Interpreter>();

        //generate test bash ht
        ht.put("helloworld",
        	new Command("Hello World", "echo \" Hello World\""));

        //create bash Interpreter object
        bash = new Interpreter("bash", "#!/bin/bash", ht);

        //add bash to AL
        toReturn.add(bash);
        return toReturn;
    }
}
