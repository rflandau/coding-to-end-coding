package structure;

import java.util.ArrayList;
import java.util.Hashtable;

/* Command
  Represents a command block and corresponding output code.
  The syntax will look something like
    echo <flags> <args>
  before the <>'d  are replaced during ScriptStruct.generateScript() */

public class Command{
    //variables-----------------------------------------------------------------
    // basic info
    private String name, tooltip, syntax, id;
    //private ArrayList<String> args;
    //constructors--------------------------------------------------------------
    public Command(String name, String syntax){
        this.name = name;
        this.id = name;
        this.syntax = syntax.trim(); //trim whitespace from syntax
        //^trim may cause issues in Python? A problem for later.
    }
    //duplication
    public Command(Command c){
        name = c.getName();
        tooltip = c.getTooltip();
        syntax = c.getSyntax();
        id = c.getId();
        //copy args individually, when implemented!
    }
    //subroutines---------------------------------------------------------------
    //getters/setters
    public String getName()               { return name; }
    public void   setName(String n)       { name = n; }
    public String getTooltip()            { return tooltip; }
    public void   setTooltip(String t)    { tooltip = t; }
    public String getSyntax()             { return syntax; }
    public void   setId(String i)         { id = i; }
    public String getId()                 { return id; }
    //public ArrayList<String> getArgs()    { return args; }

    //static subroutines--------------------------------------------------------
}
