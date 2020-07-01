package prefabs;
import java.util.Hashtable;

/* Command
Represents a command block and corresponding output code.

NOTE: each command block is unlikely to have more than one or two interpreters
  and corresponding code. A hash for two items may be overkill, espcially
  because deep-copying a hashtable is expensive. */

/*	Moved Command into prefabs so it can be handled as a type by CommandBlock, Command's
 	visual representation on the screen (classes from the default package can't be imported)
 	-Thomas	*/

public class Command{
  //variables-------------------------------------------------------------------
  // basic info
  private String id, name, tooltip;
  private int inputCount; //may need to be tweaked after we figure out how to take text
  // hashtable stores K -> V as interpreter -> code
  // this could be broken off into an independent hashtable in main
  Hashtable<String, String> codeTable = new Hashtable<String, String>();
  //constructors----------------------------------------------------------------
  public Command(String id, String name){
    this.id   = id;
    this.name = name;
  }
  //duplication
  public Command(Command c){
    id   = c.getId();
    name = c.getName();
    tooltip = c.getTooltip();
    inputCount = c.getInputCount();
    //codeTable = c.codeTable;
  }
  //subroutines-----------------------------------------------------------------
  //getters/setters
  String getId()             { return id; }
  void   setId(String i)     { id = i; }
  String getName()           { return name; }
  void   setName(String n)   { name = n; }
  String getTooltip()        { return tooltip; }
  void   setTooltip(String t){ tooltip = t; }
  int    getInputCount()     { return inputCount; }
  void   setTooltip(int i)   { inputCount = i; }

  /* addLang
    Adds an entry to the hashtable (interp -> code) */
  //this was the original line here, but add isn't a method of hashtables? I guess?
  //	I'm assuming that's what add is doing here, adding this key/value pair
  //void addLang(String interp, String code){codeTable.add(interp, code); return;}
  void addLang(String interp, String code){codeTable.put(interp, code); return;}
  //static subroutines----------------------------------------------------------
}