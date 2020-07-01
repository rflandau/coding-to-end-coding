import java.util.ArrayList;
import java.util.Hashtable;

/* Command
  Represents a command block and corresponding output code.
  The syntax will look something like
    echo <flags> <args>
  before the <>'d  are replaced during ScriptStruct.generateScript()*/

public class Command{
  //variables-------------------------------------------------------------------
  // basic info
  private String name, tooltip, syntax;
  private ArrayList<String> args;
  //constructors----------------------------------------------------------------
  public Command(String name, String syntax){
    this.name = name;
    this.syntax = syntax;
  }
  //duplication
  public Command(Command c){
    name = c.getName();
    tooltip = c.getTooltip();
  }
  //subroutines-----------------------------------------------------------------
  //getters/setters
  String getName()           { return name; }
  void   setName(String n)   { name = n; }
  String getTooltip()        { return tooltip; }
  void   setTooltip(String t){ tooltip = t; }

  /* duplicate
    Returns a deep copy of the command.
    Alternative to constructor-based duplication. */
  Command duplicate(){
    //NYI
    return null;
  }
  //static subroutines----------------------------------------------------------
}
