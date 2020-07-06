package structure;

import java.util.ArrayList;
import java.util.Hashtable;
import java.lang.StringBuilder;
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

/* ScriptStruct
  Represents the main generation class for storing the flowchart and outputting
  to the selected out-lang.
  '.export()' is the public-facing method for printing the script. */

public class ScriptStruct{
    //variables-----------------------------------------------------------------
    /*'Flow' holds command representations of the GUI flowchart. */
    public ArrayList<Command> flow;
    //'out' holds the path to the desired output file
    String outPath;
    //constructors--------------------------------------------------------------
    public ScriptStruct(){
        flow = new ArrayList<Command>();
        outPath = "out.txt";
    }
    // with specified output path
    public ScriptStruct(String o){
        flow = new ArrayList<Command>();
        outPath = o;
    }
    //subroutines---------------------------------------------------------------
    //getters/setters
    public String  getOutPath()             { return outPath; }
    public void    setOutPath(String o)     { outPath = o; }
    public int     getFlowSize()            { return flow.size(); }
    public Command getCommand(int i)        { return flow.get(i); }

    /* addCommandToFlow
       Takes a command and inserts it into flow at the specified index.
       If index is out of range, prints error to terminal and does nothing.
       DEPRECIATED. */
//     public void addCommandToFlow(int i, Command cmd){
//         if (i <= getFlowSize() && i >= 0){
//             System.out.println(cmd.getName()+" added to flow");
//             flow.add(i, cmd);
//         }else{
//             System.err.println("ERROR@ScriptStruct.addCommandToFlow()\n" +
//                 "---Given index (" + i + ") is out of range.");
//         }
//         return;
//     }

    /* addCommandToFlow
        Takes an id for a command, duplicates it from the current interpreter,
        and adds the new command to flow.
        Alternative to addCommandToFlow() so Commands do not have to be passed.
        Does nothing if id is not found in interpreter hash. */
    public void addCommandToFlow(int i, String id, Interpreter interp){
        Command fetched, cmd;

        if (i <= getFlowSize() && i >= 0){
            if((fetched = interp.getCommand(id)) != null){
                cmd = new Command(fetched);//duplicate command from fetched com
                flow.add(i, cmd);
            }
            else System.err.println("ERROR@ScriptStruct.addCommandFromID()\n" +
                "---Command with ID " + id + "could not be found.");
        }else System.err.println("ERROR@ScriptStruct.addCommandToFlow()\n" +
                "---Given index (" + i + ") is out of range.");
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
        Helper function for export.
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
       Calls generateScript to return the script before printing it to 'out'.
       Will return -1 if file cannot be created or opened. */
    public boolean export(Interpreter interp) throws IOException{
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
