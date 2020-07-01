import java.util.ArrayList;
import java.util.Hashtable;

/* Coding
  The main java file. */

public class Coding_To_End_Coding{
  //main------------------------------------------------------------------------
  public static void main(String[] args){
    Globals.interps = generateInterpreters();

    System.exit(0);
  }
  //static subroutines----------------------------------------------------------
  /* generateInterpreters
    Used to populate the ArrayList of interpreter objects (as well as fill their
    fields).
    NOTE: Currently just creates the test interpreter. */
  static ArrayList<Interpreter> generateInterpreters(){
    //variables
    Hashtable<String, String> ht = new Hashtable<String, String>();
    Interpreter bash;
    ArrayList<Interpreter> toReturn = new ArrayList<Interpreter>();

    //generate test bash ht
    ht.put("helloworld", "echo \" Hello World\"");

    //create bash Interpreter object
    bash = new Interpreter("bash", "#!/bin/bash", ht);

    //add bash to AL
    toReturn.add(bash);

    return toReturn;
  }
}
