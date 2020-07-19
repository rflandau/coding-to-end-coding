package structure;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Collection;
import java.io.*;
import java.lang.StringBuilder;

/*
    ScriptStruct
    Represents the main generation class for storing the flowchart and
    outputting to the selected out-lang. Intended as the public face of the
    back-end. '.export()' is the public-facing method for printing the script.
*/
public class ScriptStruct{
    //variables----------------------------------------------------------------
    @SuppressWarnings("FieldCanBeLocal")
    private final String        DEFAULTINTERP = "bash"; // default interpreter
    private static final String BREAKSEQ = "---";       // ctecblock delimiter
    public ArrayList<Command>   flow;                   // flowchart commands
    String                      outPath;                // path to output file
    Hashtable<String, Interpreter> interpreterList;     // all Interpreters
    Interpreter                 interp;                 // current Interpreter

    //constructors-------------------------------------------------------------
    /*
        default constructor
        creates flow, sets all fields, and initalizes the interpreters
    */
    public ScriptStruct(){
        flow = new ArrayList<Command>();
        outPath = "out.txt";
        interpreterList = new Hashtable<String, Interpreter>();
        //interpreterList = generateInterpreters();
        generateInterpreters();
        changeInterpreter(DEFAULTINTERP);
    }

    /*
        constructor with specified output path
        creates flow, sets all fields, and initalizes the interpreters
    */
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

