package structure;

import java.util.ArrayList;

/*  Command
    Represents a command block and corresponding output code.
    The syntax will look something like
    echo <flags> <args>
    before the <>'d  are replaced during ScriptStruct.generateScript()
*/
public class Command{
    //variables-----------------------------------------------------------------
    private String              name,       // command name
                                syntax,     // literal command syntax
                                tooltip;    // rollover tooltip
    private ArrayList<String>   flags,      // command options
                                arguments;  // command arguments

    //constructors--------------------------------------------------------------
    /*
        default constructor (used when members need to be assigned manually)
    */
    public Command(){
        flags = new ArrayList<String>();
        arguments = new ArrayList<String>();
    }

    /*
        a good constructor (used for start and end blocks)
    */
    public Command(String name){
        this.name = name;
    }

    /*
        a better constructor
    */
    public Command(String name, String syntax){
        this(name);
        this.syntax = syntax.trim(); //trim whitespace from syntax
        //^trim may cause issues in Python? A problem for later.
        flags = new ArrayList<String>();
        arguments = new ArrayList<String>();
    }

    /*
        the ultimate constructor
    */
    public Command(String name,
                   String syntax,
                   String tooltip,
                   ArrayList<String> flags,
                   ArrayList<String> arguments){
        this.name = name;
        this.syntax = syntax;
        this.tooltip = tooltip;

        this.flags = new ArrayList<String>();
        int flagSize = flags.size();
        for(int i = 0; i < flagSize; i ++){
            this.flags.add(i, flags.get(i));
        }

        this.arguments = new ArrayList<String>();
        int argumentSize = arguments.size();
        for(int i = 0; i < argumentSize; i ++){
            this.arguments.add(i, arguments.get(i));
        }
    }
    
    /*
        duplication constructor
    */
    public Command(Command c){
        name = c.getName();
        syntax = c.getSyntax();
        tooltip = c.getTooltip();
        flags = new ArrayList<String>();
        ArrayList<String> f = c.getFlags();
        for(int i = 0; f != null && i < f.size(); i ++){
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
    public String getTooltip()                  { return tooltip; }
    public void   setTooltip(String t)          { tooltip = t; }
    public ArrayList<String> getFlags()         { return flags; }
    public void   setFlags(ArrayList<String> f) {
        for(int i = 0; i < f.size(); i ++){
            flags.add(i, f.get(i));
        }
        return;
    }
    public ArrayList<String> getArguments()     { return arguments; }
    public void   setArguments(ArrayList<String> a) {
        for(int i = 0; i < a.size(); i ++){
            arguments.add(i, a.get(i));
        }
        return;
    }
    public String toString(){
        return name + " -> " + syntax;
    }
    //static subroutines--------------------------------------------------------
}
