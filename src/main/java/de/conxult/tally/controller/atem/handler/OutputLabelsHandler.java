/*
 * Copyright by https://conxult.de
 */
package de.conxult.tally.controller.atem.handler;

import de.conxult.tally.controller.atem.AtemConfiguration;

/**
 *
 * @author joerg
 */
public class OutputLabelsHandler
  extends MessageHandler {

  public OutputLabelsHandler(AtemConfiguration configuration) {
    super(configuration);
  }

  @Override
  public void handleMessage(String message) {
    splitMessage(message, ' ');
  }

  @Override
  public void handleMessage(String key, String value) {
    configuration.setOutputLabel(key, value);
  }

}
