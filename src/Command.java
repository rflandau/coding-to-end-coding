import java.util.Hashtable;

/* Command
Represents a command block and corresponding output code.

NOTE: each command block is unlikely to have more than one or two interpreters
  and corresponding code. A hash for two items may be overkill.*/

public class Command{
  //variables-------------------------------------------------------------------
  // basic info
  String id, name, tooltip;
  int inputs; //may  need to be tweaked after we figure out how to take text
  // hashtable stores K -> V as interpreter -> code
  private Hashtable<String, String> codeTable = new Hashtable<String, String>();
  //constructors----------------------------------------------------------------
  public Command(String id, String name){
    this.id   = id;
    this.name = name;
  }
  //subroutines-----------------------------------------------------------------
  /* addLang
    Adds an entry to the hashtable (interp -> code) */
  void addLang(String interp, String code){codeTable.add(interp, code); return;}
  //static subroutines----------------------------------------------------------
}
