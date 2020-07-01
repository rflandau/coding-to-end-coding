import java.util.Hashtable;

class Interpreter{
  //variables-------------------------------------------------------------------
  String name; //name of the interpreter
  String path; //execution path to the interpreter (probably with #!)
  Hashtable<String, String> commands; //all available commands; id -> code
  //constructors----------------------------------------------------------------
  public Interpreter(String n, String p, Hashtable<String, String> h){
    name = n;
    path = p;
    commands = h;
  }
  //subroutines-----------------------------------------------------------------
  //static subroutines----------------------------------------------------------
}
