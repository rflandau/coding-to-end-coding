package structure;

import java.util.ArrayList;

/* Globals
  Stores information that is best if easily available to all components.
  Could probably be refactored out, with interps and curInterp passed instead.*/

public class Globals{
    //holds all interpreters. generateInterpreters must be called to init it
    static ArrayList<Interpreter> interps;
    //points to the current interpreter in 'interps'
    static int curInterp = 0;

	/* getCurInterp
		returns the Interpreter currently referenced by curInterp */
	static Interpreter getCurInterp(){ return interps.get(curInterp); }
}
