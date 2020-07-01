import java.util.ArrayList;

/* Globals
  Stores information that is best if easily available to all components.
  */

public class Globals{
  //holds all interpreters
  static ArrayList<Interpreter> interps = new ArrayList<Interpreter>();
  //points to the current interpreter in 'interps'
  static int curInterp = 0;





  /* NOTE: For the time being, we have one hashtable of commands, named for
    its interpreter. When we add support for more interpreters/languages, we
    will need toi figure a method of storing multiple */
}