    //build the interpreters
    /*
        generateInterpreters()
        Used to populate the Hashtable of interpreter objects (as well as fill
        their fields).
    */
    void generateInterpreters(){
        String path = "../commands/bash.ctecblock";     // default input path

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

    //parse subroutines
    /*
        parse()
        Parses input file and adds interpreters or commands to interpreterList
        as needed
    */
    private void parse(BufferedReader reader){
        String line;    // current line read by BufferedReader

        // reads input file and determines if current entry is a command,
        // an interpreter, or garbage input
        try{
            while((line = reader.readLine()) != null){
                line = line.trim();
                if(line.equals("INTERPRETER")){newInterpreter(reader);}
                else if(line.equals("COMMAND")){newCommand(reader);}
                else{System.err.println("File formatting error" + line);}
            }
        }catch(IOException ex){
            System.out.println("ERROR@ScriptStruct.parse()\n" +
            "IO Exception: " + ex );
        }
        return;
    }

    /*
        newInterpreter()
        Parses input file and adds interpreter to interpreters hashtable
        Runs until it hits the break sequence "---".
    */
    private int newInterpreter(BufferedReader reader) {
        String  name = "",      // Interpreter name
                path = "",      // path to Interpreter shell
                tooltip = "";   // rollover tooltip
                //[] data;      //original declaration, but caused errors
        String[]data;           // words from a line
        int     returnVal = 0;  // return value

        try{
            String string;      // current string read by BufferedReader

            while((string = reader.readLine()) != null){
            data = string.trim().split(" ");
                // if delimiter, end while loop
                if(data[0].equals(BREAKSEQ)){break;}

                // check length and assign variable values
                if(data.length > 1){
                    if(data[0].equals("NAME"))      {name = data[1];}
                    else if(data[0].equals("PATH")) {path = data[1];}
                    else if(data[0].equals("TIP"))  {tooltip = data[1];}
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

    /*
        newCommand()
        Parses input file and adds command to commands ArrayList in the
        appropriate interpreter in interpreters
    */
    private int newCommand(BufferedReader reader) {
        ArrayList<String>  args = new ArrayList<String>(),  // temp arguments
                           flags = new ArrayList<String>(); // temp flags
        String             name = "",                       // temp name
                           interpreter = "",                // temp interpreter
                           command = "",                    // temp command
                           tooltip = "";                    // temp tooltip
        String             []data;                          // words from a line
        int                returnVal = 0;                   // return value

        try{
            String string;      // current string read by BufferedReader
            while((string = reader.readLine()) != null){
                data = string.trim().split(" ");
                //check for BREAKSEQ
                //Tried to integrate it into switch/case, but it never fired?
                if (data[0].equals(BREAKSEQ)) break;
                else{
                    //check for empty line
                    if(data.length > 1){
                        //identify line type by precursor
                        switch(data[0]){
                            case "NAME":
                                for(int i = 1; i <= data.length-1; i++){
                                    name += data[i];
                                    //add a space if not last entry
                                    if (i != data.length-1) name += " ";
                                }
                                break;
                            case "INT": interpreter = data[1]; break;
                            case "CMD": command = data[1]; break;
                            case "TIP":
                                for(int i = 1; i <= data.length-1; i++){
                                    tooltip += data[i];
                                    if (i != data.length-1) tooltip += " ";
                                }
                                break;
                            case "ARG":
                                for(int i = 1; i < data.length; i++)
                                    args.add(i-1, data[i]);
                                break;
                            case "FLAG":
                                for(int i = 1; i < data.length; i ++)
                                    flags.add(i-1, data[i]);
                                break;
                            default:
                                System.out.println("Unknown line. Ignored.");
                        }
                    }
                }
            }
            // make a shiny new command in the designated interpreter
            interpreterList.get(interpreter).addCommand(name,
                new Command(name, command, tooltip, flags, args));
        }catch(IOException ex){
            System.out.println("IO Exception");
            returnVal = -1;
        }
        return returnVal;
    }

    //other subroutines
    /*
        getTemplateCommands()
        Returns an ArrayList of all commands in the current interp
    */
    public ArrayList<Command> getTemplateCommands(){
        ArrayList<Command> returnVal = null;

        if(interp != null){
            returnVal = new ArrayList<Command>(interp.commands.values());
        }
        return returnVal;
    }

    /*
        changeInterpreter()
        Tries to set the current interp to the one named 'name'.
        Returns false if 'name' could not be found w/in interpreterList.
    */
    public boolean changeInterpreter(String name){
        boolean found = false;  // return value

        if(interpreterList.get(name) != null) {
            found = true;
            interp = interpreterList.get(name);
        }

        if (!found) System.out.println("Could not find interp '" + name +"'");
        return found;//return whether or not it was found
    }

    //interact with flow
    /*
        addCommandToFlow()
        Takes an id for a command, duplicates it from the current interpreter,
        and adds the new command to flow.
        Does nothing if id is not found in interpreter hash.
    */
    public void addCommandToFlow(int i, String id){
        Command fetched;    // template Command fetched from Interpreter

        //validate index
        if (0 <= i && i <= getFlowSize()){
            //check the id exists
            if((fetched = interp.getCommand(id)) != null){
                flow.add(i, new Command(fetched));
            }else{
                System.err.println("ERROR@ScriptStruct.addCommandFromID()\n" +
                "---Command with ID " + id + "could not be found.");
            }
        }else System.err.println("ERROR@ScriptStruct.addCommandToFlow()\n" +
        "---Given index (" + i + ") is out of range.");
        return;
    }

    /*
        removeCommandFromFlow()
        Removes the command at given index from flow.
        If index is out of range, prints error to terminal and does nothing.
    */
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
    
    /*
        add()
        TODO: finish these comments
    */
    public void add(int index, Command command){
        
        // validate index
        if(0 <= index && index <= getFlowSize()){
//             System.out.println("removing " command.getName() + " at " + index);
        }
    
    
    
//         System.out.println(command.getName());
    }
    
    /*
        setCommandSyntax()
        TODO: finish these comments
    */
    public void setCommandSyntax(int i, String arg){
        String[] nArgs; //new args
        nArgs = arg.trim().split(" ");

        flow.get(i).setArguments(nArgs);
    }

    //export____________________________________________________________________
    /*
        writeScript()
        Helper function for export().
        Writes a (multi-line) string of the completed script, built from the
        items in 'flow'.
        Uses Global.curInterp to figure out langauge, so make sure it is set
        properly before calling.
        Throws IOException if file to write to was not checked previously. Call
        createOutFile() for proper error-handling.
    */
    private void writeScript(BufferedWriter br, Interpreter interp)
        throws IOException{
        boolean error = false;

        //add interpreter path to top of script, plus newline
        try{
            br.write(interp.getPath() + "\n\n");
        }catch(IOException e){
            System.out.println("Caught exception " + e);
            error = true;
        }

        if(!error){
            System.out.println(interp.getPath());

            //iterate through every element in 'flow'
            for(int i = 0; i<flow.size(); i++){
                Command c = flow.get(i);

                br.write(c.getSyntax());
                for(String arg : c.getArguments()) br.write(" " + arg);
                //don't forget the newline!
                br.write("\n");
            }

            //ensure the script is end-capped by a newline
            br.write("\n");
            //close writer
            br.close();
        }
        return;
    }

    /*
        createOutFile()
        Helper function for export().
        Tests the output file and returns a File on success.
    */
    private File createOutFile(){
        File toReturn;  // File object to be returned

        try{
            toReturn = new File(outPath);
            if(toReturn.createNewFile()){
                System.out.println(outPath + " created. Writting...");
            }else System.out.println(outPath+" already exists. Overwritting...");
        } catch (NullPointerException | SecurityException | IOException ex){
            System.err.println("ERROR@createOutFile()\n" + "---"+ex.toString());
            toReturn = null;
            /* this error-handling could and should be made more robust as we
            flesh out the program and give the user more options (like
            naming the script). */
        }
        return toReturn;
    }

    /*
        export()
        Calls writeScript to return the script before printing it to 'out'.
        Will return -1 if file cannot be created or opened.
    */
    public boolean export() throws IOException{
        File            out = createOutFile();      // for script output
        BufferedWriter  reader;                     // creates output file
        boolean         toReturn = true;            // return value

        //check for null
        if(out != null){
            //assign BufferedWriter
            reader = new BufferedWriter(new FileWriter(out));
            //call helper function
            writeScript(reader, interp);
            reader.close();
            System.out.println("Finished!");
        } else toReturn = false;

        return toReturn;
    }

    //other_____________________________________________________________________
    /*
        toString()
        Prints the ScriptStruct in full, calling toStrings for commands and
        interpreters.
        Uses StringBuilder for efficency.
    */
    public String toString(){
        StringBuilder returnVal = new StringBuilder(100);   // arbitrary value
        ArrayList<Interpreter> interpArr = new
            ArrayList<Interpreter>(interpreterList.values()); // all interps
        ArrayList<Command> comArr;                          // all commands

        //add name, path commands from all interpreters
        for(Interpreter in : interpArr){
            returnVal.append(in.toString() + "\n");
            comArr = new ArrayList<Command>(in.commands.values());
            for(Command c : comArr)
                returnVal.append(c + "\n");
        }
        //print flow status
        returnVal.append("Flow status:\n");
        for(Command c : flow){
            returnVal.append("---" + c.getName() + "\n");
        }
        return returnVal.toString();

    }
    //static subroutines--------------------------------------------------------
}
