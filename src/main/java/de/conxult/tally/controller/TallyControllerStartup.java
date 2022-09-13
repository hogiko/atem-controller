/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.conxult.tally.controller;

import de.conxult.tally.controller.atem.AtemClient;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 *
 * @author joerg
 */

@ApplicationScoped
public class TallyControllerStartup {

  @Inject
  TallyControllerConfiguration configuration;

  @Inject
  TallyControllerState states;

  AtemClient atemClient;

  void onStart(@Observes StartupEvent ev)
    throws Exception {
    TallyLogger.setDebug(configuration.isDebug());
    atemClient = new AtemClient(configuration.getAtemHostname(), configuration.getAtemPort())
      .setStates(states)
      .connect();
  }

  void onStop(@Observes ShutdownEvent ev) {
    atemClient.close();
  }
}
