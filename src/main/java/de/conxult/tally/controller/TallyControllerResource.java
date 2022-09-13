package de.conxult.tally.controller;

import de.conxult.tally.controller.TallyControllerState.PendingCall;
import de.conxult.tally.controller.domain.TallyLightInfo;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/*
 * Copyright by https://conxult.de
 */

/**
 *
 * @author joerg
 */
@RequestScoped
@Path("/controller")
public class TallyControllerResource {

  @Inject
  TallyControllerState states;

  @Inject
  HttpServletRequest request;

  static Map<String, Date> lastRemoteStamps = new ConcurrentHashMap<>();

  @GET
  @Path("tallyLights")
  public Map<String, TallyLightInfo> getTallyLights(
  ) {
    return states.getTallyLights();
  }

  @GET
  @Path("tallyLight")
  public TallyLightInfo getTallyLight(
    @QueryParam("name")    String       name,
    @QueryParam("timeout") Integer      milliSeconds
  ) {
    String         clientIdentifier = request.getRemoteAddr();
    TallyLightInfo tallyLight       = states.getTallyLight(name);

    Date lastRemoteStamp = lastRemoteStamps.get(clientIdentifier);
    if (lastRemoteStamp == null || lastRemoteStamp.before(tallyLight.getStamp())) {
      TallyLogger.info("STA %s -> %s @%s", name, tallyLight.getState(), clientIdentifier);

    } else
      if (milliSeconds != null && milliSeconds > 0) {
      PendingCall pendingCall = new PendingCall(name);
      states.addPendingCall(name, pendingCall);
      try {
        pendingCall.get(milliSeconds, TimeUnit.MILLISECONDS);
        TallyLogger.info("CHG %s -> %s", name, tallyLight.getState());
        states.removePendingCall(name, pendingCall);
      } catch (ExecutionException | InterruptedException | TimeoutException waitingException) {
        TallyLogger.info("TIM %s -> %s", name, tallyLight.getState());
      }
    }
    TallyLogger.info("RET %s -> %s", name, tallyLight.getState());
    lastRemoteStamps.put(clientIdentifier, tallyLight.getStamp());
    return tallyLight;
  }

  @GET
  @Path("standBy")
  public TallyLightInfo getStandBy() {
    return states.getStandBy();
  }

  @GET
  @Path("onAir")
  public TallyLightInfo getOnAir() {
    return states.getOnAir();
  }

}
