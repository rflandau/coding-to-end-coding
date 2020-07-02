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
	String outPath = "out.sh";
    //constructors--------------------------------------------------------------
    public ScriptStruct(){
        flow = new ArrayList<Command>();
    }
    //subroutines---------------------------------------------------------------
	//getters/setters
	public String getOutPath()			{ return outPath; }
	public void   setOutPath(String o)	{ outPath = o; }

    /* addCommandToFlow
       Duplicates the command from the hashtable and plugs it into flow.
	   NOTE: If this is to check the interp hashtable, it should be given an id.
	   If it is to just add a command, a command param is fine.
	   TODO.
       NYI: Then adds user adjustments (flags/text input) to the command. */
    public void addCommandToFlow(Command cmd){
        System.out.println("Command: "+cmd.getName()+" added to flow");
        flow.add(flow.size(), cmd);
    }
    public void removeCommandFromFlow(Command cmd){
        System.out.println("Command: "+cmd.getName()+" removed from flow");
        flow.remove(flow.size()-1);
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
        br.write(interp.getPath() + "\n\n");
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
			else System.out.println(outPath + " already exists. Overwritting...");
		} catch (NullPointerException | SecurityException | IOException ex){
			System.err.println("ERROR: " + ex.toString());
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
		File out = createOutFile(); /* all error-handling should be handled by
			createOutFile() */
		BufferedWriter br;
		boolean toReturn = false;

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
