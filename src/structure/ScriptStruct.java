package structure;

import java.util.ArrayList;
import java.util.Hashtable;
//import java.lang.StringBuilder;
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

/* ScriptStruct
Represents the main generation class for storing the flowchart and outputting
to the selected out-lang. Intended as the public face of the back-end.
'.export()' is the public-facing method for printing the script. */

public class ScriptStruct{
    //variables-----------------------------------------------------------------
    @SuppressWarnings("FieldCanBeLocal")
    private final String defaultInterp = "bash";

    /*'Flow' holds command representations of the GUI flowchart. */
    public ArrayList<Command> flow;
    //'out' holds the path to the desired output file
    String outPath = "out.txt";
    //interpreter fields
    ArrayList<Interpreter> interpreterList;
    Interpreter interp;
    //constructors--------------------------------------------------------------
    /* The SS constructor creates flow, sets all fields, and initalizes the
    interpreters */
    // default constructor
    public ScriptStruct(){
        flow = new ArrayList<Command>();
        initialize();
    }
    // with specified output path
    public ScriptStruct(String o){
        this(); //chain default constructor
        outPath = o;
    }
    //subroutines---------------------------------------------------------------
    //getters/setters
    public String  getOutPath()             { return outPath; }
    public void    setOutPath(String o)     { outPath = o; }
    public int     getFlowSize()            { return flow.size(); }
    public Command getCommand(int i)        { return flow.get(i); }

    //interpreter subroutines
    /* initialize
    Generates the interpreter list and sets the current interpreter to the
    default interpreter (as specified in the field above). */
    public void initialize(){
        interpreterList = generateInterpreters();
        changeInterpreter(defaultInterp);
        return;
    }

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
        bash.addCommand(name, new Command(name, "echo \"Hello World\""));

        //add bash to AL
        toReturn.add(bash);
        return toReturn;
    }

    /* changeInterpreter
    Tries to set the current interp to the one at index 'i'.
    Returns false and does nothing if 'i' is out of bounds. */
    public boolean changeInterpreter(int i){
        boolean toReturn;

        if (0 <= i && i < interpreterList.size()){ //i is within bounds
            interp = interpreterList.get(i);
            toReturn = true;
        } else {
            System.err.println("Could not find interp at entry " + i);
            toReturn = false;
        }
        return toReturn;
    }
    /* changeInterpreter
    Tries to set the current interp to the one named 'name'.
    Returns false if 'name' could not be found w/in interpreterList. */
    public boolean changeInterpreter(String name){
        boolean found = false;

        //search interpList until interp found or we fall off
        for(int i = 0; i < interpreterList.size() && !found; i++){
            //check interp name against given name
            if (name.equals(interpreterList.get(i).getName())){
                interp = interpreterList.get(i); //set interp
                found = true;
            }
        }
        if (!found) System.out.println("Could not find interp '" + name +"'");
        return found;//return whether or not it was found
    }

    //other subroutines
    /* getTemplateCommands
    Returns an ArrayList of all commands in the current interp.*/
    public ArrayList<Command> getTemplateCommands(){
        return new ArrayList<Command>(interp.commands.values());
    }


    /* addCommandToFlow
    Takes an id for a command, duplicates it from the current interpreter,
    and adds the new command to flow.
    Alternative to addCommandToFlow() so Commands do not have to be passed.
    Does nothing if id is not found in interpreter hash. */
    public void addCommandToFlow(int i, String id){
        Command fetched;

        if (0 <= i && i <= getFlowSize()){ //validate index
            if((fetched = interp.getCommand(id)) != null) //check the id exists
            flow.add(i, new Command(fetched));
            else
            System.err.println("ERROR@ScriptStruct.addCommandFromID()\n" +
            "---Command with ID " + id + "could not be found.");
        }else System.err.println("ERROR@ScriptStruct.addCommandToFlow()\n" +
        "---Given index (" + i + ") is out of range.");
        return;
    }

    /* removeCommandFromFlow
    Removes the command at given index from flow.
    If index is out of range, prints error to terminal and does nothing. */
    public void removeCommandFromFlow(int i){
        if(i < getFlowSize() && i >= 0){
            System.out.println(flow.get(i).getName()+" removed from flow");
            flow.remove(i);
        }else{ //index is out of range and will err
            System.err.println("ERROR@ScriptStruct.removeCommandFromFlow()\n" +
            "---Given index (" + i + ") is out of range.");
        }
        return;
    }

    /* writeScript
    hHlper function for export().
    Writes a (multi-line) string of the completed script, built from the
    items in 'flow'.
    Uses Global.curInterp to figure out langauge, so make sure it is set
    properly before calling.
    Throws IOException if file to write to was not checked previously. Call
    createOutFile() for proper error-handling. */
    private void writeScript(BufferedWriter br, Interpreter interp)
    throws IOException{

        //add interpreter path to top of script, plus newline
        try{
            br.write(interp.getPath() + "\n\n");
        }catch(IOException e){
            System.out.println("Caught exception " + e);
            return;
        }
        System.out.println(interp.getPath());

        //iterate through every element in 'flow'
        for(int i = 0; i<flow.size(); i++){
            Command c = flow.get(i);
            br.write(c.getSyntax() + "\n");
        }

        //ensure the script is end-capped by a newline
        br.write("\n");
        //close writer
        br.close();
        return;
    }

    /* createOutFile
    Helper function for export().
    Tests the output file and returns a File on success.*/
    private File createOutFile(){
        File toReturn;
        try{
            toReturn = new File(outPath);
            if(toReturn.createNewFile())
            System.out.println(outPath + " created.");
            else System.out.println(outPath+" already exists. Overwritting...");
        } catch (NullPointerException | SecurityException | IOException ex){
            System.err.println("ERROR@createOutFile()\n" + "---"+ex.toString());
            toReturn = null;
            /* this error-handling could and should be made more robust as we
            flesh out the program and give the user more options (like
            naming the script). */
        }
        return toReturn;
    }

    /* export
    Calls writeScript to return the script before printing it to 'out'.
    Will return -1 if file cannot be created or opened. */
    public boolean export() throws IOException{
        ///variables
        File out = createOutFile();
        BufferedWriter br;
        boolean toReturn = true;

        //check for null
        if(out != null){
            //assign BufferedWriter
            br = new BufferedWriter(new FileWriter(out));
            //call helper function
            writeScript(br, interp);
            br.close();
        } else toReturn = false; //return false on null

        return toReturn;
    }
    //static subroutines--------------------------------------------------------
}
