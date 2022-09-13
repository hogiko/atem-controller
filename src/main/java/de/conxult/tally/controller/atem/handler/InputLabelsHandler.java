/*
 * Copyright by https://conxult.de
 */
package de.conxult.tally.controller.atem.handler;

import de.conxult.tally.controller.atem.AtemConfiguration;

/**
 *
 * @author joerg
 */
public class InputLabelsHandler
  extends MessageHandler {

  public InputLabelsHandler(AtemConfiguration atemConfiguration) {
    super(atemConfiguration);
  }

  @Override
  public void handleMessage(String message) {
    splitMessage(message, ' ');
  }

  @Override
  public void handleMessage(String key, String value) {
    configuration.getInputLabels().put(key, value);
  }

}

