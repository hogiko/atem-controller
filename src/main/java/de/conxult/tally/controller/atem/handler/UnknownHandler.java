/*
 * Copyright by https://conxult.de
 */
package de.conxult.tally.controller.atem.handler;

import de.conxult.tally.controller.atem.AtemConfiguration;

/**
 *
 * @author joerg
 */
public class UnknownHandler
  extends MessageHandler {

  String topic;

  public UnknownHandler(AtemConfiguration configuration, String topic) {
    super(configuration);
    this.topic = topic;
  }
}
