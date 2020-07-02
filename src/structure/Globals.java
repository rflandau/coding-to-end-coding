import java.util.ArrayList;

/* Globals
  Stores information that is best if easily available to all components.
  */

public class Globals{
  //holds all interpreters
  static ArrayList<Interpreter> interps = new ArrayList<Interpreter>();
  //points to the current interpreter in 'interps'
  static int curInterp = 0;
}
