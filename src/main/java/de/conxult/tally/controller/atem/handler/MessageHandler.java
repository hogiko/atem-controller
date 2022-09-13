/*
 * Copyright by https://conxult.de
 */
package de.conxult.tally.controller.atem.handler;

import de.conxult.tally.controller.atem.AtemConfiguration;

/**
 *
 * @author joerg
 */
public class MessageHandler {

  protected AtemConfiguration configuration;

  public MessageHandler(AtemConfiguration configuration) {
    this.configuration = configuration;
  }

  public void handleMessage(String message) {
  }

  protected void splitMessage(String message, char split) {
    int indexOfSplit = message.indexOf(split);
    if (indexOfSplit != -1) {
      handleMessage(message.substring(0, indexOfSplit), message.substring(indexOfSplit + 1));
    }
  }

  public void handleMessage(String key, String value) {
  }

  public void close() {
  }
}
