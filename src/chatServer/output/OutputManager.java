/*
 * Copyright 2002-2003 Peter Brandt-Erichsen, Ardeshir Bagheri, All Rights Reserved.
 */

package chatServer.output;

/**
 * Internet Computing and Software Development (ICSD)
 * Lab 11 - High Performance Threading of the HTTP Server, and Load Testing
 *
 * Output Manager
 *
 * Provides an API to manage the level of output the server
 * displays to the console. The main idea is to provide a
 * mechanism whereby the server can be configured to a
 * certain level of verbosity, at startup, and the verbosity
 * level will dictate how much output is sent to the console.
 *
 * The result is that we can configure the server's verbosity
 * without having to change any source code.
 *
 * The console output is controlled by the definition of
 * three levels of priority. These are
 *
 *       debug, info, error
 *
 * The priority relationship is defined as
 *
 *       debug < info < error
 *
 *    - error has higher priority than info, and debug
 *    - info has higher priority than debug
 *    - debug is the lowest priority
 *
 * Setting the priority to a certain level will
 * preclude all lower levels of priority. For
 * instance, if we set the priority to error, then
 * the debug and info levels will be precluded.
 *
 * To make this work, you simply set the verbosity level
 * of this class to one of these priorities, upon
 * startup.
 *
 * For example:
 *
 * OutputManager output = OutputManager.getReference();
 * output.setVerbosity("debug");
 * output.debug("this is a debug message");
 * outut.info("this is an info message");
 * output.error("this is an error message");
 *
 * - output to the console will be:
 * this is a debug message
 * this is an info message
 * this is an error message
 *
 *
 * Another example:
 *
 * OutputManager output = OutputManager.getReference();
 * output.setVerbosity("info");
 * output.debug("this is a debug message");
 * outut.info("this is an info message");
 * output.error("this is an error message");
 *
 * - output to the console will be:
 * this is an info message
 * this is an error message
 *
 * Note:
 * The debug message will not be sent to the console,
 * because the info priority level precludes debug
 * level messages.
 *
 *
 * Finally:
 *
 * OutputManager output = OutputManager.getReference();
 * output.setVerbosity("error");
 * output.debug("this is a debug message");
 * outut.info("this is an info message");
 * output.error("this is an error message");
 *
 * - output to the console will be:
 * this is an error message
 *
 * Note:
 * In this case both the debug and the info
 * priority levels have been precluded by the
 * error level priority.
 *
 *
 * There is also a force() method which will force
 * output to the console regardless of the verbosity level.
 *
 * This class is singleton.
 *
 * Created: 2002.11.27
 * @author Peter Brandt-Erichsen
*/
public class OutputManager {


   // static reference to this singleton class
   private static OutputManager singleton = null;

   // the verbosity level of this class
   private int verbosity = 0;

   // priority levels
   public final int DEBUG = 0;
   public final int INFO = 1;
   public final int ERROR = 2;


   // private constructor to enforce the singleton design pattern
   //
   private OutputManager() {}


	/**
	 * Returns a reference to this singleton class
	 */
	public static OutputManager getReference() {
		if(singleton==null) singleton = new OutputManager();
		return singleton;
	}

   /**
    * Sets the verbosity level
    *
    * @param verbosity specifies the level of output to
    * the console. This parameter must be one of
    *
    *     error | info | debug
    *
    */
   public void setVerbosity(String verbosity) {

      // default to debug
      if(verbosity==null) {
         this.verbosity = DEBUG;
      }
      else if(verbosity.equalsIgnoreCase("debug")) {
         this.verbosity = DEBUG;
      }
      else if(verbosity.equalsIgnoreCase("info")) {
         this.verbosity = INFO;
      }
      else if(verbosity.equalsIgnoreCase("error")) {
         this.verbosity = ERROR;
      }
      // default to debug
      else {
         this.verbosity = DEBUG;
      }
   }

   /**
    * Returns the verbosity level
    */
   public String getVerbosity() {

      if(verbosity==DEBUG) return "debug";
      if(verbosity==INFO) return "info";
      if(verbosity==ERROR) return "error";

      // should never reach this point
      return "n/a";
   }

   /**
    * Writes the specified debug message to the console
    *
    * @param message is the message to be directed to the console
    */
   public void debug(String message) {

      if(verbosity <= DEBUG) {
         System.out.println(message);
      }
   }


   /**
    * Writes the specified info message to the console
    *
    * @param message is the message to be directed to the console
    */
   public void info(String message) {

      if(verbosity <= INFO) {
         System.out.println(message);
      }
   }


   /**
    * Writes the specified error message to the console
    *
    * @param message is the message to be directed to the console
    */
   public void error(String message) {

      if(verbosity <= ERROR) {
         System.out.println(message);
      }
   }

   /**
    * Forces the specified message to the console regardless of the verbosity level
    *
    * @param message is the output message to force to the console
    */
   public void force(String message) {
      System.out.println(message);
   }

}// end class
