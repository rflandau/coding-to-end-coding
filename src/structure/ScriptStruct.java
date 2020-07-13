package structure;

import java.util.ArrayList;
import java.util.Hashtable;
import java.io.*;

/* ScriptStruct
Represents the main generation class for storing the flowchart and outputting
to the selected out-lang. Intended as the public face of the back-end.
'.export()' is the public-facing method for printing the script. */

public class ScriptStruct{
    //variables-----------------------------------------------------------------
    @SuppressWarnings("FieldCanBeLocal")
    private final String defaultInterp      = "bash";
    private static final String BREAKSEQ    = "---";

    /*'Flow' holds command representations of the GUI flowchart. */
    public ArrayList<Command> flow;
    //'out' holds the path to the desired output file
    String outPath;

    //interpreter fields
    Hashtable<String, Interpreter> interpreterList;
    Interpreter interp;
    //constructors--------------------------------------------------------------
    /* The SS constructor creates flow, sets all fields, and initalizes the
    interpreters */
    // default constructor
    public ScriptStruct(){
        //initalize
        flow = new ArrayList<Command>();
        outPath = "out.txt";
        interpreterList = new Hashtable<String, Interpreter>();
        //interpreterList = generateInterpreters();
        generateInterpreters();
        changeInterpreter(defaultInterp);
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
    /* generateInterpreters
    Used to populate the Hashtable of interpreter objects (as well as fill their
    fields).*/
    void generateInterpreters(){
        String path = "../commands/bash.ctecblock"; //hard-coded atm
        try{
            BufferedReader reader =
            new BufferedReader(new FileReader(new File(path)));
            //parse the file
            parse(reader);
        }catch(FileNotFoundException ex){
            System.out.println("file " + path + " not found.\n");
        }
        return;
    }

    /* parse
    Parses input file and adds interpreters or commands to interpreterList
    as needed */
    private void parse(BufferedReader reader){
        // variables
        String line;

        // reads input file and determines if current entry is a command,
        // an interpreter, or garbage input
        try{
            while((line = reader.readLine()) != null){
                line = line.trim();
                if(line.equals("INTERPRETER"))
                    newInterpreter(reader);
                else if(line.equals("COMMAND")){
                    newCommand(reader);
                }
                else //junk data
                System.err.println("File formatting error" + line);
            }
        }catch(IOException ex){
            System.out.println("ERROR@ScriptStruct.parse()\n" +
            "IO Exception: " + ex );
            return;
        }
    }

    /* newInterpreter
    Parses input file and adds interpreter to interpreters hashtable
    Runs until it hits the break sequence "---". */
    private int newInterpreter(BufferedReader reader) {
        // variables
        String name = "",
        path = "",
        tooltip = "";
        int    returnVal = 0;

        try{
            // this must be declared here because java
            String string;

            while((string = reader.readLine()) != null){
            String[] data = string.trim().split(" ");
                // if delimiter, end while loop
                if(data[0].equals(BREAKSEQ)){
                    break;
                }

                // check length and assign variable values
                if(data.length > 1){
                    if(data[0].equals("NAME"))      name = data[1];
                    else if(data[0].equals("PATH")) path = data[1];
                    else if(data[0].equals("TIP"))  tooltip = data[1];
                    else break; //should this not just warn and continue?

                }
            }

            // make a shiny new interpreter
            interpreterList.put(name, new Interpreter(name, path, tooltip));
        }catch(IOException ex){
            System.out.println("IO Exception");
            returnVal = -1;
        }

        return returnVal;
    }

    /* newCommand
    Parses input file and adds command to commands ArrayList in the appropriate
    interpreter in interpreters */
    private int newCommand(BufferedReader reader) {
        // variables
        ArrayList<String> arguments = new ArrayList<String>(),
        flags = new ArrayList<String>();
        String            name = "",
        interpreter = "",
        command = "",
        tooltip = "";
        int               returnVal = 0;

        try{
            // this must be declared here because java
            String  string;

            while((string = reader.readLine()) != null){
            String[] data = string.trim().split(" ");
                // if delimiter, end while loop
                if(data[0].equals("---")){
                    break;
                }

                // check length and assign variable values
                //TODO change to switch/case
                if(data.length > 1){
                    if(data[0].equals("NAME")){
                        for(int i = 1; i <= data.length-1; i ++){
                            name += data[i];
                            //add a space if not last entry
                            if (i != data.length-1) name += " ";
                        }
                    }else if(data[0].equals("INT")){
                        interpreter = data[1];
                    }else if(data[0].equals("CMD")){
                        command = data[1];
                    }else if(data[0].equals("TIP")){
                        for(int i = 1; i <= data.length-1; i ++){
                            tooltip += data[i];
                            if (i != data.length-1) tooltip += " ";
                        }
                    }else if(data[0].equals("ARG")){
                        for(int i = 1; i < data.length; i ++){
                            arguments.add(i-1, data[i]);
                        }
                    }else if(data[0].equals("FLAG")){
                        for(int i = 1; i < data.length; i ++){
                            flags.add(i-1, data[i]);
                        }
                    }else{
                        break;
                    }
                }
            }

            // make a shiny new command in the designated interpreter
            interpreterList.get(interpreter).addCommand(name,
                new Command(name, command, tooltip, flags, arguments));
        }catch(IOException ex){
            System.out.println("IO Exception");
            returnVal = -1;
        }
        return returnVal;
    }

    //other subroutines
    /* getTemplateCommands
    Returns an ArrayList of all commands in the current interp.*/
    public ArrayList<Command> getTemplateCommands(){
        if(interp != null){
            return new ArrayList<Command>(interp.commands.values());
        }
        return null;
    }
    /* changeInterpreter
    Tries to set the current interp to the one named 'name'.
    Returns false if 'name' could not be found w/in interpreterList. */
    public boolean changeInterpreter(String name){
        boolean found = false;

        if(interpreterList.get(name) != null) {
            found = true;
            interp = interpreterList.get(name);
        }

        if (!found) System.out.println("Could not find interp '" + name +"'");
        return found;//return whether or not it was found
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
    Helper function for export().
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
            System.out.println(c.getName());
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

    /* getCommandNamesInInterp
    Returns all command names in current interp*/
    public String getComTest(){
        return interp.getCommand("move").getName();
    }
    //static subroutines--------------------------------------------------------
}
