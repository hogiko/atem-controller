/*
 * Copyright by https://conxult.de
 */
package de.conxult.tally.controller;

import java.text.MessageFormat;

/**
 *
 * @author joerg
 */
public class TallyLogger {

  static boolean debug = true;

  synchronized public static void info(String message, Object... parameters) {
    if (!debug) {
    } else if (parameters == null || parameters.length == 0) {
      System.out.println(message);
    } else if (message.contains("{0}")) {
      System.out.println(MessageFormat.format(message, parameters));
    } else {
      System.out.printf(message + "\n", parameters);
    }
  }

  public static void setDebug(boolean debug) {
    TallyLogger.debug = debug;
  }

}
