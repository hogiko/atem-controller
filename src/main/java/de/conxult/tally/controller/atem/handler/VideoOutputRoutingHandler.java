/*
 * Copyright by https://conxult.de
 */
package de.conxult.tally.controller.atem.handler;

import de.conxult.tally.controller.TallyControllerState;
import de.conxult.tally.controller.TallyLogger;
import de.conxult.tally.controller.atem.AtemConfiguration;
import de.conxult.tally.controller.domain.TallyLightState;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author joerg
 */
public class VideoOutputRoutingHandler
  extends MessageHandler {

  TallyControllerState states;
  Set<String>          changeSet = new HashSet<>();

  public VideoOutputRoutingHandler(AtemConfiguration configuration, TallyControllerState states) {
    super(configuration);
    this.states = states;
  }

  @Override
  public void handleMessage(String message) {
    splitMessage(message, ' ');
  }

  @Override
  public void handleMessage(String key, String value) {
    String name   = configuration.getInputLabels().get(value);
    String output = configuration.getOutputLabels().get(key);
    switch (key) {
      case "1":
        TallyLogger.info("SET %s %s: %s -> %s", key, value, name, output);
        states.getTallyLights(TallyLightState.ON_AIR).forEach(tls -> {
          tls.setState(TallyLightState.BLACK);
          changeSet.add(tls.getName());
        });
        states.getTallyLight(name).setState(TallyLightState.ON_AIR);
        changeSet.add(name);
        break;

      case "2":
        TallyLogger.info("SET %s %s: %s -> %s", key, value, name, output);
        states.getTallyLights(TallyLightState.STAND_BY).forEach(tls -> {
          tls.setState(TallyLightState.BLACK);
          changeSet.add(tls.getName());
          });
        if (!states.getTallyLight(name).isOnAir()) {
          states.getTallyLight(name).setState(TallyLightState.STAND_BY);
          changeSet.add(name);
        }
        break;

      default:
        TallyLogger.info("IGN %s %s: %s -> %s", key, value, name, output);
        break;

    }

    TallyLogger.info("SET %s", changeSet);
  }

  @Override
  public void close() {
    TallyLogger.info("ACK StandBy: " + states.getStandBy().getName());
    TallyLogger.info("ACK OnAir  : " + states.getOnAir().getName());
    TallyLogger.info("ACK %s", changeSet);

    new Thread(() -> {
      try {
        Thread.sleep(100L);
      } catch (Exception any) {
      } finally {
        changeSet.forEach((name) -> states.triggerPendingCalls(name));
      }}).start();
  }

}
