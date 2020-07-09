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
    private String              name,
                                syntax,
                                interpreter,
                                tooltip;
    private ArrayList<String>   flags, 
                                arguments;
    
    //constructors--------------------------------------------------------------
    // default constructor (used when members need to be assigned manually)
    public Command(){
        flags = new ArrayList<String>();
        arguments = new ArrayList<String>();
    }
    // a good constructor (used for start and end blocks)
    public Command(String name){
        this.name = name;
    }
    // a better constructor
    public Command(String name, String syntax, String interpreter){
        this.name = name;
        this.syntax = syntax.trim(); //trim whitespace from syntax
        //^trim may cause issues in Python? A problem for later.
        this.interpreter = interpreter.trim();
        flags = new ArrayList<String>();
        arguments = new ArrayList<String>();
    }
    // the ultimate constructor
    public Command(String name, String syntax, String interpreter, String tooltip,
                   ArrayList<String> flags, ArrayList<String> arguments){
        this.name = name;
        this.syntax = syntax;
        this.interpreter = interpreter;
        this.tooltip = tooltip;
        for(int i = 0; i < flags.size(); i ++){
            flags.add(i, flags.get(i));
        }
        arguments = new ArrayList<String>();
        for(int i = 0; i < arguments.size(); i ++){
            arguments.add(i, arguments.get(i));
        }
    }
    //duplication
    public Command(Command c){
        name = c.getName();
        syntax = c.getSyntax();
        interpreter = c.getInterpreter();
        tooltip = c.getTooltip();
        flags = new ArrayList<String>();
        ArrayList<String> f = c.getFlags();
        for(int i = 0; i < f.size(); i ++){
            flags.add(i, f.get(i));
        }
        arguments = new ArrayList<String>();
        ArrayList<String> a = c.getArguments();
        for(int i = 0; i < a.size(); i ++){
            arguments.add(i, a.get(i));
        }
    }
    
    //subroutines---------------------------------------------------------------
    //getters/setters
    public String getName()                     { return name; }
    public void   setName(String n)             { name = n; }
    public String getSyntax()                   { return syntax; }
    public void   setSyntax(String s)           { syntax = s; }
    public String getInterpreter()              { return interpreter; }
    public void   setInterpreter(String i)      { interpreter = i; }
    public String getTooltip()                  { return tooltip; }
    public void   setTooltip(String t)          { tooltip = t; }
    public ArrayList<String> getFlags()         { return flags; }
    public void   setFlags(ArrayList<String> f) {
        for(int i = 0; i < f.size(); i ++){
            flags.add(i, f.get(i));
        }
    }
    public ArrayList<String> getArguments()     { return arguments; }
    public void   setArguments(ArrayList<String> a) {
        for(int i = 0; i < a.size(); i ++){
            arguments.add(i, a.get(i));
        }
    }

    //static subroutines--------------------------------------------------------
}
