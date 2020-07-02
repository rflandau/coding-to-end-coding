package structure;

import java.util.ArrayList;
import java.util.Hashtable;
import java.lang.StringBuilder;

/* ScriptStruct
  Represents the main generation class for storing the flowchart and outputting
  to the selected out-lang. */

public class ScriptStruct{
    //variables-------------------------------------------------------------------
    /*'Flow' holds command representations of the GUI flowchart. */
    ArrayList<Command> flow;
    //constructors----------------------------------------------------------------
    public ScriptStruct(){
	flow = new ArrayList<Command>();
    }
    //subroutines-----------------------------------------------------------------
    /* addCommandToFlow
       Duplicates the command from the hashtable and plugs it into flow.
       NYI: Then adds user adjustments (flags/text input) to the command.
    */
    public void addCommandToFlow(Command cmd){
	System.out.println("Command: "+cmd.getName()+" added to flow");
	//NYI
    }
    /* generateScript
       Returns a (multi-line) string of the completed script, built from the items
       in 'flow'.
       Uses Global.curInterp to figure out langauge, so make sure it is set
       properly before calling.
       Uses StringBuilder to build the script efficiently.
       NYI: Uses helper function */
    String generateScript(){
	StringBuilder script = new StringBuilder(100); //MAGIC NUMBER, arbitrary

	//add interpreter path to top of script, plus newline
	script.append(Globals.curInterp + "\n\n");

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
